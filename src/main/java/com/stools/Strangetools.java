package com.stools;

import com.stools.block.ModBlocks;
import com.stools.config.BaseModConfig;
import com.stools.config.ModConfigManager;
import com.stools.enchantment.ModEnchantments;
import com.stools.entity.ModEntities;
import com.stools.event.ModEvents;
import com.stools.item.ModItemGroups;
import com.stools.item.ModItems;
import com.stools.loot.ModLootTableModifier;
import com.stools.sound.ModSoundEvents;
import com.stools.wordgen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.registry.*;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Strangetools implements ModInitializer {
	public static final String MOD_ID = "strangetools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 注册配置管理器
		ModConfigManager.register();

		// 注册基础内容
		ModItems.registerItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.registerGroups();
		ModItemGroups.modifyVanillaGroups();
		ModLootTableModifier.registerModifications();
		ModEntities.registerEntities();
		ModEvents.register();
		ModSoundEvents.registerSounds();
		ModEnchantments.registerEnchantments();

		// 注册世界生成
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			ModWorldGen.registerBiomeModifications();
			LOGGER.info("World generation registration completed!");
		});

		// 引导世界生成
		DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
			// 获取配置特征注册表
			Registry<ConfiguredFeature<?, ?>> configuredRegistry = registryManager
					.getOptional(RegistryKeys.CONFIGURED_FEATURE)
					.orElse(null);

			// 获取放置特征注册表
			Registry<PlacedFeature> placedRegistry = registryManager
					.getOptional(RegistryKeys.PLACED_FEATURE)
					.orElse(null);

			if (configuredRegistry != null && placedRegistry != null) {
				// 引导配置特征
				ModWorldGen.bootstrapConfigured(configuredRegistry);

				// 引导放置特征
				ModWorldGen.bootstrapPlaced(placedRegistry, configuredRegistry);
			} else {
				LOGGER.error("Failed to obtain configuration features or place feature registry");
			}
		});

		LOGGER.info("StrangeTools mod initialization completed!");
	}
}
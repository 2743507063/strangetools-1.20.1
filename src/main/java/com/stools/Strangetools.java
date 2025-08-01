package com.stools;

import com.stools.block.ModBlocks;
import com.stools.config.ModConfigManager;
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

		// 在动态注册表设置时注册世界生成特征
		DynamicRegistrySetupCallback.EVENT.register(registrySetup -> {
			// 使用 asDynamicRegistryManager() 获取动态注册表管理器
			ModWorldGen.register(registrySetup.asDynamicRegistryManager());
			LOGGER.info("World generation features registered successfully in dynamic registry setup");
		});

		// 在标签加载后注册生物群系修改
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			ModWorldGen.registerBiomeModifications();
			LOGGER.info("World generation biome modifications completed!");
		});

		LOGGER.info("StrangeTools mod initialization completed!");
	}
}
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

		// 注册世界生成动态注册表回调
		ModWorldGen.registerDynamicRegistry();

		// 注册世界生成生物群系修改
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			// 确保在标签加载后注册生物群系修改
			ModWorldGen.registerBiomeModifications();
			LOGGER.info("World generation registration completed!");
		});

		LOGGER.info("StrangeTools mod initialization completed!");
	}
}
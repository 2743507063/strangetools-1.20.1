package com.stools;

import com.stools.config.ModConfigManager;
import com.stools.enchantment.ModEnchantments;
import com.stools.entity.ModEntities;
import com.stools.event.ModEvents;
import com.stools.item.ModItemGroups;
import com.stools.item.ModItems;
import com.stools.sound.ModSoundEvents;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Strangetools implements ModInitializer {
	public static final String MOD_ID = "strangetools";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModConfigManager.register();

		ModItems.registerItems();
		ModItemGroups.registerGroups();
		ModEntities.registerEntities();
		ModEvents.register();
		ModSoundEvents.registerSounds();
		ModEnchantments.registerEnchantments();
		LOGGER.info("Hello Fabric world!");
	}
}
package com.stools.config;

import com.stools.Strangetools;
import com.stools.config.BaseModConfig;

import java.lang.reflect.Method;

public class ModConfigManager {
    public static final BaseModConfig CONFIG = new BaseModConfig();
    public static boolean clothConfigPresent = false;

    public static void register() {
        try {
            Class.forName("me.shedaniel.autoconfig.AutoConfig");
            clothConfigPresent = true;
            ClothModConfig.initialize();
            Strangetools.LOGGER.info("Cloth Config integration enabled");
        } catch (ClassNotFoundException e) {
            Strangetools.LOGGER.info("Cloth Config not found, using default config");
        }
    }
}
package com.stools.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ModConfigManager {
    public static ModConfig CONFIG;
    private static ConfigHolder<ModConfig> configHolder;

    public static void register() {
        // 注册配置类
        configHolder = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        // 获取配置实例
        reload();
    }

    public static void reload() {
        CONFIG = configHolder.getConfig();
    }
}
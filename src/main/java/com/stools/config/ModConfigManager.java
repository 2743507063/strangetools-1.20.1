package com.stools.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ModConfigManager {
    public static ModConfig CONFIG;

    public static void register() {
        // 注册配置类
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        // 获取配置实例
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

}
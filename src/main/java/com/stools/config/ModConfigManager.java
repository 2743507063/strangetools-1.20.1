package com.stools.config;

import com.stools.Strangetools;
import com.stools.config.BaseModConfig;

import java.lang.reflect.Method;

public class ModConfigManager {
    public static BaseModConfig CONFIG = new BaseModConfig();
    private static boolean clothConfigPresent = false;

    static {
        try {
            // 检查 Cloth Config 核心类是否存在
            Class.forName("me.shedaniel.autoconfig.AutoConfig");
            clothConfigPresent = true;
        } catch (ClassNotFoundException e) {
            Strangetools.LOGGER.info("Cloth-config not detected, using default configuration");
        }
    }

    public static void register() {
        if (!clothConfigPresent) {
            return;
        }

        //直接初始化配置
        try {
            // 使用类加载器确保配置类被正确加载
            Class<?> clothConfigClass = Class.forName("com.stools.config.ClothModConfig");

            // 调用静态初始化方法
            Method initMethod = clothConfigClass.getMethod("init");
            initMethod.invoke(null);

            Strangetools.LOGGER.info("Cloth-config configuration loaded successfully");
        } catch (Exception e) {
            Strangetools.LOGGER.error("Failed to load Cloth-config integration", e);
        }
    }
}
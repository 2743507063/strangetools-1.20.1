package com.stools.config;

import com.stools.Strangetools;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class ModConfigManager {
    public static BaseModConfig CONFIG = new BaseModConfig();
    private static boolean clothConfigPresent = false;

    static {
        try {
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

        try {
            // 反射加载核心类
            Class<?> autoConfigClass = Class.forName("me.shedaniel.autoconfig.AutoConfig");
            Class<?> janksonSerializerClass = Class.forName("me.shedaniel.autoconfig.serializer.JanksonConfigSerializer");
            Class<?> clothConfigClass = Class.forName("com.stools.config.ClothModConfig");
            Class<?> janksonClass = Class.forName("blue.endless.jankson.Jankson");

            // 获取 AutoConfig.register 方法
            Method registerMethod = autoConfigClass.getMethod("register",
                    Class.class,
                    Class.forName("me.shedaniel.autoconfig.serializer.ConfigSerializerSupplier")
            );

            // 创建 Jankson 实例（通过 builder 模式）
            Method builderMethod = janksonClass.getMethod("builder");
            Object janksonBuilder = builderMethod.invoke(null);
            Method buildMethod = janksonBuilder.getClass().getMethod("build");
            Object janksonInstance = buildMethod.invoke(janksonBuilder);

            // 关键修复：动态匹配 JanksonConfigSerializer 的构造函数
            Object janksonSerializer = createJanksonSerializer(janksonSerializerClass, janksonClass, janksonInstance);

            // 注册配置并同步
            Object configHolder = registerMethod.invoke(null, clothConfigClass, janksonSerializer);
            Method getConfigMethod = configHolder.getClass().getMethod("getConfig");
            Object clothConfigInstance = getConfigMethod.invoke(configHolder);
            Method copyToMethod = clothConfigClass.getMethod("copyTo", BaseModConfig.class);
            copyToMethod.invoke(clothConfigInstance, CONFIG);

            Strangetools.LOGGER.info("Cloth-config configuration loaded successfully");
        } catch (Exception e) {
            Strangetools.LOGGER.error("Cloth-config failed to load, using default configuration", e);
            CONFIG = new BaseModConfig();
        }
    }

    // 动态创建 JanksonConfigSerializer 实例，兼容不同构造函数参数
    private static Object createJanksonSerializer(Class<?> serializerClass, Class<?> janksonClass, Object janksonInstance) throws Exception {
        // 尝试所有构造函数，找到可匹配的
        for (Constructor<?> constructor : serializerClass.getConstructors()) {
            Class<?>[] paramTypes = constructor.getParameterTypes();

            // 情况1：构造函数需要 Jankson 实例
            if (paramTypes.length == 1 && paramTypes[0].equals(janksonClass)) {
                return constructor.newInstance(janksonInstance);
            }

            // 情况2：构造函数需要 Supplier<Jankson>
            if (paramTypes.length == 1 && paramTypes[0].equals(Supplier.class)) {
                Supplier<?> janksonSupplier = () -> janksonInstance;
                return constructor.newInstance(janksonSupplier);
            }

            // 情况3：构造函数无参数（旧版本可能支持）
            if (paramTypes.length == 0) {
                return constructor.newInstance();
            }
        }

        // 所有构造函数都不匹配时抛出异常
        throw new NoSuchMethodException("No suitable JanksonConfigSerializer constructor found");
    }

    public static void reload() {
        if (!clothConfigPresent) return;

        try {
            Class<?> autoConfigClass = Class.forName("me.shedaniel.autoconfig.AutoConfig");
            Class<?> clothConfigClass = Class.forName("com.stools.config.ClothModConfig");

            Method getConfigHolderMethod = autoConfigClass.getMethod("getConfigHolder", Class.class);
            Object configHolder = getConfigHolderMethod.invoke(null, clothConfigClass);

            Method loadMethod = configHolder.getClass().getMethod("load");
            loadMethod.invoke(configHolder);

            Method getConfigMethod = configHolder.getClass().getMethod("getConfig");
            Object clothConfigInstance = getConfigMethod.invoke(configHolder);

            Method copyToMethod = clothConfigClass.getMethod("copyTo", BaseModConfig.class);
            copyToMethod.invoke(clothConfigInstance, CONFIG);
        } catch (Exception e) {
            Strangetools.LOGGER.error("Failed to reload configuration", e);
        }
    }
}

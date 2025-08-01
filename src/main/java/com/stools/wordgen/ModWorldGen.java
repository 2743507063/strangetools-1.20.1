package com.stools.wordgen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;

public class ModWorldGen {
    public static final RuleTest END_STONE_RULE = new BlockMatchRuleTest(Blocks.END_STONE);
    private static boolean isBootstrapped = false; // 防止重复初始化

    // 矿石注册键
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_ENDER_KEY = RegistryKey.of(
            RegistryKeys.CONFIGURED_FEATURE,
            new Identifier(Strangetools.MOD_ID, "ore_ender")
    );
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_VOID_KEY = RegistryKey.of(
            RegistryKeys.CONFIGURED_FEATURE,
            new Identifier(Strangetools.MOD_ID, "ore_void")
    );

    public static final RegistryKey<PlacedFeature> ORE_ENDER_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE,
            new Identifier(Strangetools.MOD_ID, "ore_ender_placed")
    );
    public static final RegistryKey<PlacedFeature> ORE_VOID_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE,
            new Identifier(Strangetools.MOD_ID, "ore_void_placed")
    );

    // 注册动态注册表回调
    public static void registerDynamicRegistry() {
        DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
            Strangetools.LOGGER.info("Registering world generation features...");

            // 获取配置特征注册表
            Registry<ConfiguredFeature<?, ?>> configuredRegistry = registryManager
                    .getOptional(RegistryKeys.CONFIGURED_FEATURE)
                    .orElseThrow(() -> new IllegalStateException("Configured feature registry not found"));

            // 获取放置特征注册表
            Registry<PlacedFeature> placedRegistry = registryManager
                    .getOptional(RegistryKeys.PLACED_FEATURE)
                    .orElseThrow(() -> new IllegalStateException("Placed feature registry not found"));

            // 引导注册
            bootstrapConfigured(configuredRegistry);
            bootstrapPlaced(placedRegistry, configuredRegistry);

            Strangetools.LOGGER.info("World generation features registered successfully");
            isBootstrapped = true;
        });
    }

    // 引导配置特征
    public static void bootstrapConfigured(Registry<ConfiguredFeature<?, ?>> registry) {
        // 末影矿配置
        ConfiguredFeature<?, ?> oreEnder = new ConfiguredFeature<>(
                Feature.ORE,
                new OreFeatureConfig(
                        END_STONE_RULE,
                        ModBlocks.ENDER_ORE.getDefaultState(),
                        3 // 矿脉大小
                )
        );

        // 虚空矿配置
        ConfiguredFeature<?, ?> oreVoid = new ConfiguredFeature<>(
                Feature.ORE,
                new OreFeatureConfig(
                        END_STONE_RULE,
                        ModBlocks.VOID_ORE.getDefaultState(),
                        5 // 矿脉大小
                )
        );

        // 注册到注册表
        Registry.register(registry, ORE_ENDER_KEY, oreEnder);
        Registry.register(registry, ORE_VOID_KEY, oreVoid);

        Strangetools.LOGGER.info("Configured features registered: ender_ore, void_ore");
    }

    // 引导放置特征
    public static void bootstrapPlaced(Registry<PlacedFeature> registry, Registry<ConfiguredFeature<?, ?>> configuredRegistry) {
        // 获取配置特征
        ConfiguredFeature<?, ?> enderFeature = configuredRegistry.getOrThrow(ORE_ENDER_KEY);
        ConfiguredFeature<?, ?> voidFeature = configuredRegistry.getOrThrow(ORE_VOID_KEY);

        // 创建配置特征条目（使用更兼容的方式）
        RegistryEntry<ConfiguredFeature<?, ?>> enderEntry = RegistryEntry.of(enderFeature);
        RegistryEntry<ConfiguredFeature<?, ?>> voidEntry = RegistryEntry.of(voidFeature);

        // 创建放置特征
        PlacedFeature placedEnder = new PlacedFeature(
                enderEntry,
                List.of(
                        CountPlacementModifier.of(5), // 每区块5个矿脉
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                YOffset.fixed(5),
                                YOffset.fixed(40) // 在Y=5到40层生成
                        ),
                        BiomePlacementModifier.of()
                )
        );

        PlacedFeature placedVoid = new PlacedFeature(
                voidEntry,
                List.of(
                        CountPlacementModifier.of(10), // 每区块10个矿脉
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                YOffset.fixed(5),
                                YOffset.fixed(25) // 在Y=5到25层生成
                        ),
                        BiomePlacementModifier.of(),
                        EnvironmentScanPlacementModifier.of(
                                Direction.DOWN,
                                BlockPredicate.IS_AIR,
                                8 // 扫描8格范围内是否有空气
                        )
                )
        );

        // 注册放置特征
        Registry.register(registry, ORE_ENDER_PLACED_KEY, placedEnder);
        Registry.register(registry, ORE_VOID_PLACED_KEY, placedVoid);

        Strangetools.LOGGER.info("Placed features registered: ender_ore_placed, void_ore_placed");
    }

    public static void registerBiomeModifications() {
        if (!isBootstrapped) {
            Strangetools.LOGGER.warn("World generation features not registered yet! Skipping biome modifications.");
            return;
        }

        Strangetools.LOGGER.info("Registering biome modifications...");

        // 末影矿添加到所有末地生物群系
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ORE_ENDER_PLACED_KEY
        );

        // 虚空矿只添加到末地小型岛屿生物群系
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.SMALL_END_ISLANDS),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ORE_VOID_PLACED_KEY
        );

        Strangetools.LOGGER.info("Biome modifications registered successfully");
    }
}
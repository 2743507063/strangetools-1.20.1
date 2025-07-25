package com.stools.wordgen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
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

import java.util.List;

public class ModWorldGen {
    public static final RuleTest END_STONE_RULE = new BlockMatchRuleTest(Blocks.END_STONE);

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
    }

    // 引导放置特征
    public static void bootstrapPlaced(Registry<PlacedFeature> registry, Registry<ConfiguredFeature<?, ?>> configuredRegistry) {
        // 末影矿放置特征
        ConfiguredFeature<?, ?> enderFeature = configuredRegistry.get(ORE_ENDER_KEY);
        if (enderFeature == null) {
            throw new IllegalStateException("配置特征未注册: " + ORE_ENDER_KEY);
        }

        PlacedFeature placedEnder = new PlacedFeature(
                RegistryEntry.of(enderFeature),
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

        ConfiguredFeature<?, ?> voidFeature = configuredRegistry.get(ORE_VOID_KEY);
        if (voidFeature == null) {
            throw new IllegalStateException("配置特征未注册: " + ORE_VOID_KEY);
        }

        PlacedFeature placedVoid = new PlacedFeature(
                RegistryEntry.of(voidFeature),
                List.of(
                        CountPlacementModifier.of(10), // 每区块10个矿脉
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                YOffset.fixed(5), // 从Y=5开始
                                YOffset.fixed(25) // 到Y=25结束
                        ),
                        // 只在岛屿边缘生成
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
    }

    public static void registerBiomeModifications() {
        // 末影矿添加到所有末地生物群系
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ORE_ENDER_PLACED_KEY
        );

        // 虚空矿只添加到末地小型岛屿生物群系
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        net.minecraft.world.biome.BiomeKeys.SMALL_END_ISLANDS
                ),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ORE_VOID_PLACED_KEY
        );
    }
}
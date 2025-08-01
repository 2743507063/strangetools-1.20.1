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
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;

public class ModWorldGen {
    public static final RuleTest END_STONE_RULE = new BlockMatchRuleTest(Blocks.END_STONE);
    private static boolean isRegistered = false;

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

    public static void register(DynamicRegistryManager registries) {
        if (isRegistered) return;

        // 获取配置特征注册表
        Registry<ConfiguredFeature<?, ?>> configuredRegistry = registries.get(RegistryKeys.CONFIGURED_FEATURE);
        // 获取放置特征注册表
        Registry<PlacedFeature> placedRegistry = registries.get(RegistryKeys.PLACED_FEATURE);

        // 注册配置特征
        Registry.register(
                configuredRegistry,
                ORE_ENDER_KEY.getValue(),
                new ConfiguredFeature<>(
                        Feature.ORE,
                        new OreFeatureConfig(
                                END_STONE_RULE,
                                ModBlocks.ENDER_ORE.getDefaultState(),
                                3
                        )
                )
        );

        Registry.register(
                configuredRegistry,
                ORE_VOID_KEY.getValue(),
                new ConfiguredFeature<>(
                        Feature.ORE,
                        new OreFeatureConfig(
                                END_STONE_RULE,
                                ModBlocks.VOID_ORE.getDefaultState(),
                                5
                        )
                )
        );

        // 注册放置特征
        Registry.register(
                placedRegistry,
                ORE_ENDER_PLACED_KEY.getValue(),
                new PlacedFeature(
                        RegistryEntry.of(configuredRegistry.getOrThrow(ORE_ENDER_KEY)),
                        List.of(
                                CountPlacementModifier.of(5),
                                SquarePlacementModifier.of(),
                                HeightRangePlacementModifier.uniform(
                                        YOffset.fixed(5),
                                        YOffset.fixed(40)
                                ),
                                BiomePlacementModifier.of()
                        )
                )
        );

        Registry.register(
                placedRegistry,
                ORE_VOID_PLACED_KEY.getValue(),
                new PlacedFeature(
                        RegistryEntry.of(configuredRegistry.getOrThrow(ORE_VOID_KEY)),
                        List.of(
                                CountPlacementModifier.of(10),
                                SquarePlacementModifier.of(),
                                HeightRangePlacementModifier.uniform(
                                        YOffset.fixed(5),
                                        YOffset.fixed(25)
                                ),
                                BiomePlacementModifier.of(),
                                EnvironmentScanPlacementModifier.of(
                                        Direction.DOWN,
                                        BlockPredicate.IS_AIR,
                                        8
                                )
                        )
                )
        );

        isRegistered = true;
        Strangetools.LOGGER.info("World generation features registered successfully");
    }

    public static void registerBiomeModifications() {
        if (!isRegistered) {
            Strangetools.LOGGER.error("World generation features not registered! Skipping biome modifications.");
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
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
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;

public class ModWorldGen {
    public static final RuleTest END_STONE_RULE = new BlockMatchRuleTest(Blocks.END_STONE);
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_ENDER_KEY = RegistryKey.of(
            RegistryKeys.CONFIGURED_FEATURE,
            new Identifier(Strangetools.MOD_ID, "ore_ender")
    );

    public static final RegistryKey<PlacedFeature> ORE_ENDER_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE,
            new Identifier(Strangetools.MOD_ID, "ore_ender_placed")
    );

    // 引导配置特征
    public static void bootstrapConfigured(Registry<ConfiguredFeature<?, ?>> registry) {
        ConfiguredFeature<?, ?> oreEnder = new ConfiguredFeature<>(
                Feature.ORE,
                new OreFeatureConfig(
                        END_STONE_RULE,
                        ModBlocks.ENDER_ORE.getDefaultState(),
                        3
                )
        );

        // 直接注册到注册表
        Registry.register(registry, ORE_ENDER_KEY, oreEnder);
    }

    // 引导放置特征
    public static void bootstrapPlaced(Registry<PlacedFeature> registry, Registry<ConfiguredFeature<?, ?>> configuredRegistry) {
        // 获取已注册的配置特征
        ConfiguredFeature<?, ?> configuredFeature = configuredRegistry.get(ORE_ENDER_KEY);

        if (configuredFeature == null) {
            throw new IllegalStateException("配置特征未注册: " + ORE_ENDER_KEY);
        }

        // 创建放置特征
        PlacedFeature placedFeature = new PlacedFeature(
                RegistryEntry.of(configuredFeature),
                List.of(
                        CountPlacementModifier.of(15),
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                YOffset.fixed(5),
                                YOffset.fixed(50)
                        ),
                        BiomePlacementModifier.of()
                )
        );

        // 直接注册到注册表
        Registry.register(registry, ORE_ENDER_PLACED_KEY, placedFeature);
    }

    public static void registerBiomeModifications() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ORE_ENDER_PLACED_KEY
        );
    }
}
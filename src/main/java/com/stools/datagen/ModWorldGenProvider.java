package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends FabricDynamicRegistryProvider {
    public ModWorldGenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        // 创建 RuleTest 来匹配标签
        RuleTest stoneReplacables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplacables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // 创建配置特征的RegistryKey
        RegistryKey<ConfiguredFeature<?, ?>> bedrockOreKey = RegistryKey.of(
                RegistryKeys.CONFIGURED_FEATURE,
                new Identifier(Strangetools.MOD_ID, "bedrock_ore")
        );

        // 配置基岩矿石特征
        entries.add(
                bedrockOreKey,
                new ConfiguredFeature<>(
                        Feature.ORE,
                        new OreFeatureConfig(
                                List.of(
                                        OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.BEDROCK_ORE.getDefaultState()),
                                        OreFeatureConfig.createTarget(deepslateReplacables, ModBlocks.BEDROCK_ORE.getDefaultState())
                                ),
                                3 // 矿脉大小
                        )
                )
        );

        // 创建放置特征的RegistryKey
        RegistryKey<PlacedFeature> bedrockOrePlacedKey = RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                new Identifier(Strangetools.MOD_ID, "bedrock_ore_placed")
        );

        // 配置放置规则
        entries.add(
                bedrockOrePlacedKey,
                new PlacedFeature(
                        entries.ref(bedrockOreKey), // 引用配置特征
                        List.of(
                                CountPlacementModifier.of(1), // 每个区块生成次数
                                SquarePlacementModifier.of(),
                                HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(5)) // Y=-64 到 Y=5
                        )
                )
        );
    }

    @Override
    public String getName() {
        return "World Gen";
    }
}
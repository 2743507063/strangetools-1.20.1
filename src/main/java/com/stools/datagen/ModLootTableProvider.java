package com.stools.datagen;

import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModLootTableProvider extends SimpleFabricLootTableProvider {

    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // 数据输出和战利品表类型
        super(dataOutput, LootContextTypes.CHEST);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        // 添加到末地城宝箱
        exporter.accept(
                new Identifier("minecraft", "chests/end_city_treasure"),
                LootTable.builder()
                        .pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .conditionally(RandomChanceLootCondition.builder(1.0f)) // 25% 几率生成
                                .with(ItemEntry.builder(ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))
                                )
                        )
        );

        // 添加更多战利品表...
    }
}
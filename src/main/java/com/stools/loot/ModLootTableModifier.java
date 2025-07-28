package com.stools.loot;

import com.stools.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifier {

    private static final Identifier END_CITY_TREASURE_ID = new Identifier("minecraft", "chests/end_city_treasure");
    private static final Identifier VILLAGE_BLACKSMITH_ID = new Identifier("minecraft", "chests/village/village_weaponsmith");
    private static final Identifier VILLAGE_TOOLSMITH_ID = new Identifier("minecraft", "chests/village/village_toolsmith");
    private static final Identifier VILLAGE_ARMORER_ID = new Identifier("minecraft", "chests/village/village_armorer");
    public static void registerModifications() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            // 修改末地城宝箱战利品表
            if (END_CITY_TREASURE_ID.equals(id)) {
                // 添加新的战利品池
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.20f)) // 25% 几率
                        .with(ItemEntry.builder(ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE))
                        .conditionally(RandomChanceLootCondition.builder(0.15f)) // 15% 几率
                        .with(ItemEntry.builder(ModItems.VOID_INGOT))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)));
                
                tableBuilder.pool(poolBuilder);
            }
            if (VILLAGE_BLACKSMITH_ID.equals(id) ||
                    VILLAGE_TOOLSMITH_ID.equals(id) ||
                    VILLAGE_ARMORER_ID.equals(id)) {

                LootPool.Builder overworldPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.25f)) // 25% 几率
                        .with(ItemEntry.builder(ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)));

                tableBuilder.pool(overworldPool);
            }
        });
    }
}
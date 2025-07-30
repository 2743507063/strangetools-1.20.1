package com.stools.datagen;

import com.stools.block.ModBlocks;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ENDER_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.VOID_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SLICE_OF_CAKE,Models.GENERATED);
        itemModelGenerator.register(ModItems.TEST_ITEM,Models.HANDHELD);
        itemModelGenerator.register(ModItems.ENDER_ALLOY_INGOT,Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_ALLOY_SCRAP,Models.GENERATED);
        itemModelGenerator.register(ModItems.VOID_INGOT,Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_VOID,Models.GENERATED);
        itemModelGenerator.register(ModItems.VOID_PEARL,Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE,Models.GENERATED);
        itemModelGenerator.register(ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE,Models.GENERATED);
        itemModelGenerator.register(ModItems.MACE,Models.HANDHELD);
        for (String toolId : ModItems.TOOL_IDS) {
            if (toolId.startsWith("glass_")) continue; // 跳过玻璃工具
            Item item = ModItems.TOOLS.get(toolId);
            if (item != null) {
                itemModelGenerator.register(item, Models.HANDHELD);
            }
        }
        // 盔甲模型
        for (String armorId : ModItems.ARMOR_IDS) {
            Item item = ModItems.ARMORS.get(armorId);
            if (item instanceof ArmorItem armorItem) {
                itemModelGenerator.registerArmor(armorItem);
            }
        }
    }
}
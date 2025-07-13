package com.stools.datagen;

import com.stools.Strangetools;
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
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (String toolId : ModItems.TOOL_IDS) {
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
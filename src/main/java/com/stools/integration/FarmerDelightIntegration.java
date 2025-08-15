package com.stools.integration;

import com.stools.integration.farmersdelight.FarmerDelightToolMaterials;
import com.stools.item.ModItemGroups;
import com.stools.item.ModItems;
import com.stools.item.ToolFactory;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FarmerDelightIntegration {
    public static final List<String> FARMERS_DELIGHT_TOOL_IDS = new ArrayList<>();

    public static void register() {
        if (!FabricLoader.getInstance().isModLoaded("farmersdelight")) {
            return;
        }

        // 注册所有农夫乐事工具
        for (FarmerDelightToolMaterials material : FarmerDelightToolMaterials.values()) {
            String materialName = material.name().toLowerCase();
            ToolFactory.registerToolSet(material, materialName);
        }
    }

    public static Item getItem(String itemName) {
        return Registries.ITEM.get(new Identifier("farmersdelight", itemName));
    }
}
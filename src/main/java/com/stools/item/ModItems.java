package com.stools.item;

import com.stools.Strangetools;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static final Map<String, Item> TOOLS = new HashMap<>();
    public static final List<String> TOOL_IDS = new ArrayList<>();

    private static void registerToolSet(ModToolMaterials material, String materialName) {
        String prefix = materialName + "_";

        registerTool(prefix + "sword", ToolFactory.createSword(material, prefix + "sword"));
        registerTool(prefix + "pickaxe", ToolFactory.createPickaxe(material, prefix + "pickaxe"));
        registerTool(prefix + "axe", ToolFactory.createAxe(material, prefix + "axe"));
        registerTool(prefix + "shovel", ToolFactory.createShovel(material, prefix + "shovel"));
        registerTool(prefix + "hoe", ToolFactory.createHoe(material, prefix + "hoe"));
    }

    private static void registerTool(String id, Item item) {
        Item registeredItem = registerItem(id, item);

        TOOLS.put(id, registeredItem);
        TOOL_IDS.add(id);
    }

    public static void registerToolItems() {
        registerToolSet(ModToolMaterials.COPPER, "copper");
        registerToolSet(ModToolMaterials.EMERALD, "emerald");
        registerToolSet(ModToolMaterials.LAPIS, "lapis");
        registerToolSet(ModToolMaterials.REDSTONE, "redstone");
        registerToolSet(ModToolMaterials.QUARTZ, "quartz");
        registerToolSet(ModToolMaterials.COAL, "coal");
        registerToolSet(ModToolMaterials.CAKE, "cake");
        registerToolSet(ModToolMaterials.OBSIDIAN, "obsidian");
    }

    public static Item registerItem(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Strangetools.MOD_ID, id), item);
    }
    public static Item register(String id, Item item) {
        return register(new Identifier(Strangetools.MOD_ID, id), item);
    }

    public static Item register(Identifier id, Item item) {
        return register(RegistryKey.of(Registries.ITEM.getKey(), id), item);
    }

    public static Item register(RegistryKey<Item> key, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }
    public static void registerItems() {
        registerToolItems();
    }
}
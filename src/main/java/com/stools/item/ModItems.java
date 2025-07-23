package com.stools.item;

import com.stools.Strangetools;
import com.stools.item.custom.MaceItem;
import com.stools.item.materials.ModArmorMaterials;
import com.stools.item.materials.ModMaceMaterials;
import com.stools.item.materials.ModToolMaterials;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static final Item TEST_ITEM = registerItem("test_item",new SwordItem(ToolMaterials.WOOD,6,1,new Item.Settings().maxDamage(1)));
    public static final Item MACE = registerItem("mace",
            new MaceItem(ModMaceMaterials.IRON,new Item.Settings().maxCount(1))
    );
    public static final Item ENDER_ALLOY_INGOT = registerItem("ender_alloy_ingot", new Item(new Item.Settings()));

    public static final Map<String, Item> TOOLS = new HashMap<>();
    public static final List<String> TOOL_IDS = new ArrayList<>();
    public static final Map<String, Item> ARMORS = new HashMap<>();
    public static final List<String> ARMOR_IDS = new ArrayList<>();

    private static void registerToolSet(ModToolMaterials material, String materialName) {
        String prefix = materialName + "_";

        registerTool(prefix + "sword", ToolFactory.createSword(material, prefix + "sword"));
        registerTool(prefix + "pickaxe", ToolFactory.createPickaxe(material, prefix + "pickaxe"));
        registerTool(prefix + "axe", ToolFactory.createAxe(material, prefix + "axe"));
        registerTool(prefix + "shovel", ToolFactory.createShovel(material, prefix + "shovel"));
        registerTool(prefix + "hoe", ToolFactory.createHoe(material, prefix + "hoe"));
    }
    private static void registerArmorSet(ModArmorMaterials material, String materialName) {
        String prefix = materialName + "_";

        registerArmor(prefix + "helmet", ArmorFactory.createHelmet(material, prefix + "helmet"));
        registerArmor(prefix + "chestplate", ArmorFactory.createChestplate(material, prefix + "chestplate"));
        registerArmor(prefix + "leggings", ArmorFactory.createLeggings(material, prefix + "leggings"));
        registerArmor(prefix + "boots", ArmorFactory.createBoots(material, prefix + "boots"));
    }
    private static void registerTool(String id, Item item) {
        Item registeredItem = registerItem(id, item);

        TOOLS.put(id, registeredItem);
        TOOL_IDS.add(id);
    }
    private static void registerArmor(String id, Item item) {
        Item registeredItem = registerItem(id, item);
        ARMORS.put(id, registeredItem);
        ARMOR_IDS.add(id);
    }
    private static void registerMaceSet(ModMaceMaterials material, String materialName) {
        String id = materialName + "_mace";
        registerTool(id, MaceFactory.createMace(material, id));
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
        registerToolSet(ModToolMaterials.PRISMARINE, "prismarine");
        registerToolSet(ModToolMaterials.ROTTEN_FLESH, "rotten_flesh");
        registerToolSet(ModToolMaterials.GLOWSTONE, "glowstone");
        registerToolSet(ModToolMaterials.BLAZE_POWDER, "blaze_powder");
        registerToolSet(ModToolMaterials.GOLDEN_APPLE, "golden_apple");
        registerToolSet(ModToolMaterials.ENCHANTED_GOLDEN_APPLE, "enchanted_golden_apple");
        registerToolSet(ModToolMaterials.BEDROCK, "bedrock");
        registerToolSet(ModToolMaterials.BONE, "bone");
        registerToolSet(ModToolMaterials.NETHERRACK, "netherrack");
        registerToolSet(ModToolMaterials.NETHER_STAR, "nether_star");
        registerToolSet(ModToolMaterials.GLASS, "glass");
        registerToolSet(ModToolMaterials.SLIME, "slime");
        registerToolSet(ModToolMaterials.POTION, "potion");
        registerToolSet(ModToolMaterials.STRING, "string");
        registerToolSet(ModToolMaterials.ENDER_ALLOY, "ender_alloy");

        //盔甲
        registerArmorSet(ModArmorMaterials.EMERALD, "emerald");

        //重锤
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
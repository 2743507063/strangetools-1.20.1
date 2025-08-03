package com.stools.item;

import com.stools.Strangetools;
import com.stools.item.custom.MaceItem;
import com.stools.item.custom.VoidPearlItem;
import com.stools.item.materials.ModArmorMaterials;
import com.stools.item.materials.ModMaceMaterials;
import com.stools.item.materials.ModToolMaterials;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static final Item TEST_ITEM = registerItem("test_item", new SwordItem(ToolMaterials.WOOD, 6, 1, new Item.Settings().maxDamage(1)));
    public static final Item MACE = registerItem("mace",
            new MaceItem(ModMaceMaterials.IRON, new Item.Settings().maxCount(1))
    );
    public static final Item ENDER_ALLOY_INGOT = registerItem("ender_alloy_ingot", new Item(new Item.Settings()));
    public static final Item ENDER_ALLOY_SCRAP = registerItem("ender_alloy_scrap", new Item(new Item.Settings()));
    public static final Item ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE = registerItem(
            "ender_alloy_upgrade_smithing_template",
            createEnderAlloyUpgradeTemplate()
    );
    public static final Item APPLE_UPGRADE_SMITHING_TEMPLATE = registerItem("apple_upgrade_smithing_template",
            createAppleUpgradeTemplate()
    );
    public static final Item VOID_INGOT = registerItem("void_ingot", new Item(new Item.Settings()));
    public static final Item VOID_PEARL = registerItem("void_pearl",
            new VoidPearlItem(new Item.Settings())
    );
    public static final Item RAW_VOID = registerItem("raw_void", new Item(new Item.Settings()));

    public static final Item SLICE_OF_CAKE = registerItem("slice_of_cake",new Item(new Item.Settings().food(ModFoodComponents.S_CAKE)));

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
        registerToolSet(ModToolMaterials.DIRT, "dirt");
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
        registerToolSet(ModToolMaterials.APPLE, "apple");
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
        registerToolSet(ModToolMaterials.END_STONE, "end_stone");
        registerToolSet(ModToolMaterials.CHORUS_FRUIT, "chorus_fruit");
        registerToolSet(ModToolMaterials.VOID, "void");
        registerToolSet(ModToolMaterials.WATERMELON,"melon");
        registerToolSet(ModToolMaterials.SWEET_BERRIES,"sweet_berries");
        registerToolSet(ModToolMaterials.GLOW_BERRIES,"glow_berries");
        registerToolSet(ModToolMaterials.CARROT,"carrot");
        registerToolSet(ModToolMaterials.GOLDEN_CARROT,"golden_carrot");

        //盔甲
        registerArmorSet(ModArmorMaterials.COPPER, "copper");
        registerArmorSet(ModArmorMaterials.EMERALD, "emerald");
        registerArmorSet(ModArmorMaterials.LAPIS, "lapis");

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
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }

    public static void registerItems() {
        registerToolItems();
    }

    private static SmithingTemplateItem createEnderAlloyUpgradeTemplate() {
        // 自定义文本（这些需要添加到语言文件中）
        Text appliesTo = Text.translatable("item.strangetools.smithing_template.ender_alloy_upgrade.applies_to")
                .formatted(Formatting.BLUE);
        Text ingredients = Text.translatable("item.strangetools.smithing_template.ender_alloy_upgrade.ingredients")
                .formatted(Formatting.BLUE);
        Text title = Text.translatable("upgrade.strangetools.ender_alloy_upgrade")
                .formatted(Formatting.GRAY);
        Text baseSlotDesc = Text.translatable("item.strangetools.smithing_template.ender_alloy_upgrade.base_slot_description");
        Text additionsSlotDesc = Text.translatable("item.strangetools.smithing_template.ender_alloy_upgrade.additions_slot_description");

        // 基础槽位的空图标（显示在锻造界面）
        List<Identifier> emptyBaseSlots = List.of(
                new Identifier("item/empty_armor_slot_helmet"),
                new Identifier("item/empty_slot_sword"),
                new Identifier("item/empty_armor_slot_chestplate"),
                new Identifier("item/empty_slot_pickaxe"),
                new Identifier("item/empty_armor_slot_leggings"),
                new Identifier("item/empty_slot_axe"),
                new Identifier("item/empty_armor_slot_boots"),
                new Identifier("item/empty_slot_hoe"),
                new Identifier("item/empty_slot_shovel")
        );

        // 附加槽位的空图标
        List<Identifier> emptyAdditionsSlots = List.of(
                new Identifier("item/empty_slot_ingot")
        );

        return new SmithingTemplateItem(
                appliesTo,
                ingredients,
                title,
                baseSlotDesc,
                additionsSlotDesc,
                emptyBaseSlots,
                emptyAdditionsSlots
        ) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools.ender_alloy_upgrade_smithing_template";
            }
        };
    }

    private static SmithingTemplateItem createAppleUpgradeTemplate() {
        // 自定义文本
        Text appliesTo = Text.translatable("item.strangetools.smithing_template.apple_upgrade.applies_to")
                .formatted(Formatting.BLUE);
        Text ingredients = Text.translatable("item.strangetools.smithing_template.apple_upgrade.ingredients")
                .formatted(Formatting.BLUE);
        Text title = Text.translatable("upgrade.strangetools.apple_upgrade")
                .formatted(Formatting.GRAY);
        Text baseSlotDesc = Text.translatable("item.strangetools.smithing_template.apple_upgrade.base_slot_description");
        Text additionsSlotDesc = Text.translatable("item.strangetools.smithing_template.apple_upgrade.additions_slot_description");

        // 基础槽位的空图标
        List<Identifier> emptyBaseSlots = List.of(
                new Identifier("item/empty_armor_slot_helmet"),
                new Identifier("item/empty_slot_sword"),
                new Identifier("item/empty_armor_slot_chestplate"),
                new Identifier("item/empty_slot_pickaxe"),
                new Identifier("item/empty_armor_slot_leggings"),
                new Identifier("item/empty_slot_axe"),
                new Identifier("item/empty_armor_slot_boots"),
                new Identifier("item/empty_slot_hoe"),
                new Identifier("item/empty_slot_shovel")
        );

        // 附加槽位的空图标
        List<Identifier> emptyAdditionsSlots = List.of(
                new Identifier("item/empty_slot_ingot")
        );

        return new SmithingTemplateItem(
                appliesTo,
                ingredients,
                title,
                baseSlotDesc,
                additionsSlotDesc,
                emptyBaseSlots,
                emptyAdditionsSlots
        ) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools.apple_upgrade_smithing_template";
            }
        };
    }
}

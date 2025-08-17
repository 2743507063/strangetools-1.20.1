package com.stools.item;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> TOOLS_GROUP = register("tools_group");
    public static final RegistryKey<ItemGroup> ARMOR_GROUP = register("armor_group");

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Strangetools.MOD_ID, id));
    }

    public static void registerGroups() {
        final Item toolsIcon = ModItems.TOOLS.get("copper_sword") != null ?
                ModItems.TOOLS.get("copper_sword") :
                Items.IRON_SWORD;

        Registry.register(
                Registries.ITEM_GROUP,
                TOOLS_GROUP,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.strangetools.tools_group"))
                        .icon(() -> new ItemStack(toolsIcon))
                        .entries((displayContext, entries) -> {
                            // 按主题分类排序
                            Map<String, List<String>> categoryMap = new LinkedHashMap<>();

                            // 主世界工具
                            categoryMap.put("主世界", Arrays.asList(
                                    "dirt", "copper", "emerald", "lapis", "redstone", "coal",
                                    "obsidian", "prismarine","blue_ice", "rotten_flesh",
                                    "bone", "glass", "slime", "string","amethyst","flint","bedrock"
                            ));

                            // 下界工具
                            categoryMap.put("下界", Arrays.asList(
                                    "netherrack", "quartz", "nether_star","glowstone",
                                    "blaze_powder"
                            ));

                            // 末地工具
                            categoryMap.put("末地", Arrays.asList(
                                    "ender_alloy", "end_stone", "void"
                            ));

                            // 食物＆农作物工具
                            categoryMap.put("食物&农业", Arrays.asList(
                                    "apple", "golden_apple", "enchanted_golden_apple", "melon",
                                    "sweet_berries","glow_berries","carrot","golden_carrot","potato",
                                    "poisonous_potato","beetroot","wheat","dried_kelp","raw_beef",
                                    "chorus_fruit", "cake"
                            ));

                            // 按分类顺序添加工具
                            for (List<String> materials : categoryMap.values()) {
                                for (String material : materials) {
                                    for (String toolId : ModItems.TOOL_IDS) {
                                        if (toolId.startsWith(material + "_")) {
                                            Item tool = ModItems.TOOLS.get(toolId);
                                            if (tool != null) {
                                                entries.add(tool);
                                            }
                                        }
                                    }
                                }
                            }
                        })
                        .build()
        );

        final Item armorIcon = ModItems.ARMORS.get("emerald_chestplate") != null ?
                ModItems.ARMORS.get("emerald_chestplate") :
                Items.IRON_CHESTPLATE;

        Registry.register(
                Registries.ITEM_GROUP,
                ARMOR_GROUP,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.strangetools.armor_group"))
                        .icon(() -> new ItemStack(armorIcon))
                        .entries((displayContext, entries) -> {
                            List<String> materials = new ArrayList<>();
                            for (String armorId : ModItems.ARMOR_IDS) {
                                String material = armorId.split("_")[0];
                                if (!materials.contains(material)) {
                                    materials.add(material);
                                }
                            }

                            for (String material : materials) {
                                String[] types = {"helmet", "chestplate", "leggings", "boots"};
                                for (String type : types) {
                                    String armorId = material + "_" + type;
                                    if (ModItems.ARMOR_IDS.contains(armorId)) {
                                        Item armor = ModItems.ARMORS.get(armorId);
                                        if (armor != null) {
                                            entries.add(armor);
                                        }
                                    }
                                }
                            }
                        })
                        .build()
        );
    }
    public static void modifyVanillaGroups() {
        // 获取原版原材料组
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.NETHERITE_INGOT, ModItems.ENDER_ALLOY_SCRAP);
            entries.addAfter(ModItems.ENDER_ALLOY_SCRAP, ModItems.ENDER_ALLOY_INGOT);
            entries.addAfter(Items.RAW_GOLD, ModItems.RAW_VOID);
            entries.addAfter(ModItems.ENDER_ALLOY_INGOT, ModItems.VOID_INGOT);
            entries.addAfter(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE);
            entries.addAfter(Items.ENDER_EYE,ModItems.VOID_PEARL);
            entries.addAfter(Items.SNORT_POTTERY_SHERD,ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE);
            entries.addAfter(Items.GOLD_INGOT,ModItems.AMETHYST_INGOT);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Items.ANCIENT_DEBRIS, ModBlocks.VOID_ORE);
            entries.addAfter(ModBlocks.VOID_ORE, ModBlocks.ENDER_ORE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries ->
                entries.addAfter(Items.ENDER_EYE,ModItems.VOID_PEARL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries ->
                entries.addAfter(Items.CAKE,ModItems.SLICE_OF_CAKE));
        }
    }
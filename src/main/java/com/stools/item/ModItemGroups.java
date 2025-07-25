package com.stools.item;

import com.stools.Strangetools;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                            List<String> materials = new ArrayList<>();
                            for (String toolId : ModItems.TOOL_IDS) {
                                String material = toolId.split("_")[0];
                                if (!materials.contains(material)) {
                                    materials.add(material);
                                }
                            }

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
        // 获取原版原材料组（INGREDIENTS）
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.NETHERITE_INGOT, ModItems.ENDER_ALLOY_SCRAP);
            entries.addAfter(ModItems.ENDER_ALLOY_SCRAP, ModItems.ENDER_ALLOY_INGOT);
            entries.addAfter(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE);
        });
    }
}
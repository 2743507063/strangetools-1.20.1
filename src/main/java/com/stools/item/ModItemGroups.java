package com.stools.item;

import com.stools.Strangetools;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

    public static final RegistryKey<ItemGroup> STRANGETOOLS_GROUP = register("strangetools_group");

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Strangetools.MOD_ID, id));
    }

    public static void registerGroups() {
        Item iconItem = ModItems.TOOLS.get("copper_sword");
        final Item finalIconItem;
        if (iconItem == null) {
            finalIconItem = Items.IRON_SWORD;
        } else {
            finalIconItem = iconItem;
        }

        Registry.register(
                Registries.ITEM_GROUP,
                STRANGETOOLS_GROUP,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.strangetools.strangetools_group"))
                        .icon(() -> new ItemStack(finalIconItem))
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
    }
}
package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import com.stools.enchantment.ModEnchantments;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.List;

public class ModEnUsLangProvider extends FabricLanguageProvider {
    public ModEnUsLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.MACE,"Mace");
        translationBuilder.add(ModItems.TEST_ITEM,"TestItem:)");
        translationBuilder.add(ModEnchantments.DENSITY,"Density");
        translationBuilder.add(ModItems.ENDER_ALLOY_INGOT,"Ender Alloy Ingot");
        translationBuilder.add(ModItems.RAW_VOID, "Void Ore Crude");
        translationBuilder.add(ModItems.VOID_INGOT, "Void Ingot");
        translationBuilder.add(ModBlocks.ENDER_ORE, "Ender Ore");
        translationBuilder.add(ModBlocks.VOID_ORE, "Void Ore");
        translationBuilder.add(ModItems.ENDER_ALLOY_SCRAP,"Ender Alloy Scrap");
        translationBuilder.add(ModItems.VOID_PEARL, "Void Pearl");
        translationBuilder.add("item.strangetools.ender_alloy_upgrade_smithing_template", "Smithing Template");
        translationBuilder.add("itemGroup.strangetools.tools_group", "Strange Tools");
        translationBuilder.add("itemGroup.strangetools.armor_group", "Strange Armor");
        translationBuilder.add("upgrade.strangetools.ender_alloy_upgrade", "Ender Alloy Upgrade");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.applies_to", "Diamond Equipment");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.ingredients", "Ender Alloy Ingot");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.base_slot_description", "Add diamond armor/weapon to be upgraded");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.additions_slot_description", "Add ender alloy ingot");

        generateItemTranslations(translationBuilder, ModItems.TOOL_IDS);
        generateItemTranslations(translationBuilder, ModItems.ARMOR_IDS);
        translationBuilder.add("item.strangetools.apple_upgrade_smithing_template", "Upgrade Smithing Template");
        translationBuilder.add("upgrade.strangetools.apple_upgrade", "Enchanted Golden Apple Upgrade");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.applies_to", "Golden Apple Equipment");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.ingredients", "Enchanted Golden Apple");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.base_slot_description", "Add golden apple armor/weapon to be upgraded");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.additions_slot_description", "Add enchanted golden apple");
    }

 private void generateItemTranslations(TranslationBuilder translationBuilder, List<String> itemIds) {
            for (String itemId : itemIds) {
                String[] parts = itemId.split("_");
                if (parts.length < 2) continue;

                StringBuilder translation = new StringBuilder();
                for (int i = 0; i < parts.length; i++) {
                    if (i > 0) translation.append(" ");
                    translation.append(capitalize(parts[i]));
                }

                translationBuilder.add("item." + Strangetools.MOD_ID + "." + itemId, translation.toString());
            }
        }

        private String capitalize(String str) {
            if (str == null || str.isEmpty()) return str;
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
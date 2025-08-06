package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import com.stools.config.ClothModConfig;
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
        // 物品翻译
        translationBuilder.add(ModItems.TEST_ITEM, "TestItem:)");
        translationBuilder.add(ModItems.ENDER_ALLOY_INGOT, "Ender Alloy Ingot");
        translationBuilder.add(ModItems.RAW_VOID, "Void Ore Crude");
        translationBuilder.add(ModItems.VOID_INGOT, "Void Ingot");
        translationBuilder.add(ModBlocks.ENDER_ORE, "Ender Ore");
        translationBuilder.add(ModBlocks.VOID_ORE, "Void Ore");
        translationBuilder.add(ModItems.ENDER_ALLOY_SCRAP, "Ender Alloy Scrap");
        translationBuilder.add(ModItems.VOID_PEARL, "Void Pearl");
        translationBuilder.add(ModItems.SLICE_OF_CAKE, "A slice of cake");
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
        translationBuilder.add("item.strangetools.apple_upgrade_smithing_template", "Smithing Template");
        translationBuilder.add("upgrade.strangetools.apple_upgrade", "Enchanted Golden Apple Upgrade");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.applies_to", "Golden Apple Equipment");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.ingredients", "Enchanted Golden Apple");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.base_slot_description", "Add golden apple armor/weapon to be upgraded");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.additions_slot_description", "Add enchanted golden apple");

        // 配置界面翻译
        addConfigTranslations(translationBuilder);
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

    private void addConfigTranslations(TranslationBuilder translationBuilder) {
        // ==== Config Titles and Categories ====
        translationBuilder.add("text.autoconfig.strangetools.title", "Strange Tools Configuration");

        // Category Names
        translationBuilder.add("text.autoconfig.strangetools.category.general", "General Settings");
        translationBuilder.add("text.autoconfig.strangetools.category.tool_effects", "Tool Effects");
        translationBuilder.add("text.autoconfig.strangetools.category.armor_effects", "Armor Effects");
        translationBuilder.add("text.autoconfig.strangetools.category.glowstone_effects", "Glowstone Effects");
        translationBuilder.add("text.autoconfig.strangetools.category.glass_effects", "Glass Effects");
        translationBuilder.add("text.autoconfig.strangetools.category.glass_tool", "Glass Tool Settings");

        // ==== General Settings ====
        translationBuilder.add("text.autoconfig.strangetools.option.general.enableDebugMode", "Enable Debug Mode");
        translationBuilder.add("text.autoconfig.strangetools.option.general.enableDebugMode.@Tooltip",
                "Output detailed debug information to console");

        translationBuilder.add("text.autoconfig.strangetools.option.general.enableAllEffects", "Enable All Effects");
        translationBuilder.add("text.autoconfig.strangetools.option.general.enableAllEffects.@Tooltip",
                "Globally enable/disable all mod effects");

        // ==== Tool Effects ====
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolEffects", "Enable Tool Effects");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolEffects.@Tooltip",
                "Master switch for all tool-related effects");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolSkills", "Enable Tool Skills");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolSkills.@Tooltip",
                "Enable special abilities for tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.emeraldDropChance", "Emerald Drop Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.emeraldDropChance.@Tooltip",
                "Probability of dropping emeralds when hitting with emerald tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.copperIgniteChance", "Copper Ignite Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.copperIgniteChance.@Tooltip",
                "Probability of igniting targets with copper tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.quartzExtraDamage", "Quartz Extra Damage");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.quartzExtraDamage.@Tooltip",
                "Amount of additional magic damage dealt by quartz tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.rottenFleshHungerChance", "Rotten Flesh Hunger Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.rottenFleshHungerChance.@Tooltip",
                "Probability of making targets hungry when attacking with rotten flesh tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.endStoneDamageBonus", "End Stone Damage Bonus (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.endStoneDamageBonus.@Tooltip",
                "Percentage of extra damage dealt to End creatures by end stone tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.sweetBerriesPoisonChance", "Sweet Berries Poison Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.sweetBerriesPoisonChance.@Tooltip",
                "Probability of poisoning targets when attacking with sweet berries tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoPoisonChance", "Poisonous Potato Poison Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoPoisonChance.@Tooltip",
                "Probability of poisoning targets when attacking with poisonous potato tools");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoEatPoisonChance", "Poisonous Potato Eat Poison Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoEatPoisonChance.@Tooltip",
                "Probability of poisoning yourself after eating with poisonous potato tools");

        // ==== Armor Effects ====
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.enableArmorEffects", "Enable Armor Effects");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.enableArmorEffects.@Tooltip",
                "Master switch for all armor-related effects");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectChance", "Armor Reflect Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectChance.@Tooltip",
                "Probability of reflecting damage back to attackers");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectDamage", "Armor Reflect Damage");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectDamage.@Tooltip",
                "Amount of damage reflected back to attackers");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperPushChance", "Copper Push Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperPushChance.@Tooltip",
                "Probability of repelling attackers during thunderstorms with copper armor");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperCleanseChance", "Copper Cleanse Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperCleanseChance.@Tooltip",
                "Probability of removing one negative effect from the wearer with copper armor");

        // ==== Glowstone Effects ====
        // ==== Glowstone Effects ====
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.enableEffects", "Enable Glowstone Effects");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.enableEffects.@Tooltip",
                "Toggle for glowstone-related effects");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.damageAmount", "Damage Amount");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.damageAmount.@Tooltip",
                "The amount of damage dealt to surrounding undead mobs when attacking with Glowstone tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.range", "Effect Range");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.range.@Tooltip",
                "The range affecting surrounding undead mobs when using skills with Glowstone tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.glowingDuration", "Glowing Duration (seconds)");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.glowingDuration.@Tooltip",
                "The duration that undead mobs remain glowing when attacked with Glowstone tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.speedDuration", "Speed Duration (seconds)");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.speedDuration.@Tooltip",
                "The duration that undead mobs and the user receive speed effect when using skills with Glowstone tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.durabilityCost", "Durability Cost");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.durabilityCost.@Tooltip",
                "The amount of durability consumed when Glowstone tool effects are triggered");

        // ==== Glass Effects ====
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.enableEffects", "Enable Glass Effects");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.enableEffects.@Tooltip",
                "Toggle for glass-related effects");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectChance", "Shatter Reflect Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectChance.@Tooltip",
                "Probability of reflecting damage when glass tools shatter");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectDamage", "Shatter Reflect Damage");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectDamage.@Tooltip",
                "Amount of damage reflected when glass tools shatter");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterDurabilityCost", "Shatter Durability Cost");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterDurabilityCost.@Tooltip",
                "Amount of durability consumed when glass tools shatter");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDurabilityCost", "Dash Durability Cost");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDurabilityCost.@Tooltip",
                "Amount of durability consumed when dashing with glass tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDuration", "Dash Duration (seconds)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDuration.@Tooltip",
                "Duration of the dash effect with glass tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.cutDoubleDropChance", "Cut Double Drop Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.cutDoubleDropChance.@Tooltip",
                "Probability of getting double drops when breaking blocks with glass tools");

        // ==== Glass Tools ====
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.enableDoubleDrop", "Enable Double Drops");
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.enableDoubleDrop.@Tooltip",
                "Allow glass tools to sometimes drop double items");

        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropChance", "Double Drop Chance (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropChance.@Tooltip",
                "Probability of getting double drops when breaking blocks with glass tools");

        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropDurabilityCost", "Double Drop Durability Cost");
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropDurabilityCost.@Tooltip",
                "Additional durability consumed when triggering double drops with glass tools");
    }
}
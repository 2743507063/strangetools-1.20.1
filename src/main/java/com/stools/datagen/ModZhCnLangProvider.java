package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import com.stools.enchantment.ModEnchantments;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.ItemConvertible;

import java.util.List;

public class ModZhCnLangProvider extends FabricLanguageProvider {
    public ModZhCnLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "zh_cn");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.MACE,"重锤");
        translationBuilder.add(ModEnchantments.DENSITY,"致密");
        translationBuilder.add(ModItems.TEST_ITEM,"神秘东西");
        translationBuilder.add(ModItems.ENDER_ALLOY_INGOT,"末影合金锭");
        translationBuilder.add(ModItems.ENDER_ALLOY_SCRAP,"末影合金碎片");
        translationBuilder.add(ModItems.RAW_VOID,"虚空矿坯");
        translationBuilder.add(ModItems.VOID_INGOT,"虚空锭");
        translationBuilder.add(ModBlocks.ENDER_ORE,"末影矿石");
        translationBuilder.add(ModBlocks.VOID_ORE,"虚空矿石");
        translationBuilder.add(ModItems.VOID_PEARL,"虚空珍珠");
        translationBuilder.add("itemGroup.strangetools.tools_group", "奇奇怪怪的工具");
        translationBuilder.add("itemGroup.strangetools.armor_group", "奇奇怪怪的盔甲");
        translationBuilder.add("item.strangetools.ender_alloy_upgrade_smithing_template", "锻造模板");
        translationBuilder.add("upgrade.strangetools.ender_alloy_upgrade", "末影合金升级");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.applies_to", "钻石装备");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.ingredients", "末影合金锭");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.base_slot_description", "放入待升级的钻石盔甲/武器");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.additions_slot_description", "放入末影合金锭");
        //金苹果
        translationBuilder.add("item.strangetools.apple_upgrade_smithing_template", "锻造模板");
        translationBuilder.add("upgrade.strangetools.apple_upgrade", "附魔金苹果升级");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.applies_to", "金苹果装备");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.ingredients", "附魔金苹果");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.base_slot_description", "放入待升级的金苹果盔甲/武器");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.additions_slot_description", "放入附魔金苹果");
        generateItemTranslations(translationBuilder, ModItems.TOOL_IDS);
        generateItemTranslations(translationBuilder, ModItems.ARMOR_IDS);

    }
    private void generateItemTranslations(TranslationBuilder translationBuilder, List<String> itemIds) {
        for (String itemId : itemIds) {
            int lastIndex = itemId.lastIndexOf('_');
            if (lastIndex == -1) continue;

            String material = itemId.substring(0, lastIndex);
            String type = itemId.substring(lastIndex + 1);

            String materialName = getMaterialName(material);
            String typeName = getTypeName(type);

            if (materialName != null && typeName != null) {
                translationBuilder.add("item." + Strangetools.MOD_ID + "." + itemId, materialName + typeName);
            }
        }
    }

    private String getMaterialName(String material) {
        return switch (material) {
            case "copper" -> "铜";
            case "emerald" -> "绿宝石";
            case "lapis" -> "青金石";
            case "redstone" -> "红石";
            case "quartz" -> "石英";
            case "coal" -> "煤炭";
            case "cake" -> "蛋糕";
            case "obsidian" -> "黑曜石";
            case "prismarine" -> "海晶石";
            case "rotten_flesh" -> "腐肉";
            case "glowstone" -> "萤石";
            case "blaze_powder" -> "烈焰粉";
            case "apple" -> "苹果";
            case "golden_apple" -> "金苹果";
            case "enchanted_golden_apple" -> "附魔金苹果";
            case "bedrock" -> "基岩";
            case "bone" -> "骨头";
            case "nether_star" -> "下界之星";
            case "netherrack" -> "下界岩";
            case "glass" -> "玻璃";
            case "slime" -> "史莱姆";
            case "potion" -> "药水";
            case "string" -> "线";
            case "ender_alloy" -> "末影合金";
            case "end_stone" -> "末地石";
            case "chorus_fruit" -> "紫颂果";
            case "void" -> "虚空";
            default -> null;
        };
    }

    private String getTypeName(String type) {
        return switch (type) {
            case "sword" -> "剑";
            case "pickaxe" -> "镐";
            case "axe" -> "斧";
            case "shovel" -> "锹";
            case "hoe" -> "锄";
            case "helmet" -> "头盔";
            case "chestplate" -> "胸甲";
            case "leggings" -> "护腿";
            case "boots" -> "靴子";
            default -> null;
        };
    }
}
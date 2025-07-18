package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.List;

public class ModZhCnLangProvider extends FabricLanguageProvider {
    public ModZhCnLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "zh_cn");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.MACE,"重锤");
        translationBuilder.add("itemGroup.strangetools.tools_group", "奇奇怪怪的工具");
        translationBuilder.add("itemGroup.strangetools.armor_group", "奇奇怪怪的盔甲");
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
            case "golden_apple" -> "金苹果";
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
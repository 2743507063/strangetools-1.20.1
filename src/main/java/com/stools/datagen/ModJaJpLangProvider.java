package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.List;

public class ModJaJpLangProvider extends FabricLanguageProvider {
    public ModJaJpLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "ja_jp");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.MACE, "重槌（じゅうつい）");
        translationBuilder.add("itemGroup.strangetools.tools_group", "変わったツール");
        translationBuilder.add("itemGroup.strangetools.armor_group", "変わった鎧");
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
            case "copper" -> "銅（どう）";
            case "emerald" -> "エメラルド";
            case "lapis" -> "ラピスラズリ";
            case "redstone" -> "レッドストーン";
            case "quartz" -> "クォーツ";
            case "coal" -> "石炭（せきたん）";
            case "cake" -> "ケーキ";
            case "obsidian" -> "黒曜石（こくようせき）";
            case "prismarine" -> "プリズマリン";
            case "rotten_flesh" -> "腐肉（ふにく）";
            case "glowstone" -> "グローストーン";
            case "blaze_powder" -> "ブレイズの粉";
            case "golden_apple" -> "金のリンゴ";
            case "enchanted_golden_apple" -> "魔法付き金のリンゴ";
            case "bedrock" -> "岩盤（がんばん）";
            case "bone" -> "骨（ほね）";
            case "nether_star" -> "ネザースター";
            case "netherrack" -> "ネザーレック";
            case "glass" -> "ガラス";
            default -> null;
        };
    }

    private String getTypeName(String type) {
        return switch (type) {
            case "sword" -> "剣（けん）";
            case "pickaxe" -> "つるはし";
            case "axe" -> "斧（おの）";
            case "shovel" -> "シャベル";
            case "hoe" -> "くわ";
            case "helmet" -> "兜（かぶと）";
            case "chestplate" -> "胸当て（むなあて）";
            case "leggings" -> "腿甲（ももかぶと）";
            case "boots" -> "靴（くつ）";
            default -> null;
        };
    }
}
    
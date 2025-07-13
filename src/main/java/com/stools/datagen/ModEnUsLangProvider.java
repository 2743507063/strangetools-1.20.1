package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModEnUsLangProvider extends FabricLanguageProvider {
    public ModEnUsLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.strangetools.strangetools_group","Strange Tools");
        for (String toolId : ModItems.TOOL_IDS) {
            String[] parts = toolId.split("_");
            if (parts.length < 2) continue;

            String material = parts[0];
            String type = parts[1];

            String materialName = capitalize(material);
            String typeName = capitalize(type);

            translationBuilder.add("item." + Strangetools.MOD_ID + "." + toolId, materialName + " " + typeName);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
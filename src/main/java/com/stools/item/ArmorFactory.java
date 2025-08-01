package com.stools.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;

public class ArmorFactory {
    public static Item createHelmet(ArmorMaterial material, String id) {
        return new ArmorItem(material, ArmorItem.Type.HELMET, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createChestplate(ArmorMaterial material, String id) {
        return new ArmorItem(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createLeggings(ArmorMaterial material, String id) {
        return new ArmorItem(material, ArmorItem.Type.LEGGINGS, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createBoots(ArmorMaterial material, String id) {
        return new ArmorItem(material, ArmorItem.Type.BOOTS, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }
}
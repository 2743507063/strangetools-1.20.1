package com.stools.item;

import com.stools.item.custom.ModArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ArmorFactory {
    public static Item createHelmet(ArmorMaterial material, String id) {
        return new ModArmorItem(material, ArmorItem.Type.HELMET, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createChestplate(ArmorMaterial material, String id) {
        return new ModArmorItem(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createLeggings(ArmorMaterial material, String id) {
        return new ModArmorItem(material, ArmorItem.Type.LEGGINGS, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createBoots(ArmorMaterial material, String id) {
        return new ModArmorItem(material, ArmorItem.Type.BOOTS, new Item.Settings()) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }
}
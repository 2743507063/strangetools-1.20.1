package com.stools.item;

import com.stools.item.custom.MaceItem;
import com.stools.item.materials.ModMaceMaterials;
import net.minecraft.item.Item;

public class MaceFactory {
    public static Item createMace(ModMaceMaterials material, String id) {
        return new MaceItem(material, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }
}
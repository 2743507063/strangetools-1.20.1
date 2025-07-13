package com.stools.item;

import com.stools.item.custom.*;
import net.minecraft.item.*;

public class ToolFactory {
    public static Item createSword(ToolMaterial material, String id) {
        if (material == ModToolMaterials.PRISMARINE) {
            return new PrismarineSword(material, 3, -2.4F,
                    new Item.Settings().maxDamage(material.getDurability())) {
                @Override
                public String getTranslationKey() {
                    return "item.strangetools." + id;
                }
            };
        }

        return new SwordItem(material, 3, -2.4F,
                new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createPickaxe(ToolMaterial material, String id) {
        if (material == ModToolMaterials.PRISMARINE) {
            return new PrismarinePickaxe(material, 1, -2.8F,
                    new Item.Settings().maxDamage(material.getDurability())) {
                @Override
                public String getTranslationKey() {
                    return "item.strangetools." + id;
                }
            };
        }

        return new PickaxeItem(material, 1, -2.8F,
                new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createAxe(ToolMaterial material, String id) {
        if (material == ModToolMaterials.PRISMARINE) {
            return new PrismarineAxe(material, 5.0F, -3.0F,
                    new Item.Settings().maxDamage(material.getDurability())) {
                @Override
                public String getTranslationKey() {
                    return "item.strangetools." + id;
                }
            };
        }

        return new AxeItem(material, 5.0F, -3.0F,
                new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createShovel(ToolMaterial material, String id) {
        if (material == ModToolMaterials.PRISMARINE) {
            return new PrismarineShovel(material, 1.5F, -3.0F,
                    new Item.Settings().maxDamage(material.getDurability())) {
                @Override
                public String getTranslationKey() {
                    return "item.strangetools." + id;
                }
            };
        }

        return new ShovelItem(material, 1.5F, -3.0F,
                new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }

    public static Item createHoe(ToolMaterial material, String id) {
        if (material == ModToolMaterials.PRISMARINE) {
            return new PrismarineHoe(material, (int) (material.getAttackDamage() - 2), -3.0F,
                    new Item.Settings().maxDamage(material.getDurability())) {
                @Override
                public String getTranslationKey() {
                    return "item.strangetools." + id;
                }
            };
        }

        return new HoeItem(material, (int) (material.getAttackDamage() - 2), -3.0F,
                new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
        };
    }
}
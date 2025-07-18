package com.stools.item;

import com.stools.item.materials.ModToolMaterials;
import net.minecraft.item.*;
import net.minecraft.text.Text;

public class ToolFactory {
    public static Item createSword(ToolMaterial material, String id) {
        return new SwordItem(material, 3, -2.4F, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
            @Override
            public boolean hasGlint(ItemStack stack) {
                if (material instanceof ModToolMaterials modMaterial) {
                    return modMaterial.hasGlint();
                }
                return super.hasGlint(stack);
            }
        };
    }

    public static Item createPickaxe(ToolMaterial material, String id) {
        return new PickaxeItem(material, 1, -2.8F, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
            @Override
            public boolean hasGlint(ItemStack stack) {
                if (material instanceof ModToolMaterials modMaterial) {
                    return modMaterial.hasGlint();
                }
                return super.hasGlint(stack);
            }
        };
    }

    public static Item createAxe(ToolMaterial material, String id) {
        return new AxeItem(material, 5.0F, -3.0F, new Item.Settings().maxDamage(material.getDurability()))  {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
            @Override
            public boolean hasGlint(ItemStack stack) {
                if (material instanceof ModToolMaterials modMaterial) {
                    return modMaterial.hasGlint();
                }
                return super.hasGlint(stack);
            }
        };
    }

    public static Item createShovel(ToolMaterial material, String id) {
        return new ShovelItem(material, 1.5F, -3.0F, new Item.Settings().maxDamage(material.getDurability()))  {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
            @Override
            public boolean hasGlint(ItemStack stack) {
                if (material instanceof ModToolMaterials modMaterial) {
                    return modMaterial.hasGlint();
                }
                return super.hasGlint(stack);
            }
        };
    }
    public static Item createHoe(ToolMaterial material, String id) {
        return new HoeItem(material, (int) (material.getAttackDamage() - 2), -3.0F, new Item.Settings().maxDamage(material.getDurability()))  {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }
            @Override
            public boolean hasGlint(ItemStack stack) {
                if (material instanceof ModToolMaterials modMaterial) {
                    return modMaterial.hasGlint();
                }
                return super.hasGlint(stack);
            }
        };
    }
}
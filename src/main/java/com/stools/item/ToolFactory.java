package com.stools.item;

import com.stools.item.materials.ModToolMaterials;
import net.minecraft.item.*;

public class ToolFactory {
    public interface Glintable {
        boolean hasGlint();
    }
    public static void registerToolSet(ToolMaterial material, String materialName) {
        String prefix = materialName.toLowerCase() + "_";

        // 注册全套工具
        ModItems.registerTool(prefix + "sword", createSword(material, prefix + "sword"));
        ModItems.registerTool(prefix + "pickaxe", createPickaxe(material, prefix + "pickaxe"));
        ModItems.registerTool(prefix + "axe", createAxe(material, prefix + "axe"));
        ModItems.registerTool(prefix + "shovel", createShovel(material, prefix + "shovel"));
        ModItems.registerTool(prefix + "hoe", createHoe(material, prefix + "hoe"));
    }

    // 原版攻击速度参考值：
    // 木剑: -2.4F, 钻石剑: -2.4F
    // 木镐: -2.8F, 钻石镐: -2.8F
    // 木斧: -3.2F, 钻石斧: -3.0F
    // 木铲: -3.0F, 钻石铲: -3.0F
    // 木锄: -3.0F, 钻石锄: -3.0F
    public static Item createSword(ToolMaterial material, String id) {
        boolean hasGlint = material instanceof Glintable glintable && glintable.hasGlint();

        return new SwordItem(material, 3, -2.4F, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }

            @Override
            public boolean hasGlint(ItemStack stack) {
                return hasGlint || super.hasGlint(stack);
            }
        };
    }

    public static Item createPickaxe(ToolMaterial material, String id) {
        boolean hasGlint = material instanceof Glintable glintable && glintable.hasGlint();

        return new PickaxeItem(material, 1, -2.8F, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }

            @Override
            public boolean hasGlint(ItemStack stack) {
                return hasGlint || super.hasGlint(stack);
            }
        };
    }

    public static Item createAxe(ToolMaterial material, String id) {
        boolean hasGlint = material instanceof Glintable glintable && glintable.hasGlint();

        // 根据材料等级调整攻击速度
        float attackSpeed;
        int miningLevel = material.getMiningLevel();

        if (miningLevel >= 3) {
            // 钻石级及以上：基础-3.0F，每超过1级额外+0.1F攻击速度
            attackSpeed = -3.0F + (miningLevel - 3) * 0.1F;
        } else if (miningLevel == 2) { // 铁级
            attackSpeed = -3.1F;
        } else { // 木头/石头级
            attackSpeed = -3.2F;
        }

        return new AxeItem(material, 5.0F, attackSpeed, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }

            @Override
            public boolean hasGlint(ItemStack stack) {
                return hasGlint || super.hasGlint(stack);
            }
        };
    }

    public static Item createShovel(ToolMaterial material, String id) {
        boolean hasGlint = material instanceof Glintable glintable && glintable.hasGlint();

        return new ShovelItem(material, 1.5F, -3.0F, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }

            @Override
            public boolean hasGlint(ItemStack stack) {
                return hasGlint || super.hasGlint(stack);
            }
        };
    }

    public static Item createHoe(ToolMaterial material, String id) {
        boolean hasGlint = material instanceof Glintable glintable && glintable.hasGlint();

        // 防止负攻击伤害
        int attackDamage = (int) material.getAttackDamage();
        int hoeDamage = Math.max(0, attackDamage - 2);

        // 对于泥土工具，直接设置为0
        if (material instanceof ModToolMaterials && material == ModToolMaterials.DIRT) {
            hoeDamage = 0;
        }

        return new HoeItem(material, hoeDamage, -3.0F, new Item.Settings().maxDamage(material.getDurability())) {
            @Override
            public String getTranslationKey() {
                return "item.strangetools." + id;
            }

            @Override
            public boolean hasGlint(ItemStack stack) {
                return hasGlint || super.hasGlint(stack);
            }
        };
    }
}
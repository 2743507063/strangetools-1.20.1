package com.stools.item;

import com.stools.item.custom.LeatherSwordItem;
import com.stools.item.materials.ModToolMaterials;
import net.minecraft.item.*;
import com.stools.item.custom.*;
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

        // 皮革剑特殊处理
        if (material == ModToolMaterials.LEATHER) {
            return new LeatherSwordItem(material, 3, -2.4F, new Item.Settings().maxDamage(material.getDurability())) {
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

        // 其他材料保持原逻辑
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

        // 皮革镐特殊处理
        if (material == ModToolMaterials.LEATHER) {
            return new LeatherPickaxeItem(material, 1, -2.8F, new Item.Settings().maxDamage(material.getDurability())) {
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

        // 其他材料保持原逻辑
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

        // 皮革斧特殊处理
        if (material == ModToolMaterials.LEATHER) {
            return new LeatherAxeItem(material, 5, -3.2F, new Item.Settings().maxDamage(material.getDurability())) {
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

        // 其他材料保持原逻辑（攻击速度计算）
        float attackSpeed;
        int miningLevel = material.getMiningLevel();

        if (miningLevel >= 3) {
            attackSpeed = -3.0F + (miningLevel - 3) * 0.1F;
        } else if (miningLevel == 2) {
            attackSpeed = -3.1F;
        } else {
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

        // 皮革铲特殊处理
        if (material == ModToolMaterials.LEATHER) {
            return new LeatherShovelItem(material, 1.5F, -3.0F, new Item.Settings().maxDamage(material.getDurability())) {
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

        // 其他材料保持原逻辑
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

        // 皮革锄特殊处理
        if (material == ModToolMaterials.LEATHER) {
            return new LeatherHoeItem(material, 0, -3.0F, new Item.Settings().maxDamage(material.getDurability())) {
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

        // 其他材料保持原逻辑
        int attackDamage = (int) material.getAttackDamage();
        int hoeDamage = Math.max(0, attackDamage - 2);

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
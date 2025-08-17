package com.stools.item.materials;

import com.stools.datagen.ModItemTagsProvider;
import com.stools.item.ModItems;
import com.stools.item.ToolFactory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.Random;
import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial, ToolFactory.Glintable {
    COPPER(2, 500, 5.0f, 1.5f, 15, () -> Ingredient.ofItems(Items.COPPER_INGOT),false),
    EMERALD(3, 2000, 8.0f, 3.0f, 25, () -> Ingredient.ofItems(Items.EMERALD),false),
    LAPIS(2, 600, 6.0f, 1.5f, 35, () -> Ingredient.ofItems(Items.LAPIS_LAZULI),false),
    REDSTONE(2, 300, 12.0f, 1.0f, 10, () -> Ingredient.ofItems(Items.REDSTONE),false),
    QUARTZ(2, 550, 6.5f, 1.5f, 20, () -> Ingredient.ofItems(Items.QUARTZ),false),
    COAL(1, 250, 4.0f, 1.5f, 10, () -> Ingredient.ofItems(Items.COAL),false),
    CAKE(1, 250, 3.0f, 1.0f, 10, () -> Ingredient.ofItems(Items.CAKE),false),
    OBSIDIAN(3, 2000, 7.0f, 3.25f, 5, () -> Ingredient.ofItems(Items.OBSIDIAN),false),
    PRISMARINE(2, 340, 6.5f, 1.5f, 15, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD),false),
    ROTTEN_FLESH(1, 180, 3.5f, 0.8f, 8, () -> Ingredient.ofItems(Items.ROTTEN_FLESH),false),
    GLOWSTONE(2, 600, 7.0f, 2.0f, 30, () -> Ingredient.ofItems(Items.GLOWSTONE_DUST),false),
    BLAZE_POWDER(3, 600, 7.5f, 1.5f, 15, () -> Ingredient.ofItems(Items.BLAZE_POWDER),false),
    GOLDEN_APPLE(2, 1000, 7.0F, 2.0F, 25, () -> Ingredient.ofItems(Items.GOLDEN_APPLE),false),
    APPLE(1, 500, 5.0F, 1.0F, 10, () -> Ingredient.ofItems(Items.APPLE),false),
    ENCHANTED_GOLDEN_APPLE(3, 2000, 9.0F, 3.5F, 35, () -> Ingredient.ofItems(Items.ENCHANTED_GOLDEN_APPLE), true),
    BEDROCK(7, 5000, 12.0f, 12.0f, 50, () -> Ingredient.ofItems(Items.BEDROCK), false),
    BONE(2, 250, 5.5f, 2.5f, 15, () -> Ingredient.ofItems(Items.BONE), false),
    NETHERRACK(2, 350, 5.5f, 2.0f, 10, () -> Ingredient.ofItems(Items.NETHERRACK), false),
    NETHER_STAR(5, 2200, 10.0f, 5.0f, 30, () -> Ingredient.ofItems(Items.NETHER_STAR), true),
    GLASS(2, 100, 7.0f, 1.0f, 25, () -> Ingredient.ofItems(Items.GLASS), false),
    SLIME(1, 120, 7.0f, 1.0f, 25, () -> Ingredient.ofItems(Items.SLIME_BALL), false),
    POTION(2, 650, 6.0f, 3.0f, 30, () -> Ingredient.ofItems(Items.GLASS_BOTTLE), true) {
        // 随机浮动伤害 (3.0-5.0)
        @Override
        public float getAttackDamage() {
            return 3.0f + (new Random().nextFloat() * 2.0f);
        }
    },
    STRING(1, 250, 6.0f, 1.0f, 30, () -> Ingredient.ofItems(Items.STRING), false),
    ENDER_ALLOY(4, 2000, 9.0f, 4.5f, 35, () -> Ingredient.ofItems(ModItems.ENDER_ALLOY_INGOT), false),
    END_STONE(2, 400, 6.0f, 2.0f, 15, () -> Ingredient.ofItems(Items.END_STONE), false),
    CHORUS_FRUIT(2, 500, 6.0f, 2.5f, 20, () -> Ingredient.ofItems(Items.CHORUS_FRUIT), false) {
        @Override
        public float getAttackDamage() {
            return 2.5f; // 基础攻击伤害
        }
    },

    VOID(4, 1800, 8.0f, 3.5f, 18, () -> Ingredient.ofItems(ModItems.VOID_INGOT), false) {
    },
    DIRT(0, 15, 1.0f, 0.0f, 0, () -> Ingredient.fromTag(ModItemTagsProvider.DIRT_TAG), false) {
        @Override
        public float getAttackDamage() {
            return 0.0f; // 明确设置为0
        }
    },
    WATERMELON(1, 300, 4.5f, 1.5f, 0, () -> Ingredient.ofItems(Items.MELON_SLICE), false),
    SWEET_BERRIES(0, 150, 4.0f, 1.2f, 12, () -> Ingredient.ofItems(Items.SWEET_BERRIES), false),
    GLOW_BERRIES(0, 160, 4.2f, 1.3f, 15, () -> Ingredient.ofItems(Items.GLOW_BERRIES), false),
    CARROT(1, 320, 4.5f, 2.0f, 9, () -> Ingredient.ofItems(Items.CARROT), false),
    GOLDEN_CARROT(2, 500, 6.0f, 2.5f, 20, () -> Ingredient.ofItems(Items.CARROT), false),
    POTATO(0, 250, 3.5f, 1.8f, 5, () -> Ingredient.ofItems(Items.POTATO), false),
    POISONOUS_POTATO(1, 450, 6.0f, 2.2f, 18, () -> Ingredient.ofItems(Items.POISONOUS_POTATO), false),
    AMETHYST(2, 850, 7.5f, 3.5f, 35, () -> Ingredient.ofItems(ModItems.AMETHYST_INGOT), false),
    FLINT(1, 50, 3.5f, 0.5f, 1, () -> Ingredient.ofItems(Items.FLINT), false),
    WHEAT(1, 430, 4.5f, 0.5f, 4, () -> Ingredient.ofItems(Items.WHEAT), false),
    BEETROOT(1, 386, 4.0f, 2.5f, 9, () -> Ingredient.ofItems(Items.BEETROOT), false),
    DRIED_KELP(0, 20, 1.0f, 0.1f, 1, () -> Ingredient.ofItems(Items.DRIED_KELP), false),
    BLUE_ICE(1, 180, 4.5f, 1.8f, 18, () -> Ingredient.ofItems(Items.BLUE_ICE), false),
    RAW_BEEF(0, 150, 3.0f, 1.5f, 5, () -> Ingredient.ofItems(Items.BEEF), false);


    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;
    private final boolean hasGlint;
    ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient,boolean hasGlint) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
        this.hasGlint = hasGlint;
    }
    public Item getRepairItem() {
        return getRepairIngredient().getMatchingStacks()[0].getItem();
    }
    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    public boolean hasGlint() {
        return hasGlint;
    }
    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}

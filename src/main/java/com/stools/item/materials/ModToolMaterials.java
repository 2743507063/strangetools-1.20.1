package com.stools.item.materials;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    COPPER(2, 500, 5.0f, 1.5f, 15, () -> Ingredient.ofItems(Items.COPPER_INGOT)),
    EMERALD(3, 2000, 8.0f, 3.0f, 25, () -> Ingredient.ofItems(Items.EMERALD)),
    LAPIS(2, 600, 6.0f, 1.5f, 35, () -> Ingredient.ofItems(Items.LAPIS_LAZULI)),
    REDSTONE(2, 300, 12.0f, 1.0f, 10, () -> Ingredient.ofItems(Items.REDSTONE)),
    QUARTZ(2, 550, 6.5f, 2.5f, 20, () -> Ingredient.ofItems(Items.QUARTZ)),
    COAL(1, 250, 4.0f, 1.5f, 10, () -> Ingredient.ofItems(Items.COAL)),
    CAKE(1, 250, 3.0f, 1.0f, 10, () -> Ingredient.ofItems(Items.CAKE)),
    OBSIDIAN(3, 1800, 7.0f, 4.0f, 5, () -> Ingredient.ofItems(Items.OBSIDIAN)),
    PRISMARINE(2, 340, 6.5f, 1.5f, 15, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)),
    ROTTEN_FLESH(1, 180, 3.5f, 0.8f, 8, () -> Ingredient.ofItems(Items.ROTTEN_FLESH)),
    GLOWSTONE(2, 600, 7.0f, 2.0f, 30, () -> Ingredient.ofItems(Items.GLOWSTONE_DUST)),
    BLAZE_POWDER(3, 600, 7.5f, 1.5f, 15, () -> Ingredient.ofItems(Items.BLAZE_POWDER)),
    GOLDEN_APPLE(2, 1000, 7.0F, 3.0F, 25, () -> Ingredient.ofItems(Items.GOLDEN_APPLE));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
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

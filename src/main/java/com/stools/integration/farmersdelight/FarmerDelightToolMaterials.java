package com.stools.integration.farmersdelight;

import com.stools.integration.FarmerDelightIntegration;
import com.stools.item.ToolFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import java.util.function.Supplier;

public enum FarmerDelightToolMaterials implements ToolMaterial, ToolFactory.Glintable {;

    private final int miningLevel;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;
    private final boolean hasGlint;

    FarmerDelightToolMaterials(int miningLevel, int durability, float miningSpeed,
                               float attackDamage, int enchantability,
                               Supplier<Ingredient> repairIngredient, boolean hasGlint) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
        this.hasGlint = hasGlint;
    }

    @Override
    public int getDurability() {
        return this.durability;
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

    @Override
    public boolean hasGlint() {
        return hasGlint;
    }
}
package com.stools.item.materials;

import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import java.util.function.Supplier;

public enum ModMaceMaterials {
    WOOD(2, 59, 15, () -> Ingredient.ofItems(Items.OAK_PLANKS)),
    STONE(3, 131, 5, () -> Ingredient.ofItems(Items.COBBLESTONE)),
    IRON(4, 250, 14, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    DIAMOND(5, 1561, 10, () -> Ingredient.ofItems(Items.DIAMOND)),
    NETHERITE(6, 2031, 15, () -> Ingredient.ofItems(Items.NETHERITE_INGOT));

    private final int baseDamage;
    private final int durability;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModMaceMaterials(int baseDamage, int durability, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.baseDamage = baseDamage;
        this.durability = durability;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getDurability() {
        return durability;
    }

    public int getEnchantability() {
        return enchantability;
    }

    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
// ModArmorMaterials.java
package com.stools.item.materials;

import com.stools.Strangetools;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials implements StringIdentifiable, ArmorMaterial {
    EMERALD("emerald", 40, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 4);
        map.put(ArmorItem.Type.LEGGINGS, 7);
        map.put(ArmorItem.Type.CHESTPLATE, 9);
        map.put(ArmorItem.Type.HELMET, 4);
    }), 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            1.0F, 0.0F, () -> Ingredient.ofItems(Items.EMERALD), 0.3f),
    LAPIS("lapis", 30, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);     // 靴子护甲值
        map.put(ArmorItem.Type.LEGGINGS, 6);  // 护腿护甲值
        map.put(ArmorItem.Type.CHESTPLATE, 8);// 胸甲护甲值
        map.put(ArmorItem.Type.HELMET, 3);    // 头盔护甲值
    }), 30, SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F, 0.0F, () -> Ingredient.ofItems(Items.LAPIS_LAZULI), 0.25f),
    COPPER("copper", 20, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 3);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 1);
    }), 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F, 0.0F, () -> Ingredient.ofItems(Items.COPPER_INGOT), 0.1f),
    REDSTONE("redstone", 15, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 3);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 1);
    }), 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F, 0.0F, () -> Ingredient.ofItems(Items.REDSTONE), 0.08f),
    COAL("coal", 12, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 2);
        map.put(ArmorItem.Type.CHESTPLATE, 3);
        map.put(ArmorItem.Type.HELMET, 1);
    }), 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0F, 0.0F, () -> Ingredient.ofItems(Items.COAL), 0.15f),
    OBSIDIAN("obsidian", 35, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
    }), 12, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            3.0F, 0.1F, () -> Ingredient.ofItems(Items.OBSIDIAN), 0.4f);
    private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY = Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredientSupplier;
    private final float effectPower;

    ModArmorMaterials(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionAmounts,
                      int enchantability, SoundEvent equipSound, float toughness,
                      float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier,
                      float effectPower) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = repairIngredientSupplier;
        this.effectPower = effectPower;
    }

    public float getEffectPower() {
        return effectPower;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return (Integer)BASE_DURABILITY.get(type) * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return (Integer)this.protectionAmounts.get(type);
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    @Override
    public String getName() {
        return Strangetools.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
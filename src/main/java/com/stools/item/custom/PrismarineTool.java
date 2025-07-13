// PrismarineTool.java
package com.stools.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;

public abstract class PrismarineTool extends MiningToolItem {
    private final float attackDamage;
    private final float attackSpeed;

    public PrismarineTool(ToolMaterial material, float attackDamage, float attackSpeed,
                          TagKey<net.minecraft.block.Block> effectiveBlocks,
                          Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float originalSpeed = super.getMiningSpeedMultiplier(stack, state);

        // 检查玩家是否在水下
        if (stack.getHolder() instanceof PlayerEntity player &&
                player.isSubmergedInWater()) {
            return originalSpeed * 2.0f; // 水下挖掘速度加倍
        }
        return originalSpeed;
    }

    // 添加这个方法以便在工厂中使用
    public float getAttackDamage() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }
}
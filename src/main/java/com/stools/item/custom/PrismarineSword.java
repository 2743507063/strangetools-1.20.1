package com.stools.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class PrismarineSword extends SwordItem {
    public PrismarineSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 攻击海洋生物时附加减速效果
        if (target instanceof WaterCreatureEntity) {
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    100,  // 5秒持续时间 (20 ticks/秒 * 5)
                    1     // 等级I
            ));
        }
        return super.postHit(stack, target, attacker);
    }
}
package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;

public class SlimeToolUseEvent {
    private static final int DURABILITY_COST = 40;
    private static final int EFFECT_DURATION = 8 * 20; // 8秒（tick为单位）
    private static final double FORWARD_DISTANCE = 4.0; // 弹射距离

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem tool
                    && tool.getMaterial() == ModToolMaterials.SLIME) {
                if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                    return TypedActionResult.fail(stack);
                }
                if (!world.isClient()) {
                    // 消耗耐久
                    stack.damage(DURABILITY_COST, player, p -> p.sendToolBreakStatus(hand));
                    // 效果
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, EFFECT_DURATION, 0));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, EFFECT_DURATION, 1));

                    // 方向推进
                    Vec3d look = player.getRotationVec(1.0f).normalize();
                    // 水平推进4格，有轻微向上抬升
                    player.addVelocity(look.x * FORWARD_DISTANCE, 0.4, look.z * FORWARD_DISTANCE);
                    player.velocityModified = true;

                    // 声音
                    world.playSound(null, player.getBlockPos(),
                            SoundEvents.ENTITY_PARROT_FLY,
                            SoundCategory.PLAYERS, 1.2f, 1.2f);
                }
                return TypedActionResult.success(stack);
            }

            return TypedActionResult.pass(stack);
        });
    }
}
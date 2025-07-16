package com.stools.event;

import com.stools.item.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireChargeToolUseEvent {

    // 每次使用技能消耗的耐久度
    private static final int DURABILITY_COST_SKILL1 = 15; // 消耗15点耐久

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.FIRE_CHARGE) {
                    if (!world.isClient()) {
                        // 检查工具是否有足够耐久度
                        if (stack.getDamage()  + DURABILITY_COST_SKILL1 >= stack.getMaxDamage()) {
                            return TypedActionResult.fail(stack);
                        }

                        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(5), e -> e != player)) {
                            entity.setOnFireFor(5);
                            entity.damage(player.getDamageSources().playerAttack(player), 5.0f);
                        }
                        stack.damage(DURABILITY_COST_SKILL1, player, p -> p.sendToolBreakStatus(hand));

                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ITEM_FIRECHARGE_USE,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }
}
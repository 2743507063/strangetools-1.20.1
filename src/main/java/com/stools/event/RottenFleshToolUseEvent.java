package com.stools.event;

import com.stools.item.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;

public class RottenFleshToolUseEvent {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.ROTTEN_FLESH) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    int remainingDurability = maxDamage - currentDamage;
                    int consumeAmount = Math.min(remainingDurability, 50);

                    if (!world.isClient()) {
                        stack.damage(consumeAmount, player, e -> e.sendToolBreakStatus(hand));

                        // 恢复饥饿值和饱和度
                        player.getHungerManager().add(4, 0.8F);

                        // 播放食用音效
                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_GENERIC_EAT,
                                SoundCategory.PLAYERS, 1.0F, 0.8F); // 音调稍低

                        // 30% 几率食物中毒（给予饥饿效果）
                        Random random = world.getRandom();
                        if (random.nextFloat() < 0.3F) {
                            // 饥饿 II 效果，持续10秒（200 ticks）
                            player.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.HUNGER,
                                    200, // 持续时间（tick）
                                    1    // 效果等级（II级）
                            ));

                            // 播放中毒音效
                            world.playSound(null, player.getBlockPos(),
                                    SoundEvents.ENTITY_PLAYER_BURP,
                                    SoundCategory.PLAYERS, 1.0F, 0.5F);
                        }

                        // 耐久耗尽时变成木棍
                        if (stack.getDamage() >= stack.getMaxDamage()) {
                            ItemStack stickStack = new ItemStack(Items.STICK);
                            if (!player.getInventory().insertStack(stickStack)) {
                                player.dropItem(stickStack, false);
                            }
                        }
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }
}
package com.stools.event;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GoldenAppleToolUseEvent {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.GOLDEN_APPLE) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    int remainingDurability = maxDamage - currentDamage;
                    int consumeAmount = Math.min(remainingDurability, 250);

                    if (!world.isClient()) {
                        stack.damage(consumeAmount, player, e -> e.sendToolBreakStatus(hand));

                        HungerManager hungerManager = player.getHungerManager();
                        hungerManager.add(4, 4.8f);

                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600 / 2, 1));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200 / 2, 0));

                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_GENERIC_EAT,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);

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
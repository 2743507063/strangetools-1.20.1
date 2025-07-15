package com.stools.event;

import com.stools.item.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;

public class CakeToolUseEvent {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.CAKE) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    int remainingDurability = maxDamage - currentDamage;
                    int consumeAmount = Math.min(remainingDurability, 50);

                    if (!world.isClient()) {
                        stack.damage(consumeAmount, player, e -> e.sendToolBreakStatus(hand));

                        player.getHungerManager().add(2, 0.4F);

                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_GENERIC_EAT,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);

                        if (stack.getDamage() >= stack.getMaxDamage()) {
                            }
                           ItemStack stickStack = new ItemStack(Items.STICK);
                            if (!player.getInventory().insertStack(stickStack)) {
                                player.dropItem(stickStack, false);
                            }
                        }
                    }

                    return TypedActionResult.success(stack);
                }
            return TypedActionResult.pass(stack);
        });
    }
}
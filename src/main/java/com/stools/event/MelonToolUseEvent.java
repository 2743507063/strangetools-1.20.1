package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.ModItems;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;

public class MelonToolUseEvent {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.WATERMELON) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    int remainingDurability = maxDamage - currentDamage;
                    int consumeAmount = Math.min(remainingDurability, 60);

                    if (!world.isClient()) {
                        stack.damage(consumeAmount, player, e -> e.sendToolBreakStatus(hand));

                        player.getHungerManager().add(2, 1.2F);

                        net.minecraft.util.math.random.Random random = world.getRandom();
                        int chanceCount = consumeAmount / 10; // 每10点耐久触发一次判定

                        for (int i = 0; i < chanceCount; i++) {
                            if (random.nextFloat() < 0.05f) { // 5%概率
                                ItemStack cakeSlice = new ItemStack(Items.MELON_SLICE);
                                if (!player.getInventory().insertStack(cakeSlice)) {
                                    player.dropItem(cakeSlice, false);
                                }
                            }
                        }

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
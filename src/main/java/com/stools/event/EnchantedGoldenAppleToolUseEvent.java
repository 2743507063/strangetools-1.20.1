package com.stools.event;

import com.stools.config.ModConfigManager;
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

public class EnchantedGoldenAppleToolUseEvent {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.ENCHANTED_GOLDEN_APPLE) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    int remainingDurability = maxDamage - currentDamage;
                    int consumeAmount = Math.min(remainingDurability, 200);

                    if (!world.isClient()) {
                        stack.damage(consumeAmount, player, e -> e.sendToolBreakStatus(hand));

                        HungerManager hungerManager = player.getHungerManager();
                        hungerManager.add(8, 12.8f);

                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600 / 2, 4)); // 持续5秒，等级5
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000 / 2, 0));  // 持续30秒
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000 / 2, 0));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200 / 2, 3)); // 持续10秒，等级4

                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 1));

                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_GENERIC_EAT,
                                SoundCategory.PLAYERS, 1.0F, 1.2F);

                        if (stack.getDamage() >= stack.getMaxDamage()) {
                            ItemStack enchantedCore = new ItemStack(Items.STICK, 1);
                            if (!player.getInventory().insertStack(enchantedCore)) {
                                player.dropItem(enchantedCore, false);
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
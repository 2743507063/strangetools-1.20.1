package com.stools.event.glass;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;

public class GlassToolUseEvent {
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!(player.isSneaking() && stack.getItem() instanceof ToolItem tool && tool.getMaterial() == ModToolMaterials.GLASS)) {
                return TypedActionResult.pass(stack);
            }
            if (!player.isOnGround()) return TypedActionResult.pass(stack);
            if (stack.getDamage() + 30 >= stack.getMaxDamage()) return TypedActionResult.fail(stack);

            if (!world.isClient()) {
                stack.damage(30, player, p -> p.sendToolBreakStatus(hand));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 100, 0)); // 5秒
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1)); // 5秒，速度2
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.2F);
            }
            return TypedActionResult.success(stack);
        });
    }
}
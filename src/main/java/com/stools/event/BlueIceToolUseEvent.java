package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BlueIceToolUseEvent {

    private static final int DURABILITY_COST = 30;
    private static final int SHIELD_DURATION = 8 * 20; // 8秒

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.BLUE_ICE) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        // 消耗耐久
                        stack.damage(DURABILITY_COST, player, p -> p.sendToolBreakStatus(hand));
                        
                        // 给予抗性提升
                        player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.RESISTANCE, 
                            SHIELD_DURATION, 
                            0 // 等级I（20%减伤）
                        ));

                        
                        // 粒子效果
                        if (world instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE,
                                player.getX(), player.getY() + 1, player.getZ(),
                                50, 1, 1, 1, 0.2);
                        }
                        
                        // 音效
                        world.playSound(null, player.getBlockPos(),
                            SoundEvents.BLOCK_GLASS_BREAK,
                            SoundCategory.PLAYERS, 1.0f, 0.8f);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }
}
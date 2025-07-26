package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

import java.util.Random;

public class VoidToolMiningEvent {
    public static void register() {
        // 监听实体攻击事件
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills)
                return ActionResult.PASS;

            ItemStack tool = player.getStackInHand(hand);
            if (tool.getItem() instanceof ToolItem toolItem &&
                    toolItem.getMaterial() == ModToolMaterials.VOID) {

                Random rand = new Random();
                if (rand.nextFloat() < 0.2f) {
                    // 回复1点耐久
                    tool.setDamage(Math.max(0, tool.getDamage() - 1));

                    // 粒子效果
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                                entity.getX(), entity.getY() + 1, entity.getZ(),
                                5, 0.3, 0.3, 0.3, 0.02);
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
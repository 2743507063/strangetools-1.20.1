package com.stools.event.glass;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GlassToolBreakEvent {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            if (!(stack.getItem() instanceof ToolItem tool && tool.getMaterial() == ModToolMaterials.GLASS)) return;
            if (!(world instanceof ServerWorld serverWorld)) return;
            if (player.getRandom().nextFloat() >= 0.1F) return; // 10%概率

            // 掉落一次原本的掉落物（双倍）
            state.getBlock().afterBreak(serverWorld, player, pos, state, blockEntity, stack);
            // 损失2点耐久（或2倍，按你需求）
            stack.damage(2, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
        });
    }
}
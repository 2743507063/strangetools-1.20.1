package com.stools.event;

import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import com.stools.item.materials.ModToolMaterials;

import java.util.Random;

public class ChorusFruitToolUseEvent {

    private static final Random random = new Random();
    private static final int DURABILITY_COST = 100;

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }

            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.CHORUS_FRUIT) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();

                    // 检查耐久是否足够
                    if (currentDamage + DURABILITY_COST > maxDamage) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        // 消耗耐久
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));

                        // 恢复饥饿值和饱和度
                        player.getHungerManager().add(4, 2.4f);

                        // 随机传送
                        teleportPlayer(player, (ServerWorld) world);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void teleportPlayer(PlayerEntity player, ServerWorld world) {
        // 尝试寻找安全位置
        BlockPos playerPos = player.getBlockPos();
        Vec3d teleportPos = null;

        // 最多尝试10次寻找安全位置
        for (int i = 0; i < 10; i++) {
            // 在8格范围内随机选择位置
            double x = playerPos.getX() + (random.nextDouble() - 0.5) * 16;
            double z = playerPos.getZ() + (random.nextDouble() - 0.5) * 16;
            double y = playerPos.getY() + random.nextInt(5) - 2;

            BlockPos testPos = BlockPos.ofFloored(x, y, z);

            // 检查位置是否安全
            if (isSafePosition(world, testPos)) {
                teleportPos = new Vec3d(x, y, z);
                break;
            }
        }

        // 如果没找到安全位置，使用玩家当前位置
        if (teleportPos == null) {
            teleportPos = player.getPos();
        }

        // 传送玩家
        player.teleport(teleportPos.x, teleportPos.y, teleportPos.z);

        // 播放效果
        world.playSound(null, player.getBlockPos(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS, 1.0f, 1.0f);

        world.spawnParticles(ParticleTypes.PORTAL,
                player.getX(), player.getY() + 1, player.getZ(),
                30, 0.5, 0.5, 0.5, 0.1);
    }

    private static boolean isSafePosition(ServerWorld world, BlockPos pos) {
        BlockPos downPos = pos.down();

        // 检查目标位置下方是否有固体方块
        if (!world.getBlockState(downPos).isSolidBlock(world, downPos)) {
            return false;
        }

        // 检查目标位置是否可站立（非固体方块）
        if (world.getBlockState(pos).isSolidBlock(world, pos)) {
            return false;
        }

        // 检查目标位置上方是否有空间（非固体方块）
        if (world.getBlockState(pos.up()).isSolidBlock(world, pos.up())) {
            return false;
        }

        // 检查位置是否安全（非液体）
        return world.getFluidState(pos).isEmpty() &&
                world.getFluidState(pos.up()).isEmpty();
    }
}
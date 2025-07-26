package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VoidToolUseEvent {
    private static final int DURABILITY_COST = 80;
    private static final Map<BlockPos, Long> activeRifts = new HashMap<>();

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills)
                return TypedActionResult.pass(stack);

            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem &&
                    toolItem.getMaterial() == ModToolMaterials.VOID) {

                if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage())
                    return TypedActionResult.fail(stack);

                if (!world.isClient()) {
                    // 更精确的位置计算
                    Vec3d lookVec = player.getRotationVec(1.0F);
                    Vec3d targetPosVec = player.getPos().add(lookVec.multiply(4));
                    BlockPos targetPos = BlockPos.ofFloored(targetPosVec);

                    // 创建黑洞
                    createVoidRift((ServerWorld) world, targetPos);

                    // 消耗耐久
                    stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));

                }
                return TypedActionResult.success(stack);
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void createVoidRift(ServerWorld world, BlockPos pos) {
        activeRifts.put(pos, world.getTime() + 100); // 5秒持续时间

        // 音效
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE,
                SoundCategory.PLAYERS, 1.0f, 0.5f);

        // 粒子效果
        world.spawnParticles(ParticleTypes.PORTAL,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                100, 1.5, 1.5, 1.5, 0.2);
    }

    public static void updateRifts(ServerWorld world) {
        long currentTime = world.getTime();
        Iterator<Map.Entry<BlockPos, Long>> iterator = activeRifts.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Long> entry = iterator.next();
            BlockPos pos = entry.getKey();

            if (currentTime > entry.getValue()) {
                // 裂隙结束时的效果
                world.spawnParticles(ParticleTypes.SMOKE,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        30, 0.5, 0.5, 0.5, 0.1);
                iterator.remove(); // 移除过期裂隙
                continue;
            }

            // 更合理的范围检测
            Box area = new Box(
                    pos.getX() - 3, pos.getY() - 1, pos.getZ() - 3,
                    pos.getX() + 3, pos.getY() + 2, pos.getZ() + 3
            );

            List<LivingEntity> entities = world.getEntitiesByClass(
                    LivingEntity.class, area, e -> true
            );

            for (LivingEntity entity : entities) {
                // 拉扯效果
                Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                Vec3d direction = center.subtract(entity.getPos()).normalize();

                // 更平滑的拉扯效果
                double distance = entity.getPos().distanceTo(center);
                double strength = 0.2 * (1.0 - Math.min(1.0, distance / 3.0));

                entity.addVelocity(
                        direction.x * strength,
                        direction.y * strength * 0.5,
                        direction.z * strength
                );

                // 每秒伤害1次 + 粒子效果
                if (currentTime % 20 == 0) {
                    entity.damage(world.getDamageSources().magic(), 1.0f);

                    // 伤害指示粒子
                    world.spawnParticles(ParticleTypes.DAMAGE_INDICATOR,
                            entity.getX(), entity.getY() + 1, entity.getZ(),
                            3, 0.3, 0.3, 0.3, 0.02);
                }

                // 持续粒子效果
                if (currentTime % 5 == 0) {
                    world.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
                            1, 0.1, 0.1, 0.1, 0.01);
                }
            }

            // 中心持续粒子效果
            if (currentTime % 2 == 0) {
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        5, 0.3, 0.3, 0.3, 0.05);
            }
        }
    }
}
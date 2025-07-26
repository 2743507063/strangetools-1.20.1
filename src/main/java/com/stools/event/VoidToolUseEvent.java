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
    // 添加音效常量
    private static final float ACTIVATE_PITCH = 0.5f;
    private static final float DEACTIVATE_PITCH = 0.8f;

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
                    Vec3d lookVec = player.getRotationVec(1.0F);
                    Vec3d targetPosVec = player.getPos().add(lookVec.multiply(4));
                    BlockPos targetPos = BlockPos.ofFloored(targetPosVec);

                    // 添加技能释放音效
                    world.playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                            SoundCategory.PLAYERS,
                            1.0f,
                            ACTIVATE_PITCH
                    );

                    createVoidRift((ServerWorld) world, targetPos);
                    stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));
                }
                return TypedActionResult.success(stack);
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void createVoidRift(ServerWorld world, BlockPos pos) {
        activeRifts.put(pos, world.getTime() + 100); // 5秒持续时间

        //音效
        world.playSound(
                null,
                pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.BLOCK_END_PORTAL_SPAWN,
                SoundCategory.PLAYERS,
                1.5f,
                ACTIVATE_PITCH
        );

        //粒子效果
        world.spawnParticles(
                ParticleTypes.REVERSE_PORTAL,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                150,  // 粒子数量
                2.0, 2.0, 2.0,  // 范围
                0.3   // 速度
        );

        // 额外粒子效果
        world.spawnParticles(
                ParticleTypes.ELECTRIC_SPARK,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                30,
                1.0, 1.0, 1.0,
                0.1
        );
    }

    public static void updateRifts(ServerWorld world) {
        long currentTime = world.getTime();
        Iterator<Map.Entry<BlockPos, Long>> iterator = activeRifts.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Long> entry = iterator.next();
            BlockPos pos = entry.getKey();

            if (currentTime > entry.getValue()) {
                // 裂隙结束时的效果
                world.spawnParticles(
                        ParticleTypes.EXPLOSION,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        15, 0.5, 0.5, 0.5, 0.2
                );

                world.spawnParticles(
                        ParticleTypes.POOF,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        50, 1.0, 1.0, 1.0, 0.1
                );

                // 添加结束音效
                world.playSound(
                        null,
                        pos.getX(), pos.getY(), pos.getZ(),
                        SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
                        SoundCategory.PLAYERS,
                        1.2f,
                        DEACTIVATE_PITCH
                );

                iterator.remove();
                continue;
            }

            Box area = new Box(
                    pos.getX() - 3, pos.getY() - 1, pos.getZ() - 3,
                    pos.getX() + 3, pos.getY() + 2, pos.getZ() + 3
            );

            List<LivingEntity> entities = world.getEntitiesByClass(
                    LivingEntity.class, area, e -> true
            );

            for (LivingEntity entity : entities) {
                Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                Vec3d direction = center.subtract(entity.getPos()).normalize();

                double distance = entity.getPos().distanceTo(center);
                double strength = 0.2 * (1.0 - Math.min(1.0, distance / 3.0));

                entity.addVelocity(
                        direction.x * strength,
                        direction.y * strength * 0.5,
                        direction.z * strength
                );

                // 提高伤害值
                if (currentTime % 20 == 0) {
                    entity.damage(world.getDamageSources().magic(), 3.0f);

                    // 粒子效果
                    world.spawnParticles(
                            ParticleTypes.SMOKE,
                            entity.getX(), entity.getY() + 1, entity.getZ(),
                            5, 0.3, 0.3, 0.3, 0.05
                    );
                }

                // 拉扯粒子效果
                if (currentTime % 3 == 0) {
                    world.spawnParticles(
                            ParticleTypes.ASH,
                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
                            2, 0.15, 0.15, 0.15, 0.02
                    );

                    // 添加从实体到中心的粒子流
                    Vec3d particlePos = entity.getPos().add(
                            (center.x - entity.getX()) * 0.3,
                            (center.y - entity.getY()) * 0.3 + 0.5,
                            (center.z - entity.getZ()) * 0.3
                    );

                    world.spawnParticles(
                            ParticleTypes.SNEEZE,
                            particlePos.x, particlePos.y, particlePos.z,
                            1, 0.0, 0.0, 0.0, 0.1
                    );
                }
            }

            // 中心粒子效果
            if (currentTime % 2 == 0) {
                world.spawnParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        8, 0.4, 0.4, 0.4, 0.1
                );

                // 旋转粒子效果
                double angle = (currentTime % 100) * 0.0628; // 约6.28弧度/秒
                double radius = 1.0;
                double xOffset = Math.cos(angle) * radius;
                double zOffset = Math.sin(angle) * radius;

                world.spawnParticles(
                        ParticleTypes.WITCH,
                        pos.getX() + 0.5 + xOffset, pos.getY() + 1.2, pos.getZ() + 0.5 + zOffset,
                        2, 0.1, 0.1, 0.1, 0.05
                );
            }

            // 低频脉冲效果
            if (currentTime % 10 == 0) {
                world.spawnParticles(
                        ParticleTypes.END_ROD,
                        pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5,
                        10, 0.5, 0.5, 0.5, 0.2
                );

                // 脉冲音效
                world.playSound(
                        null,
                        pos.getX(), pos.getY(), pos.getZ(),
                        SoundEvents.BLOCK_BEACON_AMBIENT,
                        SoundCategory.PLAYERS,
                        0.8f,
                        0.6f
                );
            }
        }
    }
}
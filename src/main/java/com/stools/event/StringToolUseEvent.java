package com.stools.event;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StringToolUseEvent {
    private static final int DURABILITY_COST = 15;
    private static final int RANGE = 15; // 增加切割范围
    private static final float DAMAGE = 6.0f; // 提高基础伤害
    private static final int BLEED_DURATION = 200; // 流血持续时间 (10秒)
    private static final double ATTACK_WIDTH = 2.0; // 增加切割宽度

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.STRING) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));
                        performStringCut(player, (ServerWorld) world);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void performStringCut(PlayerEntity player, ServerWorld world) {
        // 获取玩家视线方向
        Vec3d lookVec = player.getRotationVec(1.0F);

        // 计算切割起点和终点
        Vec3d startPos = player.getCameraPosVec(1.0F);
        Vec3d endPos = startPos.add(lookVec.multiply(RANGE));

        // 播放音效
        world.playSound(null, player.getBlockPos(),
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                SoundCategory.PLAYERS, 1.0f, 1.8f);

        // 创建切割效果
        createCuttingEffect(world, startPos, endPos);

        // 检测并伤害路径上的实体
        damageEntitiesInPath(world, player, startPos, endPos);
    }

    private static void createCuttingEffect(ServerWorld world, Vec3d start, Vec3d end) {
        // 计算切割路径上的粒子点
        int points = 30; // 粒子数量
        List<Vec3d> linePoints = new ArrayList<>();

        // 主路径
        for (int i = 0; i <= points; i++) {
            double progress = (double) i / points;
            double x = start.x + (end.x - start.x) * progress;
            double y = start.y + (end.y - start.y) * progress;
            double z = start.z + (end.z - start.z) * progress;
            linePoints.add(new Vec3d(x, y, z));
        }

        // 生成切割粒子效果
        for (Vec3d point : linePoints) {
            // 主切割线粒子
            world.spawnParticles(ParticleTypes.END_ROD,
                    point.x, point.y, point.z,
                    3, 0.2, 0.2, 0.2, 0.01); // 粒子数量和范围

            // 添加"弦丝"粒子
            if (world.random.nextFloat() < 0.4f) {
                double offsetX = (world.random.nextDouble() - 0.5) * ATTACK_WIDTH;
                double offsetY = (world.random.nextDouble() - 0.5) * 0.5;
                double offsetZ = (world.random.nextDouble() - 0.5) * ATTACK_WIDTH;

                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                        point.x + offsetX, point.y + offsetY, point.z + offsetZ,
                        1, 0, 0, 0, 0.01);
            }
        }

        // 起点和终点的特效
        world.spawnParticles(ParticleTypes.CRIT,
                start.x, start.y, start.z,
                15, 0.2, 0.2, 0.2, 0.1);

        world.spawnParticles(ParticleTypes.CRIT,
                end.x, end.y, end.z,
                15, 0.2, 0.2, 0.2, 0.1);
    }

    private static void damageEntitiesInPath(ServerWorld world, PlayerEntity player, Vec3d start, Vec3d end) {
        // 计算路径的包围盒
        double minX = Math.min(start.x, end.x) - ATTACK_WIDTH;
        double minY = Math.min(start.y, end.y) - ATTACK_WIDTH/2;
        double minZ = Math.min(start.z, end.z) - ATTACK_WIDTH;
        double maxX = Math.max(start.x, end.x) + ATTACK_WIDTH;
        double maxY = Math.max(start.y, end.y) + ATTACK_WIDTH/2;
        double maxZ = Math.max(start.z, end.z) + ATTACK_WIDTH;

        Box pathBox = new Box(minX, minY, minZ, maxX, maxY, maxZ);

        // 获取伤害源
        DamageSource source = world.getDamageSources().playerAttack(player);

        // 检测路径上的实体
        for (Entity entity : world.getOtherEntities(player, pathBox)) {
            if (entity instanceof LivingEntity living) {
                // 计算实体到切割线的距离
                double distance = distanceToLine(start, end, entity.getPos());

                // 攻击范围2.0格
                if (distance < ATTACK_WIDTH) {
                    // 基础伤害 + 距离衰减
                    float finalDamage = DAMAGE * (float)(1.0 - (distance / ATTACK_WIDTH));

                    living.damage(source, finalDamage);

                    // 施加流血效果
                    living.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.WITHER, BLEED_DURATION, 0));

                    // 伤害特效
                    world.spawnParticles(ParticleTypes.DAMAGE_INDICATOR,
                            entity.getX(), entity.getY() + entity.getHeight()/2, entity.getZ(),
                            10, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }
    }

    // 计算点到线段的距离
    private static double distanceToLine(Vec3d lineStart, Vec3d lineEnd, Vec3d point) {
        Vec3d lineVec = lineEnd.subtract(lineStart);
        Vec3d pointVec = point.subtract(lineStart);

        double lineLength = lineVec.length();
        double dotProduct = pointVec.dotProduct(lineVec) / lineLength;

        // 计算投影点
        double t = Math.max(0, Math.min(1, dotProduct / lineLength));
        Vec3d projection = lineStart.add(lineVec.multiply(t));

        return projection.distanceTo(point);
    }
}
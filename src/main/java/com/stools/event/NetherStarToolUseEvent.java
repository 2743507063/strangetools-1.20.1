package com.stools.event;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
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
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetherStarToolUseEvent {

    private static final int DURABILITY_COST = 220;
    private static final int RIFT_DURATION = 100; // 5秒(20tick/s)
    private static final Map<UUID, RiftData> activeRifts = new HashMap<>();

    public static class RiftData {
        public BlockPos position;
        public int remainingTicks;
        public PlayerEntity creator;

        public RiftData(BlockPos pos, int duration, PlayerEntity player) {
            this.position = pos;
            this.remainingTicks = duration;
            this.creator = player;
        }
    }

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.NETHER_STAR) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));
                        createAstralRift(player, (ServerWorld) world);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void createAstralRift(PlayerEntity player, ServerWorld world) {
        // 获取目标位置
        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d targetPos = player.getPos().add(lookVec.multiply(12));

        BlockPos riftPos = world.raycast(new RaycastContext(
                player.getCameraPosVec(1.0F),
                targetPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        )).getBlockPos().up();

        // 创建裂隙数据
        activeRifts.put(UUID.randomUUID(), new RiftData(riftPos, RIFT_DURATION, player));

        // 初始效果
        world.playSound(null, riftPos,
                SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                SoundCategory.PLAYERS, 1.5f, 0.7f);

        // 生成初始粒子
        for (int i = 0; i < 50; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 3;
            double offsetY = world.random.nextDouble() * 2;
            double offsetZ = (world.random.nextDouble() - 0.5) * 3;
            world.spawnParticles(ParticleTypes.PORTAL,
                    riftPos.getX() + 0.5 + offsetX,
                    riftPos.getY() + 0.5 + offsetY,
                    riftPos.getZ() + 0.5 + offsetZ,
                    5, 0, 0, 0, 0.1);
        }
    }

    // 在ServerTick中调用
    public static void updateRifts(ServerWorld world) {
        activeRifts.entrySet().removeIf(entry -> {
            RiftData rift = entry.getValue();
            rift.remainingTicks--;

            // 引力效果
            applyGravityEffect(world, rift);

            // 粒子效果
            spawnRiftParticles(world, rift);

            // 终结爆炸
            if (rift.remainingTicks <= 0) {
                createFinalExplosion(world, rift);
                return true; // 移除裂隙
            }
            return false;
        });
    }

    private static void applyGravityEffect(ServerWorld world, RiftData rift) {
        double radius = 8.0;
        Box area = new Box(
                rift.position.getX() - radius,
                rift.position.getY() - 2,
                rift.position.getZ() - radius,
                rift.position.getX() + radius,
                rift.position.getY() + 4,
                rift.position.getZ() + radius
        );

        Vec3d center = new Vec3d(
                rift.position.getX() + 0.5,
                rift.position.getY() + 0.5,
                rift.position.getZ() + 0.5
        );

        for (Entity entity : world.getOtherEntities(null, area)) {
            if (entity instanceof LivingEntity || entity instanceof TntEntity) {
                Vec3d toCenter = center.subtract(entity.getPos()).normalize();
                double distance = center.distanceTo(entity.getPos());
                double strength = 0.3 * (1 - (distance / radius));

                // 拉向中心
                entity.addVelocity(toCenter.x * strength, toCenter.y * strength * 0.7, toCenter.z * strength);

                // 每秒伤害
                if (rift.remainingTicks % 20 == 0 && entity instanceof LivingEntity) {
                    float damage = 3.0f + (rift.creator.getAttackCooldownProgress(0) * 4);
                    ((LivingEntity) entity).damage(world.getDamageSources().magic(), damage);
                }
            }
        }
    }

    private static void spawnRiftParticles(ServerWorld world, RiftData rift) {
        // 核心粒子
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                rift.position.getX() + 0.5,
                rift.position.getY() + 0.5,
                rift.position.getZ() + 0.5,
                10, 0.2, 0.2, 0.2, 0.05);

        // 边缘粒子
        if (world.random.nextFloat() < 0.3f) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double distance = 1.5 + world.random.nextDouble() * 1.5;
            double x = rift.position.getX() + 0.5 + Math.cos(angle) * distance;
            double z = rift.position.getZ() + 0.5 + Math.sin(angle) * distance;

            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    x, rift.position.getY() + 0.8, z,
                    3, 0, 0.1, 0, 0.05);
        }
    }

    private static void createFinalExplosion(ServerWorld world, RiftData rift) {
        // 爆炸效果
        world.createExplosion(null,
                rift.position.getX() + 0.5,
                rift.position.getY() + 0.5,
                rift.position.getZ() + 0.5,
                5.0f, World.ExplosionSourceType.NONE);

        // 音效
        world.playSound(null, rift.position,
                SoundEvents.ENTITY_ENDER_DRAGON_SHOOT,
                SoundCategory.PLAYERS, 2.0f, 0.5f);

        // 爆炸粒子
        for (int i = 0; i < 30; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double distance = world.random.nextDouble() * 3;
            double x = Math.cos(angle) * distance;
            double z = Math.sin(angle) * distance;

            world.spawnParticles(ParticleTypes.FIREWORK,
                    rift.position.getX() + 0.5 + x,
                    rift.position.getY() + 0.5,
                    rift.position.getZ() + 0.5 + z,
                    5, 0, 0.2, 0, 0.1);
        }
    }
}
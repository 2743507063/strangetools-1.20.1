// EnderToolUseEvent.java
package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.entity.EnderPhantomEntity;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class EnderToolUseEvent {

    // 用于跟踪残影实体和它们的消失时间
    private static final Map<UUID, Long> ghostEntities = new HashMap<>();
    private static final int DURABILITY_COST = 80;
    private static final int GHOST_DURATION = 3 * 20; // 3秒，以tick为单位

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.ENDER_ALLOY) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));
                        teleportPlayer(player, (ServerWorld) world);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void teleportPlayer(PlayerEntity player, ServerWorld world) {
        // 计算传送目标位置
        Vec3d lookVec = player.getRotationVec(1.0F);
        double maxDistance = 8.0;
        Vec3d startPos = player.getCameraPosVec(1.0F);
        Vec3d endPos = startPos.add(lookVec.multiply(maxDistance));

        // 进行方块视线检测
        net.minecraft.util.hit.BlockHitResult blockHit = world.raycast(new RaycastContext(
                startPos,
                endPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        // 目标位置为视线交点或最大距离
        Vec3d targetPos = blockHit.getType() == net.minecraft.util.hit.BlockHitResult.Type.MISS ? endPos : blockHit.getPos();

        // 创建残影
        createGhostEntity(world, player.getPos(), player);

        // 调整目标位置确保玩家站在地面上
        BlockPos targetBlockPos = BlockPos.ofFloored(targetPos);
        if (!world.getBlockState(targetBlockPos.down()).isSolid()) {
            // 如果目标位置下方不是固体，寻找最近的固体地面
            for (int i = 1; i <= 3; i++) {
                if (world.getBlockState(targetBlockPos.down(i)).isSolid()) {
                    targetPos = new Vec3d(targetPos.x, targetBlockPos.getY() - i + 1, targetPos.z);
                    break;
                }
            }
        }

        // 传送玩家
        player.teleport(targetPos.x, targetPos.y, targetPos.z);

        // 播放传送效果
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 50, 0.5, 0.5, 0.5, 0.1);

        // 给玩家短暂的速度提升效果
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 1, false, true));
    }

    private static void createGhostEntity(ServerWorld world, Vec3d pos, PlayerEntity player) {
        EnderPhantomEntity ghost = new EnderPhantomEntity(world, player);
        world.spawnEntity(ghost);

        // 添加粒子效果
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                pos.x, pos.y + 1, pos.z,
                20, 0.5, 0.5, 0.5, 0.05);

        ghostEntities.put(ghost.getUuid(), world.getTime() + GHOST_DURATION);
    }
    public static void tick(ServerWorld world) {
        long currentTime = world.getTime();

        // 处理残影消失
        ghostEntities.entrySet().removeIf(entry -> {
            UUID ghostId = entry.getKey();
            long removeTime = entry.getValue();
            Entity entity = world.getEntity(ghostId);

            if (entity == null || currentTime >= removeTime) {
                if (entity != null) {
                    // 消失前播放效果
                    world.spawnParticles(ParticleTypes.SMOKE,
                            entity.getX(), entity.getY() + 1, entity.getZ(),
                            20, 0.3, 0.3, 0.3, 0.05);
                    entity.discard();
                }
                return true;
            }
            return false;
        });
    }

    // 寻找安全位置（最近的空气方块）
    private static BlockPos findSafePosition(World world, BlockPos pos) {
        // 从当前位置向上和向下搜索
        for (int y = pos.getY(); y < world.getTopY(); y++) {
            BlockPos testPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.isAir(testPos) && world.getBlockState(testPos.down()).isSolid()) {
                return testPos;
            }
        }
        return pos; // 找不到，返回原位置
    }
}
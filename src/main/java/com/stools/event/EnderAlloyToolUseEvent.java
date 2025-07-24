// EnderToolUseEvent.java
package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.entity.EnderPhantomEntity;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderAlloyToolUseEvent {

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
        // 只考虑水平方向（忽略垂直视角）
        Vec3d horizontalLookVec = Vec3d.fromPolar(0, player.getYaw());

        double maxDistance = 8.0;
        Vec3d startPos = player.getPos().add(0, player.getStandingEyeHeight(), 0);
        Vec3d endPos = startPos.add(horizontalLookVec.multiply(maxDistance));

        // 进行方块视线检测（仅水平）
        RaycastContext context = new RaycastContext(
                startPos,
                endPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        );

        net.minecraft.util.hit.BlockHitResult blockHit = world.raycast(context);

        // 目标位置为视线交点或最大距离
        Vec3d targetPos = blockHit.getType() == net.minecraft.util.hit.BlockHitResult.Type.MISS ?
                endPos : blockHit.getPos();

        // 创建残影
        createGhostEntity(world, player.getPos(), player);

        // 调整目标位置确保玩家站在地面上
        BlockPos targetBlockPos = BlockPos.ofFloored(targetPos);
        for (int i = 0; i < 5; i++) {
            BlockPos downPos = targetBlockPos.down();
            if (world.getBlockState(downPos).isSolidBlock(world, downPos)) {
                targetPos = new Vec3d(targetPos.x, targetBlockPos.getY(), targetPos.z);
                break;
            }
            targetBlockPos = downPos;
        }

        // 传送玩家（保持当前高度或调整到地面）
        player.teleport(targetPos.x, targetPos.y, targetPos.z);

        // 播放传送效果
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.spawnParticles(ParticleTypes.PORTAL,
                player.getX(), player.getY() + 1, player.getZ(),
                50, 0.5, 0.5, 0.5, 0.1);

        // 给玩家短暂的速度提升效
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
}
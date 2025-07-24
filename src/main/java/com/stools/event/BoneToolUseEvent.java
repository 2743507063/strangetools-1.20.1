package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BoneToolUseEvent {

    private static final int DURABILITY_COST = 30;
    private static final int SHIELD_DURATION = 200; // 10秒
    private static final Map<UUID, Long> activeShields = new HashMap<>(); // 玩家UUID到结束时间的映射
    private static final Map<UUID, List<Entity>> playerBones = new HashMap<>(); // 玩家UUID到骨头实体的映射

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.BONE) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));
                        activateBoneShield(player, (ServerWorld) world);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void activateBoneShield(PlayerEntity player, ServerWorld world) {
        // 清除之前的骨头
        clearActiveBonesForPlayer(player);

        // 添加伤害吸收效果
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.ABSORPTION,
                SHIELD_DURATION,
                2, // 吸收4颗心
                false,
                true
        ));

        // 记录护盾结束时间（当前世界时间 + 持续时间）
        activeShields.put(player.getUuid(), world.getTime() + SHIELD_DURATION);

        // 创建新的骨头实体列表
        List<Entity> bones = new ArrayList<>();
        playerBones.put(player.getUuid(), bones);

        // 生成3个盔甲架作为旋转的骨头
        for (int i = 0; i < 3; i++) {
            Entity armorStand = EntityType.ARMOR_STAND.create(world);
            if (armorStand != null) {
                armorStand.setPosition(player.getPos());
                armorStand.setNoGravity(true);
                armorStand.setInvulnerable(true);
                armorStand.setInvisible(true); // 不可见
                armorStand.setSilent(true);
                armorStand.addCommandTag("bone_shield");
                world.spawnEntity(armorStand);
                bones.add(armorStand);
            }
        }

        // 播放音效
        world.playSound(null, player.getBlockPos(),
                SoundEvents.ENTITY_WITHER_SHOOT,
                SoundCategory.PLAYERS, 1.0f, 0.8f);
    }

    private static void clearActiveBonesForPlayer(PlayerEntity player) {
        List<Entity> bones = playerBones.get(player.getUuid());
        if (bones != null) {
            for (Entity bone : bones) {
                if (bone.isAlive()) {
                    bone.discard();
                }
            }
            playerBones.remove(player.getUuid());
        }
        activeShields.remove(player.getUuid());
    }

    // 这个方法需要在每个tick调用
    public static void updateBoneShields(ServerWorld world) {
        long currentTime = world.getTime();

        // 检查所有活动的护盾
        for (Map.Entry<UUID, Long> entry : new ArrayList<>(activeShields.entrySet())) {
            UUID playerId = entry.getKey();
            long endTime = entry.getValue();

            // 如果护盾过期，清除骨头
            if (currentTime >= endTime) {
                PlayerEntity player = world.getPlayerByUuid(playerId);
                if (player != null) {
                    clearActiveBonesForPlayer(player);
                } else {
                    activeShields.remove(playerId);
                    playerBones.remove(playerId);
                }
                continue;
            }

            // 更新骨头位置
            PlayerEntity player = world.getPlayerByUuid(playerId);
            if (player == null) continue;

            List<Entity> bones = playerBones.get(playerId);
            if (bones == null || bones.isEmpty()) continue;

            // 更新每个骨头的位置
            for (int i = 0; i < bones.size(); i++) {
                Entity bone = bones.get(i);
                if (!bone.isAlive()) continue;

                // 计算旋转角度：每个骨头间隔120度
                double angle = (currentTime % 360) * Math.PI / 180.0 + (i * 120 * Math.PI / 180.0);
                double radius = 1.5;
                double x = player.getX() + radius * Math.cos(angle);
                double y = player.getY() + 1.0;
                double z = player.getZ() + radius * Math.sin(angle);

                bone.setPosition(x, y, z);

                // 粒子效果
                world.spawnParticles(ParticleTypes.ASH,
                        bone.getX(), bone.getY(), bone.getZ(),
                        2, 0.1, 0.1, 0.1, 0.01);

                // 伤害附近的敌人
                Box area = bone.getBoundingBox().expand(0.5);
                for (Entity entity : world.getOtherEntities(player, area)) {
                    if (entity instanceof LivingEntity && !entity.getCommandTags().contains("bone_shield")) {
                        // 使用正确的伤害来源
                        entity.damage(world.getDamageSources().magic(), 2.0f);
                    }
                }

                // 反弹投射物
                for (Entity projectile : world.getOtherEntities(bone, bone.getBoundingBox())) {
                    if (projectile instanceof ProjectileEntity) {
                        Vec3d velocity = projectile.getVelocity();
                        projectile.setVelocity(-velocity.x, -velocity.y, -velocity.z);
                    }
                }
            }
        }
    }
}
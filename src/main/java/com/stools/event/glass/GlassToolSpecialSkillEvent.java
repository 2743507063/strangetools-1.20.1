package com.stools.event.glass;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class GlassToolSpecialSkillEvent {
    private static final int WATER_SKILL_COST = 10; // 提高消耗以平衡新技能
    private static final int LAVA_SKILL_COST = 20;
    private static final int WATER_SKILL_COOLDOWN = 40;
    private static final int LAVA_SKILL_COOLDOWN = 60;

    // 潮汐冲击参数
    private static final double TIDAL_WAVE_RANGE = 8.0; // 冲击波范围
    private static final double TIDAL_WAVE_WIDTH = 3.0; // 冲击波宽度
    private static final float TIDAL_DAMAGE = 3.0f; // 伤害值
    private static final int TIDAL_KNOCKBACK = 2; // 击退强度

    private static final int MAGMA_BURST_DAMAGE = 15; // 岩浆爆破消耗的耐久
    private static final int MAGMA_BURST_COOLDOWN = 60; // 3秒冷却
    private static final float EXPLOSION_POWER = 3.0f; // 爆炸强度

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (!(player.isSneaking() && stack.getItem() instanceof ToolItem tool &&
                    tool.getMaterial() == ModToolMaterials.GLASS)) {
                return TypedActionResult.pass(stack);
            }

            // 检查是否正对流体源（防止冲突）
            BlockHitResult fluidHit = raycast(world, player, RaycastContext.FluidHandling.SOURCE_ONLY);
            if (fluidHit.getType() == HitResult.Type.BLOCK) {
                if (world.getFluidState(fluidHit.getBlockPos()).isIn(FluidTags.WATER) ||
                        world.getFluidState(fluidHit.getBlockPos()).isIn(FluidTags.LAVA)) {
                    return TypedActionResult.pass(stack);
                }
            }

            NbtCompound tag = stack.getOrCreateNbt();
            String content = tag.getString("BottleContent");
            if (hand != Hand.MAIN_HAND) return TypedActionResult.pass(stack);

            // ===== 改进的水技能：潮汐冲击 =====
            if ("water".equals(content)) {
                if (stack.getDamage() + WATER_SKILL_COST >= stack.getMaxDamage())
                    return TypedActionResult.fail(stack);

                if (!world.isClient()) {
                    // 播放技能启动音效
                    world.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.PLAYERS,
                            1.0F, 0.8F);

                    // 创建潮汐冲击效果
                    createTidalWave(player, world);

                    // 移除玩家身上燃烧
                    player.extinguish();

                    // 扣耐久，清空
                    stack.damage(WATER_SKILL_COST, player, p -> p.sendToolBreakStatus(hand));
                    tag.putString("BottleContent", "empty");
                    stack.setNbt(tag);
                }

                // 添加冷却
                if (world instanceof net.minecraft.server.world.ServerWorld) {
                    player.getItemCooldownManager().set(stack.getItem(), WATER_SKILL_COOLDOWN);
                }

                return TypedActionResult.success(stack);
            }

            if ("lava".equals(content)) {
                if (stack.getDamage() + MAGMA_BURST_DAMAGE >= stack.getMaxDamage()) {
                    return TypedActionResult.fail(stack);
                }

                if (!world.isClient()) {
                    // 1. 在玩家位置创建爆炸效果
                    world.createExplosion(
                            player,
                            player.getX(),
                            player.getY() + 1.0,
                            player.getZ(),
                            EXPLOSION_POWER,
                            false,
                            World.ExplosionSourceType.NONE
                    );

                    // 2. 对周围敌人造成伤害和点燃
                    Box area = new Box(
                            player.getBlockPos().add(-5, -2, -5),
                            player.getBlockPos().add(5, 3, 5)
                    );

                    for (LivingEntity entity : world.getEntitiesByClass(
                            LivingEntity.class, area, e -> e != player)) {
                        // 造成火焰伤害
                        entity.damage(player.getDamageSources().inFire(), 6.0f);

                        // 点燃实体
                        entity.setOnFireFor(5);

                        // 添加击退效果
                        Vec3d knockback = entity.getPos().subtract(player.getPos()).normalize();
                        entity.addVelocity(knockback.x * 0.8, 0.4, knockback.z * 0.8);
                    }

                    // 3. 创建岩浆粒子效果
                    if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                        serverWorld.spawnParticles(
                                ParticleTypes.LAVA,
                                player.getX(),
                                player.getY() + 1.5,
                                player.getZ(),
                                100,
                                3.0, 2.0, 3.0,
                                0.2
                        );

                        serverWorld.spawnParticles(
                                ParticleTypes.FLAME,
                                player.getX(),
                                player.getY() + 1.0,
                                player.getZ(),
                                50,
                                2.0, 1.0, 2.0,
                                0.1
                        );
                    }

                    // 4. 播放音效
                    world.playSound(
                            null,
                            player.getBlockPos(),
                            SoundEvents.ENTITY_GHAST_SHOOT,
                            SoundCategory.PLAYERS,
                            1.0F,
                            0.7F
                    );

                    // 5. 消耗耐久并清空内容
                    stack.damage(MAGMA_BURST_DAMAGE, player, p -> p.sendToolBreakStatus(hand));
                    tag.putString("BottleContent", "empty");
                }

                // 设置冷却时间
                player.getItemCooldownManager().set(stack.getItem(), MAGMA_BURST_COOLDOWN);

                return TypedActionResult.success(stack);
            }

            return TypedActionResult.pass(stack);
        }); // 修复这里：移除了多余的括号
    }
    // 创建潮汐冲击波
    private static void createTidalWave(PlayerEntity player, World world) {
        Vec3d startPos = player.getCameraPosVec(1.0f);
        Vec3d lookVec = player.getRotationVec(1.0f);
        Vec3d endPos = startPos.add(lookVec.multiply(TIDAL_WAVE_RANGE));

        // 检测命中的实体
        EntityHitResult entityHit = ProjectileUtil.raycast(
                player,
                startPos,
                endPos,
                new Box(startPos, endPos),
                entity -> !entity.isSpectator() && entity.canHit(),
                TIDAL_WAVE_RANGE
        );

        // 粒子效果
        if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
            // 沿冲击路径生成粒子
            for (int i = 0; i < 20; i++) {
                double progress = i / 20.0 * TIDAL_WAVE_RANGE;
                Vec3d particlePos = startPos.add(lookVec.multiply(progress));

                serverWorld.spawnParticles(
                        ParticleTypes.SPLASH,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        3,
                        0.5, 0.5, 0.5,
                        0.1
                );

                serverWorld.spawnParticles(
                        ParticleTypes.BUBBLE,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        2,
                        0.3, 0.3, 0.3,
                        0.05
                );
            }
        }

        // 对路径上的实体造成影响
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
            // 造成伤害
            target.damage(player.getDamageSources().drown(), TIDAL_DAMAGE);

            // 击退效果
            Vec3d knockbackVec = lookVec.multiply(TIDAL_KNOCKBACK);
            target.addVelocity(knockbackVec.x, knockbackVec.y * 0.5, knockbackVec.z);

            // 灭火
            target.extinguish();

            // 添加减速效果
            target.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    60, // 3秒
                    1   // 减速II
            ));
        }

        // 播放冲击命中音效
        world.playSound(null, endPos.x, endPos.y, endPos.z,
                SoundEvents.ENTITY_PLAYER_SPLASH_HIGH_SPEED,
                SoundCategory.PLAYERS, 1.0F, 0.7F);
    }

    private static BlockHitResult raycast(World world, net.minecraft.entity.player.PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        double reach = player.isCreative() ? 5.0D : 4.5D;
        return world.raycast(new RaycastContext(
                player.getCameraPosVec(1.0F),
                player.getCameraPosVec(1.0F).add(player.getRotationVec(1.0F).multiply(reach)),
                RaycastContext.ShapeType.OUTLINE,
                fluidHandling,
                player
        ));
    }
}
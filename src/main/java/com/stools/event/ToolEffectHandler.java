package com.stools.event;

import com.stools.Strangetools;
import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.stools.item.materials.ModToolMaterials;

import java.util.*;

public class ToolEffectHandler {
    private static final Random random = new Random();

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            // 先检查总开关是否开启
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return ActionResult.PASS;
            }
            // 再检查原有效果开关
            if (!ModConfigManager.CONFIG.toolEffects.enableToolEffects) {
                return ActionResult.PASS;
            }

            if (!world.isClient() && entity instanceof LivingEntity) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.getItem() instanceof ToolItem toolItem &&
                        toolItem.getMaterial() instanceof ModToolMaterials material) {

                    applyToolEffect(material, player, (LivingEntity) entity, world, stack, hand);
                }
            }
            return ActionResult.PASS;
        });
    }

    private static void applyToolEffect(ModToolMaterials material,
                                        PlayerEntity player,
                                        LivingEntity target,
                                        World world,
                                        ItemStack stack,
                                        Hand hand) {

        switch (material) {
            case COPPER:
                float copperChance = ModConfigManager.CONFIG.toolEffects.copperIgniteChance / 100f;
                if (random.nextFloat() < copperChance) {
                    target.setOnFireFor(2);
                    world.playSound(null, target.getBlockPos(),
                            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                            SoundCategory.PLAYERS, 0.8f, 1.2f);
                }
                break;

            case EMERALD:
                float emeraldChance = ModConfigManager.CONFIG.toolEffects.emeraldDropChance / 100f;
                if (random.nextFloat() < emeraldChance) {
                    target.dropStack(new ItemStack(Items.EMERALD, 1));
                }
                break;

            case LAPIS:
                player.addExperience(1 + random.nextInt(3));
                break;

            case REDSTONE:
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HASTE, 100, 1, true, false));
                break;

            case QUARTZ:
                float quartzDamage = ModConfigManager.CONFIG.toolEffects.quartzExtraDamage;
                target.damage(target.getDamageSources().magic(), quartzDamage);
                break;

            case COAL:
                int fireTime = 1 + random.nextInt(4);
                target.setOnFireFor(fireTime);
                break;

            case CAKE:
                if (player.getHungerManager().getFoodLevel() < 20) {
                    player.getHungerManager().add(1, 0.1f);
                }
                break;

            case OBSIDIAN:
                target.takeKnockback(0.5f,
                        player.getX() - target.getX(),
                        player.getZ() - target.getZ());
                break;

            case PRISMARINE:
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WATER_BREATHING, 200, 0, true, false));
                break;

            case ROTTEN_FLESH:
                float rottenChance = ModConfigManager.CONFIG.toolEffects.rottenFleshHungerChance / 100f;
                if (random.nextFloat() < rottenChance) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.HUNGER, 100, 0, true, false));
                }
                break;
            case GLOWSTONE:
                if (!ModConfigManager.CONFIG.glowstoneEffects.enableEffects) {
                    break; // 开关关闭则不执行效果
                }
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING, 100, 0, false, true
                ));

                if (!world.isClient()) {
                    world.playSound(null, target.getBlockPos(),
                            SoundEvents.BLOCK_GLASS_BREAK,
                            SoundCategory.PLAYERS, 0.7f, 1.5f);
                } else {
                    for (int i = 0; i < 10; i++) {
                        double x = target.getX() + (world.random.nextDouble() - 0.5);
                        double y = target.getY() + world.random.nextDouble() * 2;
                        double z = target.getZ() + (world.random.nextDouble() - 0.5);
                        world.addParticle(ParticleTypes.GLOW, x, y, z, 0, 0.1, 0);
                    }
                }
                break;
            case BLAZE_POWDER:
                target.setOnFireFor(3);
                break;
            case BONE:
                if (random.nextFloat() < 0.3f) {
                    target.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.SLOWNESS, 100, 1
                    ));

                    if (world.isClient()) {
                        for (int i = 0; i < 5; i++) {
                            world.addParticle(ParticleTypes.ASH,
                                    target.getX(),
                                    target.getY() + 1,
                                    target.getZ(),
                                    0, 0.1, 0);
                        }
                    }
                }
                break;
            case NETHERRACK:
                target.setOnFireFor(3);
                if (random.nextFloat() < 0.2f) {
                    if (!world.isClient()) {
                        world.createExplosion(
                                null,
                                target.getX(),
                                target.getY(),
                                target.getZ(),
                                1.5f,
                                World.ExplosionSourceType.NONE
                        );
                    }

                    if (world.isClient()) {
                        for (int i = 0; i < 15; i++) {
                            double offsetX = (random.nextDouble() - 0.5) * 2;
                            double offsetY = random.nextDouble() * 1.5;
                            double offsetZ = (random.nextDouble() - 0.5) * 2;

                            world.addParticle(ParticleTypes.FLAME,
                                    target.getX(),
                                    target.getY() + 1,
                                    target.getZ(),
                                    offsetX, offsetY, offsetZ);
                        }
                    }
                }
                break;
            case GLASS:
                world.playSound(null, target.getBlockPos(),
                        SoundEvents.BLOCK_GLASS_BREAK,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
                break;
            case POTION:
                // 攻击时有20%概率传递药水效果
                if (random.nextFloat() < 0.2f) {
                    List<StatusEffectInstance> activeEffects = new ArrayList<>(player.getActiveStatusEffects().values());
                    if (!activeEffects.isEmpty()) {
                        StatusEffectInstance instance = activeEffects.get(random.nextInt(activeEffects.size()));

                        target.addStatusEffect(new StatusEffectInstance(
                                instance.getEffectType(),
                                instance.getDuration() / 2,
                                instance.getAmplifier()
                        ));
                    }
                }
                break;
            case ENDER_ALLOY:
                if (random.nextFloat() < 0.25f) { // 25%触发概率
                    // 计算目标身后的位置
                    Vec3d lookVec = target.getRotationVec(0.0F).normalize();
                    Vec3d behindPos = target.getPos().add(lookVec.multiply(-1.5));

                    // 保存玩家当前位置（用于粒子效果）
                    Vec3d originalPos = player.getPos();

                    // 确保传送位置安全（有支撑方块）
                    BlockPos targetPos = BlockPos.ofFloored(behindPos);
                    boolean foundSolidGround = false;

                    // 向下搜索5格寻找支撑面
                    for (int i = 0; i < 5; i++) {
                        if (world.getBlockState(targetPos.down()).isSolidBlock(world, targetPos.down())) {
                            behindPos = new Vec3d(behindPos.x, targetPos.getY(), behindPos.z);
                            foundSolidGround = true;
                            break;
                        }
                        targetPos = targetPos.down();
                    }

                    // 如果没有找到安全位置，则传送到目标位置上方1格
                    if (!foundSolidGround) {
                        behindPos = new Vec3d(behindPos.x, target.getY() + 1, behindPos.z);
                    }

                    // 传送玩家到目标身后
                    player.teleport(behindPos.x, behindPos.y, behindPos.z);

                    // 给予缓降效果防止摔落伤害
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.SLOW_FALLING, 100, 0, false, true
                    ));

                    // 造成额外伤害
                    float extraDamage = 4.0f;
                    target.damage(target.getDamageSources().playerAttack(player), extraDamage);

                    // 粒子效果和音效
                    if (world instanceof ServerWorld serverWorld) {
                        // 原位置粒子
                        serverWorld.spawnParticles(ParticleTypes.PORTAL,
                                originalPos.x, originalPos.y + 1, originalPos.z,
                                20, 0.5, 0.5, 0.5, 0.1);

                        // 新位置粒子
                        serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                                behindPos.x, behindPos.y + 1, behindPos.z,
                                20, 0.5, 0.5, 0.5, 0.1);

                        // 音效
                        serverWorld.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                                SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }
                }
                break;
            case END_STONE:
                // 检查是否为末地生物
                if (isEndMob(target)) {
                    float endStoneDamageBonus = ModConfigManager.CONFIG.toolEffects.endStoneDamageBonus / 100f;
                    float baseDamage = material.getAttackDamage();
                    float bonusDamage = baseDamage * endStoneDamageBonus;

                    // 造成额外伤害
                    target.damage(target.getDamageSources().magic(), bonusDamage);

                    // 粒子效果和音效
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                                target.getX(), target.getY() + target.getHeight() / 2, target.getZ(),
                                10, 0.3, 0.3, 0.3, 0.05);
                    }

                    world.playSound(null, target.getBlockPos(),
                            SoundEvents.BLOCK_END_PORTAL_FRAME_FILL,
                            SoundCategory.PLAYERS, 0.7f, 1.2f);
                }
                break;
            case CHORUS_FRUIT:
                if (player.getHungerManager().getFoodLevel() < 20) {
                    player.getHungerManager().add(2, 0.1f);
                }
                break;
            case VOID:
                // 检查目标是否为末地生物
                if (target instanceof EndermanEntity || target instanceof ShulkerEntity
                        || target instanceof EndermiteEntity || target instanceof EnderDragonEntity) {
                    // 读取配置中的额外伤害百分比（参考endStoneDamageBonus）
                    float extraDamagePercent = ModConfigManager.CONFIG.toolEffects.endStoneDamageBonus / 100f;
                    float baseDamage = 2.0f; // 基础伤害可调整，或从攻击事件中获取
                    float extraDamage = baseDamage * extraDamagePercent;
                    target.damage(target.getDamageSources().magic(), extraDamage);
                }
                break;
            case SWEET_BERRIES:
                float sweetBerryPoisonChance = ModConfigManager.CONFIG.toolEffects.sweetBerriesPoisonChance / 100f;
                if (random.nextFloat() < sweetBerryPoisonChance) {
                    target.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.POISON, 60, 0 // 3秒中毒（60 ticks），等级0
                    ));
                    break;
                }
                break;
            case POISONOUS_POTATO:
                float potatoPoisonChance = ModConfigManager.CONFIG.toolEffects.poisonousPotatoPoisonChance / 100f;
                if (random.nextFloat() < potatoPoisonChance) {
                    target.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.POISON,
                            100,  // 持续时间
                            0,     // 效果等级（0为1级）
                            false,
                            false,
                            true
                    ));
                }
                break;
            case AMETHYST:
                if (!ModConfigManager.CONFIG.amethystEffects.enableEffects) {
                    break;
                }

                // 获取配置值
                float crystalChance = ModConfigManager.CONFIG.amethystEffects.passiveCrystalChance / 100f;
                float crystalDamage = ModConfigManager.CONFIG.amethystEffects.passiveCrystalDamage;

                // 水晶共鸣被动效果
                if (random.nextFloat() < crystalChance) {
                    // 消耗1点耐久
                    stack.damage(1, player, p -> p.sendToolBreakStatus(hand));

                    // 造成额外伤害
                    target.damage(target.getDamageSources().magic(), crystalDamage);

                    // 播放音效和粒子
                    world.playSound(null, target.getBlockPos(),
                            SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                            SoundCategory.PLAYERS, 0.8f, 1.2f);

                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                                target.getX(), target.getY() + 1, target.getZ(),
                                10, 0.5, 0.5, 0.5, 0.05);
                    }
                }
                break;
            case FLINT:
                if (random.nextFloat() < 0.25f) {
                    // 随机生成1或2秒的火焰持续时间
                    int fireSeconds = random.nextBoolean() ? 1 : 2;
                    // 为目标实体设置火焰效果
                    target.setOnFireFor(fireSeconds * 20);
                }
                break;
        }
    }
    private static boolean isEndMob(LivingEntity entity) {
        // 原生末地生物检测
        if (entity instanceof EndermanEntity ||
                entity instanceof EndermiteEntity ||
                entity instanceof ShulkerEntity ||
                entity instanceof EnderDragonEntity) {
            return true;
        }

        // 检测模组实体
        Identifier entityId = EntityType.getId(entity.getType());
        return entityId != null &&
                entityId.getNamespace().equals("dummmmmmy") &&
                entityId.getPath().equals("target_dummy");
    }
}
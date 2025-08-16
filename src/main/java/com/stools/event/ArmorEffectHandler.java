package com.stools.event;

import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import com.stools.item.materials.ModArmorMaterials;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.joml.Vector3f;

import java.util.*;

public class ArmorEffectHandler {
    private static final Map<ModArmorMaterials, List<StatusEffectInstance>> ARMOR_EFFECTS_MAP =
            new EnumMap<>(ModArmorMaterials.class);

    static {
        ARMOR_EFFECTS_MAP.put(ModArmorMaterials.EMERALD, List.of(
                new StatusEffectInstance(StatusEffects.LUCK, 100, 1, false, false, true)
        ));
        ARMOR_EFFECTS_MAP.put(ModArmorMaterials.LAPIS, List.of(
                new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, false, false, true)
        ));
    }

    private static final Random RANDOM = new Random();

    public static void register() {
        initArmorEffects();

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            // 修复检查逻辑
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills ||
                    !ModConfigManager.CONFIG.armorEffects.enableArmorEffects) {
                return true;
            }

            if (source.getAttacker() instanceof LivingEntity attacker) {
                applyReflectiveDamage(entity, attacker);
            }
            return true;
        });

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            // 修复检查逻辑
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills ||
                    !ModConfigManager.CONFIG.armorEffects.enableArmorEffects) {
                return;
            }

            if (!(world instanceof ServerWorld)) return;

            for (PlayerEntity player : world.getPlayers()) {
                if (player instanceof ServerPlayerEntity) {
                    applyContinuousEffects(player);
                }
            }
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            // 修复检查逻辑
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills ||
                    !ModConfigManager.CONFIG.armorEffects.enableArmorEffects) {
                return;
            }

            applyContinuousEffects(player);
        });
    }

    private static void initArmorEffects() {
        // 可扩展添加更多盔甲效果
    }

    private static void applyReflectiveDamage(LivingEntity wearer, LivingEntity attacker) {
        float totalEffectPower = 0;
        ModArmorMaterials primaryMaterial = null;

        for (ItemStack armor : wearer.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem armorItem &&
                    armorItem.getMaterial() instanceof ModArmorMaterials mat) {
                totalEffectPower += mat.getEffectPower();
                // 确保只设置一次主要材料
                if (primaryMaterial == null) {
                    primaryMaterial = mat;
                }
            }
        }

        // 确保primaryMaterial不为null
        if (totalEffectPower > 0 && primaryMaterial != null) {
            // 绿宝石盔甲专属效果：反弹伤害
            if (primaryMaterial == ModArmorMaterials.EMERALD) {
                float reflectChance = ModConfigManager.CONFIG.armorEffects.armorReflectChance / 100f;
                if (wearer.getRandom().nextFloat() < reflectChance) {
                    float reflectDamage = ModConfigManager.CONFIG.armorEffects.armorReflectDamage;
                    attacker.damage(wearer.getDamageSources().thorns(wearer), reflectDamage);
                    wearer.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.GLOWING, 40, 0, true, false));
                }
            }

            // 青金石盔甲专属效果：经验窃取
            if (primaryMaterial == ModArmorMaterials.LAPIS) {
                float xpStealChance = 0.3f;
                if (wearer.getRandom().nextFloat() < xpStealChance) {
                    int stolenXp = 1 + RANDOM.nextInt(3);

                    if (attacker instanceof PlayerEntity) {
                        ((PlayerEntity) attacker).addExperience(-stolenXp);
                    }
                    if (wearer instanceof PlayerEntity) {
                        ((PlayerEntity) wearer).addExperience(stolenXp);

                        if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                                    wearer.getX(), wearer.getY() + 1.5, wearer.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.1);
                        }
                    }
                }
            }

            // 铜盔甲专属效果
            if (primaryMaterial == ModArmorMaterials.COPPER) {
                // 效果1: 简易导电 - 雷暴天气小概率击退敌人
                if (wearer.getWorld().isThundering()) {
                    float lightningChance = ModConfigManager.CONFIG.armorEffects.copperPushChance / 100f;
                    if (RANDOM.nextFloat() < lightningChance) {
                        attacker.takeKnockback(0.5f,
                                wearer.getX() - attacker.getX(),
                                wearer.getZ() - attacker.getZ());
                        wearer.getWorld().playSound(null, attacker.getBlockPos(),
                                SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                                SoundCategory.PLAYERS, 0.5f, 1.5f);
                    }
                }

                // 效果2: 基础净化 - 概率清除一个负面效果
                float cleanseChance = ModConfigManager.CONFIG.armorEffects.copperCleanseChance / 100f;
                if (RANDOM.nextFloat() < cleanseChance) {
                    Optional<StatusEffect> effectToRemove = wearer.getActiveStatusEffects().keySet().stream()
                            .filter(effect -> !effect.isBeneficial())
                            .findAny();
                    effectToRemove.ifPresent(effect -> {
                        wearer.removeStatusEffect(effect);
                        if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                                    wearer.getX(), wearer.getY() + 1.5, wearer.getZ(),
                                    5, 0.3, 0.3, 0.3, 0.1);
                        }
                    });
                }
            }

            // 红石盔甲专属效果
            if (primaryMaterial == ModArmorMaterials.REDSTONE) {
                // 效果1: 微弱红石脉冲 - 小概率给予攻击者短暂缓慢
                float slowChance = 0.12f;
                if (RANDOM.nextFloat() < slowChance) {
                    attacker.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            40, 0, false, false, true
                    ));

                    // 修复粒子生成 - 使用DustParticleEffect
                    if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                        // 创建红色粒子效果 (红石颜色)
                        DustParticleEffect dustEffect = new DustParticleEffect(
                                new Vector3f(1.0f, 0.0f, 0.0f), // RGB 红色
                                1.0f // 大小
                        );

                        serverWorld.spawnParticles(
                                dustEffect,
                                attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                                5, // 粒子数量
                                0.3, 0.3, 0.3, // 偏移量
                                0.1 // 速度
                        );
                    }
                }

                // 效果2: 能量反馈 - 被攻击时回复少量耐久
                float repairChance = 0.15f;
                if (RANDOM.nextFloat() < repairChance) {
                    for (ItemStack armor : wearer.getArmorItems()) {
                        if (armor.isDamaged()) {
                            armor.setDamage(armor.getDamage() - 1);
                        }
                    }
                    wearer.getWorld().playSound(null, wearer.getBlockPos(),
                            SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                            SoundCategory.PLAYERS, 0.6f, 1.8f);
                }
            }
        }
        if (primaryMaterial == ModArmorMaterials.COAL) {
            //被攻击时概率点燃攻击者
            float igniteChance = 0.15f + (totalEffectPower * 0.05f); // 基础15% + 每件盔甲增加5%
            if (RANDOM.nextFloat() < igniteChance) {
                int fireTime = 1 + RANDOM.nextInt(3); // 1-3秒燃烧时间
                attacker.setOnFireFor(fireTime * 20); // 转换为游戏刻

                // 粒子效果
                if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.FLAME,
                            attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                            10, 0.3, 0.3, 0.3, 0.05);
                }
            }
        }
    }
    private static void applyContinuousEffects(PlayerEntity player) {
        for (Map.Entry<ModArmorMaterials, List<StatusEffectInstance>> entry : ARMOR_EFFECTS_MAP.entrySet()) {
            ModArmorMaterials material = entry.getKey();
            List<StatusEffectInstance> effects = entry.getValue();

            if (hasFullSet(player, material)) {
                for (StatusEffectInstance effect : effects) {
                    applyEffectWithRefresh(player, effect);
                }
            }
        }
    }

    private static void applyEffectWithRefresh(PlayerEntity player, StatusEffectInstance effect) {
        StatusEffectInstance current = player.getStatusEffect(effect.getEffectType());

        if (current == null || current.getDuration() <= 10) {
            player.addStatusEffect(new StatusEffectInstance(
                    effect.getEffectType(),
                    20,
                    effect.getAmplifier(),
                    false, false, true
            ));
        }
    }

    private static boolean hasFullSet(PlayerEntity player, ModArmorMaterials material) {
        return isArmorOfMaterial(player.getEquippedStack(EquipmentSlot.HEAD), material) &&
                isArmorOfMaterial(player.getEquippedStack(EquipmentSlot.CHEST), material) &&
                isArmorOfMaterial(player.getEquippedStack(EquipmentSlot.LEGS), material) &&
                isArmorOfMaterial(player.getEquippedStack(EquipmentSlot.FEET), material);
    }

    private static boolean isArmorOfMaterial(ItemStack stack, ModArmorMaterials material) {
        return stack.getItem() instanceof ArmorItem armorItem &&
                armorItem.getMaterial() == material;
    }
}
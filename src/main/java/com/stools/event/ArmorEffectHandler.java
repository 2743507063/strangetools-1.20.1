package com.stools.event;

import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import com.stools.item.materials.ModArmorMaterials;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.joml.Vector3f;

import java.util.*;
import net.minecraft.registry.tag.DamageTypeTags;

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
        ARMOR_EFFECTS_MAP.put(ModArmorMaterials.OBSIDIAN, List.of(
                new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, false, false, true),
                new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0, false, false, true)
        ));
    }

    private static final Random RANDOM = new Random();

    public static void register() {
        initArmorEffects();

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills ||
                    !ModConfigManager.CONFIG.armorEffects.enableArmorEffects) {
                return true;
            }

            // 黑曜石盔甲爆炸免疫处理
            if (source.isIn(DamageTypeTags.IS_EXPLOSION) && hasAnyObsidianArmor(entity)) {
                // 消耗每件黑曜石盔甲10%耐久
                for (ItemStack armor : entity.getArmorItems()) {
                    if (isObsidianArmor(armor)) {
                        int maxDamage = armor.getMaxDamage();
                        int damageToApply = maxDamage / 10; // 10%耐久消耗
                        armor.setDamage(armor.getDamage() + damageToApply);

                        if (armor.getDamage() >= maxDamage) {
                            armor.decrement(1); // 盔甲损坏
                        }
                    }
                }

                        // 播放爆炸吸收效果
                if (entity.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                            entity.getX(), entity.getY() + 1.0, entity.getZ(),
                            20, 0.5, 0.5, 0.5, 0.1);
                    serverWorld.playSound(null, entity.getBlockPos(),
                            SoundEvents.BLOCK_ANVIL_PLACE,
                            SoundCategory.PLAYERS, 0.8f, 0.5f);
                }

                return false; // 取消爆炸伤害
            }

            if (source.getAttacker() instanceof LivingEntity attacker) {
                applyReflectiveDamage(entity, attacker, amount);
            }
            return true;
        });

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills ||
                    !ModConfigManager.CONFIG.armorEffects.enableArmorEffects) {
                return;
            }

            if (!(world instanceof ServerWorld)) return;

            for (PlayerEntity player : world.getPlayers()) {
                if (player instanceof ServerPlayerEntity) {
                    applyContinuousEffects(player);
                    applyObsidianFullSetEffects(player);
                }
            }
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
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

    private static void applyReflectiveDamage(LivingEntity wearer, LivingEntity attacker, float damageAmount) {
        // 1. 检查煤炭盔甲
        int coalArmorCount = 0;
        for (ItemStack armor : wearer.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem armorItem &&
                    armorItem.getMaterial() == ModArmorMaterials.COAL) {
                coalArmorCount++;
            }
        }

        // 如果有煤炭盔甲，触发效果
        if (coalArmorCount > 0) {
            float igniteChance = 0.15f + (coalArmorCount * 0.05f); // 基础15% + 每件盔甲增加5%
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

        // 2. 检查是否穿了一整套盔甲（同一种材料，不包括煤炭）
        ModArmorMaterials fullSetMaterial = null;

        // 检查头盔
        ItemStack headStack = wearer.getEquippedStack(EquipmentSlot.HEAD);
        ModArmorMaterials headMaterial = getArmorMaterial(headStack);

        // 检查胸甲
        ItemStack chestStack = wearer.getEquippedStack(EquipmentSlot.CHEST);
        ModArmorMaterials chestMaterial = getArmorMaterial(chestStack);

        // 检查护腿
        ItemStack legsStack = wearer.getEquippedStack(EquipmentSlot.LEGS);
        ModArmorMaterials legsMaterial = getArmorMaterial(legsStack);

        // 检查靴子
        ItemStack feetStack = wearer.getEquippedStack(EquipmentSlot.FEET);
        ModArmorMaterials feetMaterial = getArmorMaterial(feetStack);

        // 只有当四件都是同一种材料时才算整套（排除煤炭）
        if (headMaterial != null && headMaterial != ModArmorMaterials.COAL &&
                headMaterial == chestMaterial &&
                headMaterial == legsMaterial &&
                headMaterial == feetMaterial) {
            fullSetMaterial = headMaterial;
        }

        // 没有整套盔甲，直接返回
        if (fullSetMaterial == null) {
            return;
        }

        // 3. 根据整套盔甲材料触发对应效果
        switch (fullSetMaterial) {
            case EMERALD:
                float dropChance = ModConfigManager.CONFIG.armorEffects.emeraldDropBaseChance / 100f;
                dropChance += damageAmount * 0.01f;
                dropChance = Math.min(dropChance, 0.5f);

                if (wearer.getRandom().nextFloat() < dropChance) {
                    int dropCount = Math.max(1,
                            (int) (damageAmount * ModConfigManager.CONFIG.armorEffects.emeraldDamageMultiplier));
                    dropCount = Math.min(dropCount, 5);

                    if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                        ItemEntity emeraldDrop = new ItemEntity(
                                serverWorld,
                                wearer.getX(),
                                wearer.getY() + 0.5,
                                wearer.getZ(),
                                new ItemStack(Items.EMERALD, dropCount)
                        );
                        serverWorld.spawnEntity(emeraldDrop);

                        serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                                wearer.getX(), wearer.getY() + 1.5, wearer.getZ(),
                                15, 0.5, 0.5, 0.5, 0.1);

                        serverWorld.playSound(null, wearer.getBlockPos(),
                                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                                SoundCategory.PLAYERS, 0.8f, 1.2f);
                    }
                }
                break;

            case LAPIS:
                // 青金石盔甲专属效果：经验窃取
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
                break;

            case COPPER:
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
                break;

            case REDSTONE:
                // 效果1: 微弱红石脉冲 - 小概率给予攻击者短暂缓慢
                float slowChance = 0.12f;
                if (RANDOM.nextFloat() < slowChance) {
                    attacker.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            40, 0, false, false, true
                    ));

                    if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                        DustParticleEffect dustEffect = new DustParticleEffect(
                                new Vector3f(1.0f, 0.0f, 0.0f),
                                1.0f
                        );

                        serverWorld.spawnParticles(
                                dustEffect,
                                attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                                5, 0.3, 0.3, 0.3, 0.1
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
                break;

            case OBSIDIAN:
                // 黑曜石盔甲专属效果：黑曜石碎片反射
                float reflectChance = 0.15f;
                if (RANDOM.nextFloat() < reflectChance) {
                    // 造成3点伤害
                    attacker.damage(attacker.getDamageSources().thorns(wearer), 3.0f);

                    // 击退效果
                    attacker.takeKnockback(0.5f,
                            wearer.getX() - attacker.getX(),
                            wearer.getZ() - attacker.getZ());

                    // 粒子效果和音效
                    if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.CRIT,
                                attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                                10, 0.3, 0.3, 0.3, 0.1);
                        serverWorld.playSound(null, attacker.getBlockPos(),
                                SoundEvents.BLOCK_GLASS_BREAK,
                                SoundCategory.PLAYERS, 0.8f, 1.2f);
                    }
                }
                break;
        }
    }

    // 辅助方法：从盔甲获取材料
    private static ModArmorMaterials getArmorMaterial(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem &&
                armorItem.getMaterial() instanceof ModArmorMaterials mat) {
            return mat;
        }
        return null;
    }

    // 检查是否穿戴任意黑曜石盔甲
    private static boolean hasAnyObsidianArmor(LivingEntity entity) {
        for (ItemStack armor : entity.getArmorItems()) {
            if (isObsidianArmor(armor)) {
                return true;
            }
        }
        return false;
    }

    // 检查单件盔甲是否为黑曜石材质
    private static boolean isObsidianArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armorItem &&
                armorItem.getMaterial() == ModArmorMaterials.OBSIDIAN;
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

    // 应用黑曜石全套特殊效果
    private static void applyObsidianFullSetEffects(PlayerEntity player) {
        if (hasFullSet(player, ModArmorMaterials.OBSIDIAN)) {
            // 这里可以添加黑曜石全套的特殊效果
            // 例如每5秒回复1点耐久
            if (player.getWorld().getTime() % 100 == 0) { // 每5秒
                for (ItemStack armor : player.getArmorItems()) {
                    if (armor.isDamaged() && isObsidianArmor(armor)) {
                        armor.setDamage(armor.getDamage() - 1);
                    }
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
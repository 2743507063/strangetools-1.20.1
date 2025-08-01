package com.stools.event;

import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import com.stools.item.materials.ModArmorMaterials;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ArmorEffectHandler {
    private static final Map<ModArmorMaterials, List<StatusEffectInstance>> ARMOR_EFFECTS_MAP =
            new EnumMap<>(ModArmorMaterials.class);

    static {
        ARMOR_EFFECTS_MAP.put(ModArmorMaterials.EMERALD, List.of(
                new StatusEffectInstance(StatusEffects.LUCK, 100, 1, false, false, true)
        ));
        ARMOR_EFFECTS_MAP.put(ModArmorMaterials.LAPIS, List.of(
                new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, false, false, true)// 伤害抗性
        ));
    }

    // 添加静态Random实例
    private static final Random RANDOM = new Random();

    public static void register() {
        // 初始化效果映射
        initArmorEffects();

        // 注册伤害反射事件
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!checkConfig()) return true;
            if (source.getAttacker() instanceof LivingEntity attacker) {
                applyReflectiveDamage(entity, attacker);
            }
            return true;
        });

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (!checkConfig()) return;
            if (!(world instanceof ServerWorld)) return;

            for (PlayerEntity player : world.getPlayers()) {
                if (player instanceof ServerPlayerEntity) {
                    applyContinuousEffects(player);
                }
            }
        });

        // 注册维度切换事件（确保效果正确应用）
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            if (!checkConfig()) return;
            applyContinuousEffects(player);
        });
    }

    private static boolean checkConfig() {
        return ModConfigManager.CONFIG.toolEffects.enableToolSkills &&
                ModConfigManager.CONFIG.armorEffects.enableArmorEffects;
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
                primaryMaterial = mat; // 记录主要材料
            }
        }

        if (totalEffectPower > 0) {
            float reflectChance = ModConfigManager.CONFIG.armorEffects.armorReflectChance / 100f;
            if (wearer.getRandom().nextFloat() < reflectChance) {
                float reflectDamage = ModConfigManager.CONFIG.armorEffects.armorReflectDamage;
                attacker.damage(wearer.getDamageSources().thorns(wearer), reflectDamage);
                wearer.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING, 40, 0, true, false));
            }
            // 青金石盔甲专属效果：经验窃取
            if (primaryMaterial == ModArmorMaterials.LAPIS) {
                float xpStealChance = 0.3f; // 30%几率
                if (wearer.getRandom().nextFloat() < xpStealChance) {
                    int stolenXp = 1 + RANDOM.nextInt(3); // 使用静态RANDOM实例

                    // 使用getWorld()方法
                    if (attacker instanceof PlayerEntity) {
                        ((PlayerEntity) attacker).addExperience(-stolenXp); // 从攻击者偷取经验
                    }
                    if (wearer instanceof PlayerEntity) {
                        ((PlayerEntity) wearer).addExperience(stolenXp); // 给穿戴者经验

                        // 粒子效果 - 使用getWorld()获取世界
                        if (wearer.getWorld() instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                                    wearer.getX(), wearer.getY() + 1.5, wearer.getZ(),
                                    10, 0.5, 0.5, 0.5, 0.1);
                        }
                    }
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
                    200,  // 延长持续时间减少检查频率
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
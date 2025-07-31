package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class PotionToolUseEvent {

    private static final int DURABILITY_COST = 130;
    private static final List<StatusEffect> ALL_EFFECTS = Arrays.asList(
            StatusEffects.SPEED,
            StatusEffects.SLOWNESS,
            StatusEffects.HASTE,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.STRENGTH,
            StatusEffects.INSTANT_HEALTH,
            StatusEffects.INSTANT_DAMAGE,
            StatusEffects.JUMP_BOOST,
            StatusEffects.NAUSEA,
            StatusEffects.REGENERATION,
            StatusEffects.RESISTANCE,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.WATER_BREATHING,
            StatusEffects.INVISIBILITY,
            StatusEffects.BLINDNESS,
            StatusEffects.NIGHT_VISION,
            StatusEffects.HUNGER,
            StatusEffects.WEAKNESS,
            StatusEffects.POISON,
            StatusEffects.WITHER,
            StatusEffects.HEALTH_BOOST,
            StatusEffects.ABSORPTION,
            StatusEffects.SATURATION,
            StatusEffects.GLOWING,
            StatusEffects.LEVITATION,
            StatusEffects.LUCK,
            StatusEffects.UNLUCK,
            StatusEffects.SLOW_FALLING,
            StatusEffects.CONDUIT_POWER,
            StatusEffects.DOLPHINS_GRACE,
            StatusEffects.BAD_OMEN,
            StatusEffects.HERO_OF_THE_VILLAGE
    );

    // 特殊效果组（更强大但持续时间短）
    private static final List<StatusEffect> SPECIAL_EFFECTS = Arrays.asList(
            StatusEffects.ABSORPTION,
            StatusEffects.RESISTANCE,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.WATER_BREATHING,
            StatusEffects.INVISIBILITY,
            StatusEffects.NIGHT_VISION,
            StatusEffects.CONDUIT_POWER,
            StatusEffects.DOLPHINS_GRACE
    );

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.POTION) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));

                        // 10%概率获得特殊效果组
                        if (world.random.nextFloat() < 0.1f) {
                            applySpecialEffectSet(player, world);
                        } else {
                            applyRandomEffect(player, world);
                        }
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void applyRandomEffect(PlayerEntity player, World world) {
        Random random = world.getRandom();
        boolean isPositive = random.nextFloat() > 0.3f; // 70% 正面效果

        // 随机选择效果
        StatusEffect effect = ALL_EFFECTS.get(random.nextInt(ALL_EFFECTS.size()));

        // 根据效果类型调整参数
        int duration = (isPositive ? 600 : 300) + random.nextInt(300); // 30-45秒
        int amplifier = isPositive ? random.nextInt(2) : random.nextInt(3);

        // 即时效果特殊处理
        if (effect == StatusEffects.INSTANT_HEALTH || effect == StatusEffects.INSTANT_DAMAGE) {
            duration = 1;
            amplifier = 0;
        }

        player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));

        // 播放音效
        world.playSound(null, player.getBlockPos(),
                isPositive ? SoundEvents.ENTITY_WITCH_CELEBRATE : SoundEvents.ENTITY_WITCH_AMBIENT,
                SoundCategory.PLAYERS, 1.0f, random.nextFloat() * 0.4f + 0.8f);
    }

    private static void applySpecialEffectSet(PlayerEntity player, World world) {
        Random random = world.getRandom();

        // 随机选择3-5个特殊效果
        int effectCount = 3 + random.nextInt(3);
        for (int i = 0; i < effectCount; i++) {
            StatusEffect effect = SPECIAL_EFFECTS.get(random.nextInt(SPECIAL_EFFECTS.size()));
            int duration = 200 + random.nextInt(100); // 10-15秒
            int amplifier = random.nextInt(2);

            player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));
        }

        // 播放特殊音效
        world.playSound(null, player.getBlockPos(),
                SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
                SoundCategory.PLAYERS, 1.0f, 0.8f);

        // 粒子效果
        if (world.isClient()) {
            for (int i = 0; i < 30; i++) {
                double x = player.getX() + (random.nextDouble() - 0.5) * 2.0;
                double y = player.getY() + random.nextDouble() * 2.0;
                double z = player.getZ() + (random.nextDouble() - 0.5) * 2.0;

                world.addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 0.1, 0);
            }
        }
    }
}
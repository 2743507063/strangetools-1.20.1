package com.stools.event;

import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import com.stools.item.materials.ModToolMaterials;

import java.util.Random;

public class ToolEffectHandler {
    private static final Random random = new Random();

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            // 检查工具效果总开关
            if (!ModConfigManager.CONFIG.toolEffects.enableToolEffects) {
                return ActionResult.PASS;
            }

            if (!world.isClient() && entity instanceof LivingEntity) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.getItem() instanceof ToolItem toolItem &&
                        toolItem.getMaterial() instanceof ModToolMaterials material) {

                    applyToolEffect(material, player, (LivingEntity) entity, world);
                }
            }
            return ActionResult.PASS;
        });
    }

    private static void applyToolEffect(ModToolMaterials material,
                                        PlayerEntity player,
                                        LivingEntity target,
                                        World world) {
        switch (material) {
            case COPPER:
                // 使用配置中的铜点燃概率
                float copperChance = ModConfigManager.CONFIG.toolEffects.copperIgniteChance / 100f;
                if (random.nextFloat() < copperChance) {
                    target.setOnFireFor(2);
                    world.playSound(null, target.getBlockPos(),
                            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                            SoundCategory.PLAYERS, 0.8f, 1.2f);
                }
                break;

            case EMERALD:
                // 使用配置中的绿宝石掉落概率
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
                // 使用配置中的石英额外伤害
                float quartzDamage = ModConfigManager.CONFIG.toolEffects.quartzExtraDamage;
                target.damage(target.getDamageSources().magic(), quartzDamage);
                break;

            case COAL:
                target.setOnFireFor(3);
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
                // 使用配置中的腐肉饥饿概率
                float rottenChance = ModConfigManager.CONFIG.toolEffects.rottenFleshHungerChance / 100f;
                if (random.nextFloat() < rottenChance) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.HUNGER, 100, 0, true, false));
                }
                break;
            case GLOWSTONE:
                target.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING,
                        100,
                        0,
                        false,
                        true
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
        }
    }
}
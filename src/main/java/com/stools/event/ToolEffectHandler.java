package com.stools.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import com.stools.item.ModToolMaterials;

import java.util.Random;

public class ToolEffectHandler {
    private static final Random random = new Random();

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient() && entity instanceof LivingEntity) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.getItem() instanceof ToolItem toolItem) {
                    ModToolMaterials material = (ModToolMaterials) toolItem.getMaterial();
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
                // 导电效果：小范围电击
                if (random.nextFloat() < 0.3) {
                    target.setOnFireFor(2);
                    world.playSound(null, target.getBlockPos(),
                            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                            SoundCategory.PLAYERS, 0.8f, 1.2f);
                }
                break;

            case EMERALD:
                // 幸运效果：额外掉落物
                if (random.nextFloat() < 0.25) {
                    target.dropStack(new ItemStack(Items.EMERALD, 1));
                }
                break;

            case LAPIS:
                // 经验效果：增加经验
                player.addExperience(1 + random.nextInt(3));
                break;

            case REDSTONE:
                // 红石能量：短暂提升挖掘速度
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HASTE, 100, 1, true, false));
                break;

            case QUARTZ:
                // 精准打击：额外伤害
                target.damage(target.getDamageSources().magic(), 2.0f);
                break;

            case COAL:
                // 点燃效果
                target.setOnFireFor(4);
                break;

            case CAKE:
                // 蛋糕恢复：攻击时恢复饥饿值
                if (player.getHungerManager().getFoodLevel() < 20) {
                    player.getHungerManager().add(1, 0.1f);
                }
                break;

            case OBSIDIAN:
                // 黑曜石震荡：击退效果
                target.takeKnockback(0.5f,
                        player.getX() - target.getX(),
                        player.getZ() - target.getZ());
                break;

            case PRISMARINE:
                // 海洋祝福：水下呼吸
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WATER_BREATHING, 200, 0, true, false));
                break;

            case ROTTEN_FLESH:
                // 腐化效果：给予饥饿
                if (random.nextFloat() < 0.4) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.HUNGER, 100, 0, true, false));
                }
                break;
        }
    }
}
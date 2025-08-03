package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class PoPotatoToolUseEvent {
    private static final Random random = new Random();
    private static final int POISON_DURATION = 80; // 4秒 (20 ticks/秒 * 4)

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // 检查总开关是否开启
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }

            // 检查是否为潜行状态和毒土豆工具
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.POISONOUS_POTATO) {
                    int currentDamage = stack.getDamage();
                    int maxDamage = stack.getMaxDamage();
                    int remainingDurability = maxDamage - currentDamage;
                    int consumeAmount = Math.min(remainingDurability, 50);

                    if (!world.isClient()) {
                        // 消耗耐久度
                        stack.damage(consumeAmount, player, e -> e.sendToolBreakStatus(hand));

                        // 恢复饥饿度
                        HungerManager hungerManager = player.getHungerManager();
                        hungerManager.add(2, 1.2f);

                        // 播放音效
                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_GENERIC_EAT,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);

                        // 应用中毒效果
                        applyPoisonEffect(player, world);

                        // 耐久耗尽时掉落木棍
                        if (stack.getDamage() >= stack.getMaxDamage()) {
                            ItemStack stickStack = new ItemStack(Items.STICK);
                            if (!player.getInventory().insertStack(stickStack)) {
                                player.dropItem(stickStack, false);
                            }
                        }
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void applyPoisonEffect(PlayerEntity player, World world) {
        // 获取配置中的中毒概率
        float poisonChance = ModConfigManager.CONFIG.toolEffects.poisonousPotatoEatPoisonChance / 100f;

        // 根据概率决定是否中毒
        if (random.nextFloat() < poisonChance) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.POISON,
                    POISON_DURATION, // 4秒
                    0,              // 等级I
                    false,
                    true
            ));
        }
    }
}
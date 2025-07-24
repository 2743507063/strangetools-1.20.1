package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;

public class BlazePowderToolUseEvent {

    // 每次使用技能消耗的耐久度
    private static final int DURABILITY_COST_SKILL = 20; // 消耗20点耐久

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.BLAZE_POWDER) {
                    if (!world.isClient()) {
                        // 检查工具是否有足够耐久度
                        if (stack.getDamage()  + DURABILITY_COST_SKILL >= stack.getMaxDamage()) {
                            return TypedActionResult.fail(stack);
                        }

                        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(5), e -> e != player)) {
                            entity.setOnFireFor(5);
                            entity.damage(player.getDamageSources().playerAttack(player), 5.0f);
                        }
                        stack.damage(DURABILITY_COST_SKILL, player, p -> p.sendToolBreakStatus(hand));

                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ITEM_FIRECHARGE_USE,
                                SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }
}
package com.stools.event.glass;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;

public class GlassToolPassiveEvent {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof PlayerEntity player)) return true;
            ItemStack stack = player.getMainHandStack();
            if (!(stack.getItem() instanceof ToolItem tool) || tool.getMaterial() != ModToolMaterials.GLASS) return true;
            if (!(source.getAttacker() instanceof LivingEntity attacker)) return true;
            if (player.getRandom().nextFloat() >= 0.3F) return true; // 30%概率

            // 造成2点无视护甲伤害
            DamageSource magic = player.getDamageSources().magic();
            attacker.damage(magic, 2.0f);

            // 损失20点耐久
            stack.damage(20, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            // 可加音效、粒子等

            return true;
        });
    }
}
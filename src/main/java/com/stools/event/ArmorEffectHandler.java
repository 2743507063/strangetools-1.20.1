package com.stools.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import com.stools.item.ModArmorMaterials;

public class ArmorEffectHandler {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (source.getAttacker() instanceof LivingEntity attacker) {
                applyArmorEffects(entity, attacker);
            }
            return true;
        });
    }

    private static void applyArmorEffects(LivingEntity wearer, LivingEntity attacker) {
        float totalEffectPower = 0;

        for (ItemStack armor : wearer.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem armorItem &&
                    armorItem.getMaterial() instanceof ModArmorMaterials mat) {
                totalEffectPower += mat.getEffectPower();
            }
        }

        if (totalEffectPower > 0) {
            if (wearer.getRandom().nextFloat() < totalEffectPower) {
                attacker.damage(wearer.getDamageSources().thorns(wearer), 2.0f);
                wearer.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING, 40, 0, true, false));
            }
        }
    }
}
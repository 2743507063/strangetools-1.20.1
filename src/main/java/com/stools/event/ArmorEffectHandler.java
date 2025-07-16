package com.stools.event;

import com.stools.config.ModConfigManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import com.stools.item.materials.ModArmorMaterials;

public class ArmorEffectHandler {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!ModConfigManager.CONFIG.armorEffects.enableArmorEffects) {
                return true;
            }

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
            float reflectChance = ModConfigManager.CONFIG.armorEffects.armorReflectChance / 100f;
            if (wearer.getRandom().nextFloat() < reflectChance) {
                float reflectDamage = ModConfigManager.CONFIG.armorEffects.armorReflectDamage;
                attacker.damage(wearer.getDamageSources().thorns(wearer), reflectDamage);

                wearer.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING, 40, 0, true, false));
            }
        }
    }
}
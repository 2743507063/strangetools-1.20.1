package com.stools.mixin;

import com.stools.item.custom.MaceItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F"
            ),
            ordinal = 0
    )
    private float modifyAttackDamage(float baseDamage, Entity target) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        if (stack.getItem() instanceof MaceItem) {
            MaceItem mace = (MaceItem) stack.getItem();
            // 只添加额外伤害，不重复添加基础伤害
            return baseDamage + mace.getBonusAttackDamage(player);
        }

        return baseDamage;
    }
}
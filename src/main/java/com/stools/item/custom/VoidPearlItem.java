package com.stools.item.custom;

import com.stools.entity.VoidPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class VoidPearlItem extends Item {
    private static final int DURABILITY_COST = 1; // 每次使用消耗1点耐久

    public VoidPearlItem(Settings settings) {
        // 设置最大耐久度
        super(settings.maxDamage(64));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_ENDER_PEARL_THROW,
                SoundCategory.NEUTRAL,
                0.7F, //音量
                0.5F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        user.getItemCooldownManager().set(this, 20);

        if (!world.isClient) {
            VoidPearlEntity voidPearlEntity = new VoidPearlEntity(world, user);
            voidPearlEntity.setItem(itemStack);

            // 投掷参数
            voidPearlEntity.setVelocity(
                    user,
                    user.getPitch(),
                    user.getYaw(),
                    0.0F,
                    1.8F, // 初始速度
                    1.0F
            );

            world.spawnEntity(voidPearlEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!user.getAbilities().creativeMode) {
            itemStack.damage(DURABILITY_COST, user, entity -> entity.sendToolBreakStatus(hand));
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
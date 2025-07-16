package com.stools.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.stools.sound.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MaceItem extends Item {
    private static final UUID ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER_ID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private static final Random RANDOM = new Random();

    private static final int BASE_DAMAGE = 6;
    private static final float ATTACK_SPEED = -3.4F;
    private static final float KNOCKBACK_RANGE = 3.5F;
    private static final float KNOCKBACK_POWER = 0.7F;

    public MaceItem(Settings settings) {
        super(settings.maxDamage(250));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));

        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            World world = player.getWorld();

            if (shouldDealAdditionalDamage(player)) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;

                    // 播放音效
                    SoundEvent sound;
                    if (target.isOnGround()) {
                        if (player.fallDistance > 5.0F) {
                            sound = ModSoundEvents.MACE_HEAVY_SMASH_GROUND;
                        } else {
                            sound = ModSoundEvents.MACE_SMASH_GROUND;
                        }
                    } else {
                        sound = ModSoundEvents.MACE_SMASH_AIR;
                    }

                    serverWorld.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            sound,
                            SoundCategory.PLAYERS,
                            1.0F,
                            RANDOM.nextFloat() * 0.2F + 0.9F // 轻微随机音高
                    );

                    // 击退效果
                    knockbackNearbyEntities(serverWorld, player, target);

                    // 防止玩家受到摔落伤害
                    player.fallDistance = 0;
                }
            }
        }
        return true;
    }

    private static void knockbackNearbyEntities(World world, PlayerEntity player, Entity attacked) {
        Box box = attacked.getBoundingBox().expand(KNOCKBACK_RANGE);
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, e ->
                e != player && e != attacked &&
                        !(e instanceof ArmorStandEntity && ((ArmorStandEntity)e).isMarker()) &&
                        !player.isTeammate(e)
        );

        for (LivingEntity entity : entities) {
            Vec3d direction = entity.getPos().subtract(attacked.getPos());
            double distance = direction.length();
            double strength = (KNOCKBACK_RANGE - distance) * KNOCKBACK_POWER *
                    (player.fallDistance > 5.0F ? 1.5 : 1.0) *
                    (1.0 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));

            if (strength > 0) {
                entity.addVelocity(direction.normalize().x * strength, 0.7, direction.normalize().z * strength);
                if (entity instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
                }
            }
        }
    }

    public static boolean shouldDealAdditionalDamage(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
    }

    public float getBonusAttackDamage(LivingEntity attacker) {
        if (!shouldDealAdditionalDamage(attacker)) {
            return 0.0F;
        }

        float fallDistance = attacker.fallDistance;
        //额外伤害 = 下落高度 × 2.5
        float bonusDamage = fallDistance * 2.5f;

        // 附魔效果
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            int smashLevel = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, player.getMainHandStack());
            bonusDamage += smashLevel * 1.0f; // 每级附魔增加1点伤害
        }

        return bonusDamage;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.BLAZE_ROD);
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", BASE_DAMAGE, EntityAttributeModifier.Operation.ADDITION));
            modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                    new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", ATTACK_SPEED, EntityAttributeModifier.Operation.ADDITION));
        }
        return modifiers;
    }
}
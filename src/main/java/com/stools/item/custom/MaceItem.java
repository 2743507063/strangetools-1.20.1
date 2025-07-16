package com.stools.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.stools.enchantment.ModEnchantments;
import com.stools.item.materials.ModMaceMaterials;
import com.stools.sound.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
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

    //属性
    private static final int BASE_DAMAGE = 6; // 基础伤害
    private static final float ATTACK_SPEED = -3.4F; // 攻击速度 = 4.0 - 3.4 = 0.6 (1.65秒冷却)
    private static final float KNOCKBACK_RANGE = 3.5F;
    private static final float KNOCKBACK_POWER = 0.7F;

    private final ModMaceMaterials material;

    public MaceItem(ModMaceMaterials material, Settings settings) {
        super(settings);
        this.material = material;
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

                    SoundEvent sound;
                    boolean isHeavySmash = false;

                    if (target.isOnGround()) {
                        if (player.fallDistance > 5.0F) {
                            sound = ModSoundEvents.MACE_HEAVY_SMASH_GROUND;
                            isHeavySmash = true;

                            BlockPos groundPos = target.getBlockPos().down();
                            BlockState groundState = serverWorld.getBlockState(groundPos);

                            serverWorld.spawnParticles(
                                    new BlockStateParticleEffect(ParticleTypes.BLOCK, groundState),
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    50,
                                    0.5,
                                    0.1,
                                    0.5,
                                    0.2
                            );
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
                            RANDOM.nextFloat() * 0.2F + 0.9F
                    );

                    // 击退效果
                    knockbackNearbyEntities(serverWorld, player, target, isHeavySmash);

                    player.addVelocity(0, 0.15, 0);
                    if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(
                                new EntityVelocityUpdateS2CPacket(player)
                        );
                    }

                    // 防止玩家受到摔落伤害
                    player.fallDistance = 0;
                }
            }
        }
        return true;
    }

    private static void knockbackNearbyEntities(World world, PlayerEntity player, Entity attacked, boolean isHeavySmash) {
        Box box = attacked.getBoundingBox().expand(KNOCKBACK_RANGE);
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, e ->
                e != player && e != attacked &&
                        !(e instanceof ArmorStandEntity && ((ArmorStandEntity)e).isMarker()) &&
                        !player.isTeammate(e)
        );

        for (LivingEntity entity : entities) {
            Vec3d direction = entity.getPos().subtract(attacked.getPos());
            double distance = direction.length();
            double strength = (KNOCKBACK_RANGE - distance) * KNOCKBACK_POWER;

            // 重击时击退翻倍
            if (isHeavySmash) {
                strength *= 2.0;
            }

            // 考虑击退抗性
            strength *= (1.0 - entity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));

            if (strength > 0) {
                entity.addVelocity(direction.normalize().x * strength, 0.2, direction.normalize().z * strength);
                if (entity instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
                }
            }
        }
    }

    public static boolean shouldDealAdditionalDamage(LivingEntity attacker) {
        boolean hasSlowFalling = attacker.hasStatusEffect(StatusEffects.SLOW_FALLING);
        boolean isGliding = attacker.isFallFlying();

        return attacker.fallDistance > 1.5F && !hasSlowFalling && !isGliding;
    }

    public float getBonusAttackDamage(LivingEntity attacker) {
        if (!shouldDealAdditionalDamage(attacker)) {
            return 0.0F;
        }

        float fallDistance = attacker.fallDistance;
        float bonusDamage = 0.0F;

        // 分段计算伤害
        if (fallDistance <= 3.0F) {
            bonusDamage = fallDistance * 4.0F;
        } else if (fallDistance <= 8.0F) {
            bonusDamage = 12.0F + (fallDistance - 3.0F) * 2.0F;
        } else {
            bonusDamage = 22.0F + (fallDistance - 8.0F) * 1.0F;
        }

        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            int densityLevel = EnchantmentHelper.getLevel(ModEnchantments.DENSITY, player.getMainHandStack());
            bonusDamage += densityLevel * 2.0f; // 每级附魔增加2点伤害
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
    public int getEnchantability() {
        return material.getEnchantability();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return material.getRepairIngredient().test(ingredient);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier",
                            material.getBaseDamage(), EntityAttributeModifier.Operation.ADDITION));
            modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                    new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier",
                            ATTACK_SPEED, EntityAttributeModifier.Operation.ADDITION));
        }
        return modifiers;
    }
}
package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.Set;

public class GlowstoneToolUseEvent {

    private static final Set<EntityType<?>> UNDEAD_ENTITIES = Set.of(
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.DROWNED,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.STRAY,
            EntityType.HUSK,
            EntityType.ZOGLIN,
            EntityType.PHANTOM
    );

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // 添加全局效果开关检查
            if (!ModConfigManager.CONFIG.general.enableAllEffects) {
                return TypedActionResult.pass(stack);
            }

            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }

            // 添加萤石效果开关检查
            if (!ModConfigManager.CONFIG.glowstoneEffects.enableEffects) {
                return TypedActionResult.pass(stack);
            }

            if (stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.GLOWSTONE) {
                    if (player.isSneaking()) {
                        if (!world.isClient()) {
                            stack.damage(10, player, e -> e.sendToolBreakStatus(hand));

                            player.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.SPEED,
                                    100,
                                    1,
                                    false,
                                    false
                            ));

                            BlockPos pos = player.getBlockPos();
                            Box area = new Box(pos.add(-8, -3, -8), pos.add(8, 3, 8));

                            world.getEntitiesByClass(
                                    MobEntity.class,
                                    area,
                                    entity -> UNDEAD_ENTITIES.contains(entity.getType())
                            ).forEach(entity -> {
                                entity.addStatusEffect(new StatusEffectInstance(
                                        StatusEffects.GLOWING,
                                        200,
                                        0,
                                        false,
                                        true
                                ));

                                entity.damage(entity.getDamageSources().magic(), 2.0f);

                                Vec3d awayDirection = entity.getPos().subtract(player.getPos()).normalize();
                                Vec3d fleePos = entity.getPos().add(awayDirection.multiply(15)); // 逃到15格外
                                entity.getNavigation().startMovingTo(fleePos.x, fleePos.y, fleePos.z, 1.5);
                            });

                            world.playSound(null, pos,
                                    SoundEvents.BLOCK_BEACON_ACTIVATE,
                                    SoundCategory.PLAYERS, 1.0f, 1.0f);
                        }

                        if (world.isClient()) {
                            for (int i = 0; i < 30; i++) {
                                double x = player.getX() + (world.random.nextDouble() - 0.5) * 5;
                                double y = player.getY() + world.random.nextDouble() * 2;
                                double z = player.getZ() + (world.random.nextDouble() - 0.5) * 5;
                                world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0, 0);
                            }
                        }

                        return TypedActionResult.success(stack);
                    }
                }
            }
            return TypedActionResult.pass(stack);
        });
    }
}
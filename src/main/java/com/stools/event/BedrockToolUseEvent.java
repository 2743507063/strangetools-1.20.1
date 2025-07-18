package com.stools.event;

import com.stools.item.materials.ModToolMaterials;
import com.stools.sound.ModSoundEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BedrockToolUseEvent {

    private static final int DURABILITY_COST = 100;

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (player.isSneaking() && stack.getItem() instanceof ToolItem toolItem) {
                if (toolItem.getMaterial() == ModToolMaterials.BEDROCK) {
                    if (stack.getDamage() + DURABILITY_COST >= stack.getMaxDamage()) {
                        return TypedActionResult.fail(stack);
                    }

                    if (!world.isClient()) {
                        stack.damage(DURABILITY_COST, player, e -> e.sendToolBreakStatus(hand));

                        activateBedrockAbility(player, world);
                    }

                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static void activateBedrockAbility(PlayerEntity player, World world) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE,
                200,
                4,
                false,
                true
        ));

        BlockPos pos = player.getBlockPos();
        Box area = new Box(pos.add(-5, -2, -5), pos.add(5, 3, 5));

        DamageSources damageSources = world.getDamageSources();

        for (Entity entity : world.getOtherEntities(player, area, e -> e instanceof LivingEntity)) {
            entity.damage(damageSources.playerAttack(player), 5.0f);

            Vec3d awayDirection = entity.getPos().subtract(player.getPos()).normalize();
            entity.addVelocity(awayDirection.x * 2.0, 0.5, awayDirection.z * 2.0);
        }

        world.playSound(null, pos,
                ModSoundEvents.MACE_SMASH_AIR,
                SoundCategory.PLAYERS, 1.0f, 0.8f);

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.BEDROCK.getDefaultState()),
                    player.getX(),
                    player.getY() + 1.0,
                    player.getZ(),
                    50,
                    3.0,
                    2.0,
                    3.0,
                    0.1
            );
        }
    }
}
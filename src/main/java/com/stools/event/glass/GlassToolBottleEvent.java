package com.stools.event.glass;

import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GlassToolBottleEvent {
    private static final int WATER_PLACE_DAMAGE = 5; // 放置水消耗的耐久

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // 只允许玻璃工具
            if (!(stack.getItem() instanceof ToolItem tool) || tool.getMaterial() != ModToolMaterials.GLASS) {
                return TypedActionResult.pass(stack);
            }

            NbtCompound tag = stack.getOrCreateNbt();
            String content = tag.getString("BottleContent");

            // ====================
            // 放置水功能
            // ====================
            if ("water".equals(content)) {
                BlockHitResult hitResult = raycast(world, player, RaycastContext.FluidHandling.ANY);
                if (hitResult.getType() != HitResult.Type.BLOCK) {
                    return TypedActionResult.pass(stack);
                }

                BlockPos targetPos = hitResult.getBlockPos();
                Direction side = hitResult.getSide();
                BlockPos placementPos = targetPos.offset(side);

                // 检查目标位置是否已经是液体
                if (world.getFluidState(targetPos).isStill() || world.getFluidState(placementPos).isStill()) {
                    return TypedActionResult.pass(stack);
                }

                // 检查位置是否可放置
                if (!world.getBlockState(placementPos).isReplaceable()) {
                    return TypedActionResult.pass(stack);
                }

                if (stack.getDamage() + WATER_PLACE_DAMAGE >= stack.getMaxDamage()) {
                    return TypedActionResult.fail(stack);
                }

                if (!world.isClient()) {
                    world.setBlockState(placementPos, Blocks.WATER.getDefaultState());
                    stack.damage(WATER_PLACE_DAMAGE, player, p -> p.sendToolBreakStatus(hand));
                    tag.putString("BottleContent", "empty");
                    world.playSound(null, placementPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                return TypedActionResult.success(stack);
            }

            // ====================
            // 装水功能
            // ====================
            if ("empty".equals(content) || content.isEmpty()) {
                BlockHitResult hitResult = raycast(world, player, RaycastContext.FluidHandling.SOURCE_ONLY);
                if (hitResult.getType() != HitResult.Type.BLOCK) {
                    return TypedActionResult.pass(stack);
                }

                BlockPos blockPos = hitResult.getBlockPos();
                if (!world.canPlayerModifyAt(player, blockPos)) {
                    return TypedActionResult.pass(stack);
                }

                // 只处理水
                if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    if (!world.isClient()) {
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());

                        tag.putString("BottleContent", "water");
                        stack.setNbt(tag);
                        player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        world.emitGameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                    }
                    return TypedActionResult.success(stack, world.isClient());
                }
            }

            return TypedActionResult.pass(stack);
        });
    }

    // 射线检测方法
    private static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        double reach = player.isCreative() ? 5.0D : 4.5D;
        return world.raycast(new RaycastContext(
                player.getCameraPosVec(1.0F),
                player.getCameraPosVec(1.0F).add(player.getRotationVec(1.0F).multiply(reach)),
                RaycastContext.ShapeType.OUTLINE,
                fluidHandling,
                player
        ));
    }
}
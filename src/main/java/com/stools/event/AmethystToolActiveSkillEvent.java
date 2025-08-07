
package com.stools.event;

import com.stools.config.ModConfigManager;
import com.stools.item.materials.ModToolMaterials;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class AmethystToolActiveSkillEvent {
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // 检查配置和条件
            if (!ModConfigManager.CONFIG.general.enableAllEffects) {
                return TypedActionResult.pass(stack);
            }
            if (!ModConfigManager.CONFIG.toolEffects.enableToolSkills) {
                return TypedActionResult.pass(stack);
            }
            if (!ModConfigManager.CONFIG.amethystEffects.enableEffects) {
                return TypedActionResult.pass(stack);
            }
            if (!(player.isSneaking() && stack.getItem() instanceof ToolItem tool &&
                    tool.getMaterial() == ModToolMaterials.AMETHYST)) {
                return TypedActionResult.pass(stack);
            }

            // 获取配置值
            float range = ModConfigManager.CONFIG.amethystEffects.activeRange;
            float damage = ModConfigManager.CONFIG.amethystEffects.activeDamage;
            int cost = ModConfigManager.CONFIG.amethystEffects.activeDurabilityCost;

            // 检查耐久
            if (stack.getDamage() + cost >= stack.getMaxDamage()) {
                return TypedActionResult.fail(stack);
            }

            // 客户端只播放效果
            if (world.isClient()) {
                // 播放准备音效
                world.playSound(player, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_HIT,
                        SoundCategory.PLAYERS, 1.0f, 1.2f);
                return TypedActionResult.success(stack);
            }

            // 服务器端执行逻辑
            // 1. 计算扇形区域
            Vec3d origin = player.getPos().add(0, 1.5, 0);
            Vec3d lookVec = player.getRotationVec(1.0F);

            // 2. 分三个方向发射音波
            for (int i = -1; i <= 1; i++) {
                float angle = i * 60 * (float)Math.PI / 180;
                Vec3d direction = rotateY(lookVec, angle);
                Vec3d endPos = origin.add(direction.multiply(range));

                // 3. 检测路径上的实体
                Box area = new Box(
                        Math.min(origin.x, endPos.x) - 0.5,
                        Math.min(origin.y, endPos.y) - 0.5,
                        Math.min(origin.z, endPos.z) - 0.5,
                        Math.max(origin.x, endPos.x) + 0.5,
                        Math.max(origin.y, endPos.y) + 0.5,
                        Math.max(origin.z, endPos.z) + 0.5
                );

                List<LivingEntity> entities = world.getEntitiesByClass(
                        LivingEntity.class, area, e -> e != player && e.isAlive()
                );

                for (LivingEntity entity : entities) {
                    // 应用伤害
                    entity.damage(entity.getDamageSources().magic(), damage);

                    // 击退效果
                    Vec3d knockback = entity.getPos().subtract(origin).normalize();
                    entity.addVelocity(knockback.x * 0.5, 0.3, knockback.z * 0.5);

                    // 粒子效果
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                                entity.getX(), entity.getY() + 1, entity.getZ(),
                                5, 0.3, 0.3, 0.3, 0.05);
                    }
                }

                // 4. 粒子轨迹
                if (world instanceof ServerWorld serverWorld) {
                    for (double d = 0; d <= range; d += 0.5) {
                        Vec3d pos = origin.add(direction.multiply(d));
                        serverWorld.spawnParticles(ParticleTypes.GLOW,
                                pos.x, pos.y, pos.z,
                                1, 0, 0, 0, 0);
                    }
                }
            }

            // 5. 破坏玻璃方块
            int rangeInt = (int) Math.ceil(range);
            for (BlockPos pos : BlockPos.iterate(
                    player.getBlockPos().add(-rangeInt, -1, -rangeInt),
                    player.getBlockPos().add(rangeInt, 2, rangeInt)
            )) {
                Block block = world.getBlockState(pos).getBlock();
                if (isGlassBlock(world.getBlockState(pos))) {
                    world.breakBlock(pos, true);
                }
            }

            // 6. 激活紫水晶簇
            for (BlockPos pos : BlockPos.iterate(
                    player.getBlockPos().add(-rangeInt, -1, -rangeInt),
                    player.getBlockPos().add(rangeInt, 2, rangeInt)
            )) {
                Block block = world.getBlockState(pos).getBlock();
                if (block == Blocks.AMETHYST_CLUSTER || block == Blocks.LARGE_AMETHYST_BUD) {
                    world.setBlockState(pos, Blocks.AMETHYST_CLUSTER.getDefaultState());
                    world.scheduleBlockTick(pos, block, 2); // 触发红石更新
                }
            }

            // 7. 消耗耐久并播放音效
            stack.damage(cost, player, p -> p.sendToolBreakStatus(hand));
            world.playSound(null, player.getBlockPos(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    SoundCategory.PLAYERS, 1.5f, 0.8f);

            return TypedActionResult.success(stack);
        });
    }

    // 辅助方法：绕Y轴旋转向量
    private static Vec3d rotateY(Vec3d vec, float angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return new Vec3d(
                vec.x * cos - vec.z * sin,
                vec.y,
                vec.x * sin + vec.z * cos
        );
    }
    private static boolean isGlassBlock(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.GLASS ||
                block instanceof StainedGlassBlock ||
                block == Blocks.GLASS_PANE ||
                block instanceof StainedGlassPaneBlock ||
                block == Blocks.TINTED_GLASS;
    }
}
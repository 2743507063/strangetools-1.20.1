package com.stools.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import com.stools.item.ModItems;

public class VoidPearlEntity extends ThrownItemEntity {

    public VoidPearlEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public VoidPearlEntity(World world, LivingEntity owner) {
        super(ModEntities.VOID_PEARL, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.VOID_PEARL;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        // 使用 getWorld() 而不是直接访问 world 字段
        World world = getWorld();

        // 生成粒子效果
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    20,
                    0.0, 0.0, 0.0,
                    0.15
            );
        }

        // 只在服务端处理传送逻辑
        if (!world.isClient && !this.isRemoved()) {
            // 获取投掷者（玩家）
            if (this.getOwner() instanceof LivingEntity owner) {
                // 传送玩家（没有伤害）
                owner.requestTeleport(this.getX(), this.getY(), this.getZ());
                owner.fallDistance = 0.0F; // 重置摔落距离

                // 添加额外的粒子效果
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                            ParticleTypes.REVERSE_PORTAL,
                            owner.getX(), owner.getY() + 1, owner.getZ(),
                            50,
                            0.5, 0.5, 0.5,
                            0.1
                    );
                }
            }

            // 移除实体
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        // 覆盖此方法以防止对碰撞实体造成伤害
        // 原版末影珍珠会在这里造成伤害
    }
}
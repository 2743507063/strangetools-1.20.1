package com.stools.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
        World world = getWorld();

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    30, // 增加粒子数量
                    0.0, 0.0, 0.0,
                    0.2 // 提高速度
            );
        }

        if (!world.isClient && !this.isRemoved()) {
            if (this.getOwner() instanceof LivingEntity owner) {
                // 添加传送音效
                world.playSound(
                        null,
                        owner.getX(), owner.getY(), owner.getZ(),
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                        SoundCategory.PLAYERS,
                        1.0f,
                        0.8f
                );

                owner.requestTeleport(this.getX(), this.getY(), this.getZ());
                owner.fallDistance = 0.0F;

                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                            ParticleTypes.REVERSE_PORTAL,
                            owner.getX(), owner.getY() + 1, owner.getZ(),
                            80, // 增加粒子数量
                            0.7, 0.7, 0.7, // 扩大范围
                            0.15 // 提高速度
                    );
                }
            }
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        // 覆盖此方法以防止对碰撞实体造成伤害
        // 原版末影珍珠会在这里造成伤害
    }

    @Override
    public void tick() {
        super.tick();

        // 检查拥有者是否存活
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity && !((LivingEntity) owner).isAlive()) {
            this.discard(); // 玩家死亡时丢弃实体
            return;
        }

        // 添加轨迹粒子效果
        if (!this.getWorld().isClient() && this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    1,
                    0.0, 0.0, 0.0,
                    0.01
            );
        }
    }
}
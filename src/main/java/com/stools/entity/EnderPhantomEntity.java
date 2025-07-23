package com.stools.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import java.util.UUID;

public class EnderPhantomEntity extends Entity {
    private UUID playerUuid;
    private int lifetime;
    private PlayerEntity playerRef;

    public EnderPhantomEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.noClip = true;
        this.setInvisible(false);
        this.setInvulnerable(true);
    }

    public EnderPhantomEntity(World world, PlayerEntity player) {
        this(ModEntities.ENDER_PHANTOM, world);

        // 确保玩家不为 null
        if (player == null) {
            this.discard();
            return;
        }

        this.playerUuid = player.getUuid();
        this.playerRef = player;
        this.setPosition(player.getPos());
        this.setYaw(player.getYaw());
        this.setPitch(player.getPitch());
        this.setCustomNameVisible(false);
        this.setSilent(true);
        this.lifetime = 60; // 3秒生命周期
    }

    @Override
    public void tick() {
        super.tick();

        if (!getWorld().isClient()) {
            // 如果 UUID 为空，立即丢弃实体
            if (playerUuid == null) {
                this.discard();
                return;
            }

            if (this.lifetime-- <= 0) {
                this.discard();
                return;
            }

            // 确保玩家引用有效
            if (playerRef == null) {
                // 尝试重新获取玩家（无需强制类型转换）
                playerRef = getWorld().getPlayerByUuid(playerUuid);

                // 如果仍然找不到玩家，丢弃实体
                if (playerRef == null) {
                    this.discard();
                    return;
                }
            }

            // 跟随玩家位置
            if (this.age % 5 == 0) {
                this.setPosition(playerRef.getPos());
            }

            // 每5tick生成粒子效果
            if (this.age % 5 == 0 && getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                        this.getX(), this.getY() + 1, this.getZ(),
                        5, 0.2, 0.2, 0.2, 0.01);
            }
        }
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("OwnerUUID")) {
            this.playerUuid = nbt.getUuid("OwnerUUID");
        } else {
            // 如果NBT中没有UUID，设为默认值（虽然不应该发生）
            this.playerUuid = UUID.randomUUID();
        }
        this.lifetime = nbt.getInt("Lifetime");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (playerUuid != null) {
            nbt.putUuid("OwnerUUID", playerUuid);
        }
        nbt.putInt("Lifetime", lifetime);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public UUID getPlayerUuid() {
        // 确保返回值不为null
        return playerUuid != null ? playerUuid : UUID.randomUUID();
    }
}
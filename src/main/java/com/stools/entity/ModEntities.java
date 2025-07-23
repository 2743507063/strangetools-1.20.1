package com.stools.entity;

import com.stools.Strangetools;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<EnderPhantomEntity> ENDER_PHANTOM = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(Strangetools.MOD_ID, "ender_phantom"),
        FabricEntityTypeBuilder.<EnderPhantomEntity>create(SpawnGroup.MISC, EnderPhantomEntity::new)
                .dimensions(EntityDimensions.fixed(0.6f, 1.8f)) // 玩家尺寸
                .build()
    );

    public static void registerEntities() {
        // 注册代码已经在上面的静态初始化中完成
    }
}
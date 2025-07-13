package com.stools.item.custom;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class PrismarineShovel extends PrismarineTool {
    public PrismarineShovel(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, BlockTags.SHOVEL_MINEABLE, settings);
    }
}
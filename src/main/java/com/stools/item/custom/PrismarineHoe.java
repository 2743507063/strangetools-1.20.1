package com.stools.item.custom;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class PrismarineHoe extends PrismarineTool {
    public PrismarineHoe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, BlockTags.HOE_MINEABLE, settings);
    }
}
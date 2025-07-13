package com.stools.item.custom;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class PrismarineAxe extends PrismarineTool {
    public PrismarineAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, BlockTags.AXE_MINEABLE, settings);
    }
}
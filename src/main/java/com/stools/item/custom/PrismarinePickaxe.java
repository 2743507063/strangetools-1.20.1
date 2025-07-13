package com.stools.item.custom;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;

public class PrismarinePickaxe extends PrismarineTool {
    public PrismarinePickaxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, BlockTags.PICKAXE_MINEABLE, settings);
    }
}
package com.stools.block;

import com.stools.Strangetools;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block BEDROCK_ORE = register("bedrock_ore", new Block(AbstractBlock.Settings.copy(Blocks.ANCIENT_DEBRIS)));
    public static void registerBlockItems(String id, Block block) {
        Registry.register(Registries.ITEM, new Identifier(Strangetools.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }
    public static Block register(String id, Block block) {
        registerBlockItems(id, block);
        return Registry.register(Registries.BLOCK, new Identifier(Strangetools.MOD_ID, id), block);
    }
    public static void registerModBlocks() {

    }
}

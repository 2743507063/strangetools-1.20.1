package com.stools.block;

import com.stools.Strangetools;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
        public static final Block ENDER_ORE = register("ender_ore", new Block(
                AbstractBlock.Settings.create()
                        .strength(25.0F, 1000.0F)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        ));
    public static Block register(String id, Block block) {
        registerBlockItems(id, block);
        return Registry.register(Registries.BLOCK, new Identifier(Strangetools.MOD_ID, id), block);
    }
    public static void registerBlockItems(String id, Block block) {
        Registry.register(Registries.ITEM, new Identifier(Strangetools.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }
    public static void registerModBlocks() {

    }
}

package com.stools.datagen;

import com.stools.Strangetools;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> POTIONS_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(Strangetools.MOD_ID, "potions"));

    public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(POTIONS_TAG)
                .add(Items.POTION)
                .add(Items.SPLASH_POTION)
                .add(Items.LINGERING_POTION)
                //.addOptionalTag(new Identifier("minecraft", "potions")) // 可选：使用原版药水标签
                .addOptional(new Identifier("farmersdelight:milk_bottle"))
                .addOptional(new Identifier("croptopia:grape_juice"))
                .addOptional(new Identifier("bewitchment:brew"))
                .addOptional(new Identifier("bwplus:brew"));
    }
}
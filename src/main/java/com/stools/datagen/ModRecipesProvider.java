package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipesProvider extends FabricRecipeProvider {
    public ModRecipesProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        generateToolRecipes(exporter, "copper", Items.COPPER_INGOT);
        generateToolRecipes(exporter, "emerald", Items.EMERALD);
        generateToolRecipes(exporter, "lapis", Items.LAPIS_LAZULI);
        generateToolRecipes(exporter, "redstone", Items.REDSTONE);
        generateToolRecipes(exporter, "quartz", Items.QUARTZ);
        generateToolRecipes(exporter, "coal", Items.COAL);
        generateToolRecipes(exporter, "cake", Items.CAKE);
        generateToolRecipes(exporter, "obsidian", Items.OBSIDIAN);
        generateToolRecipes(exporter, "prismarine", Items.PRISMARINE_SHARD);
        generateToolRecipes(exporter, "rotten_flesh", Items.ROTTEN_FLESH);
        generateToolRecipes(exporter, "glowstone", Items.GLOWSTONE_DUST);
        generateToolRecipes(exporter, "blaze_powder", Items.FIRE_CHARGE);
        generateToolRecipes(exporter, "golden_apple", Items.GOLDEN_APPLE);
        generateToolRecipes(exporter, "enchanted_golden_apple", Items.ENCHANTED_GOLDEN_APPLE);
        generateToolRecipes(exporter, "bedrock", Items.BEDROCK);
        generateToolRecipes(exporter, "bone", Items.BONE);
        generateToolRecipes(exporter, "nether_star", Items.NETHER_STAR);
        generateToolRecipes(exporter, "netherrack", Items.NETHERRACK);

        generateArmorRecipes(exporter, "emerald", Items.EMERALD);

    }

    private void generateArmorRecipes(Consumer<RecipeJsonProvider> exporter, String material, Item materialItem) {
        generateArmorPieceRecipe(exporter, material, "helmet",
                "EEE", "E E", materialItem);

        generateArmorPieceRecipe(exporter, material, "chestplate",
                "E E", "EEE", "EEE", materialItem);

        generateArmorPieceRecipe(exporter, material, "leggings",
                "EEE", "E E", "E E", materialItem);

        generateArmorPieceRecipe(exporter, material, "boots",
                "E E", "E E", materialItem);
    }

    private void generateArmorPieceRecipe(Consumer<RecipeJsonProvider> exporter,
                                          String material,
                                          String type,
                                          String pattern1,
                                          String pattern2,
                                          Item materialItem) {
        generateArmorPieceRecipe(exporter, material, type, pattern1, pattern2, "", materialItem);
    }

    private void generateArmorPieceRecipe(Consumer<RecipeJsonProvider> exporter,
                                          String material,
                                          String type,
                                          String pattern1,
                                          String pattern2,
                                          String pattern3,
                                          Item materialItem) {
        ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(
                RecipeCategory.COMBAT,
                ModItems.ARMORS.get(material + "_" + type)
        );

        if (!pattern1.isEmpty()) builder.pattern(pattern1);
        if (!pattern2.isEmpty()) builder.pattern(pattern2);
        if (!pattern3.isEmpty()) builder.pattern(pattern3);

        builder.input('E', materialItem)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_" + type));
    }

    private void generateToolRecipes(Consumer<RecipeJsonProvider> exporter, String material, Item materialItem) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_sword"))
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_pickaxe"))
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_axe"))
                .pattern("MM ")
                .pattern("MS ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_shovel"))
                .pattern(" M ")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_hoe"))
                .pattern("MM ")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter);
    }
}
package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.item.ModItems;
import com.stools.item.ModToolMaterials;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

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
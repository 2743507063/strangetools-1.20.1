package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipesProvider extends FabricRecipeProvider {
    public static final TagKey<Item> POTIONS_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(Strangetools.MOD_ID, "potions"));
    public static final List<ItemConvertible> ENDER_ORE = List.of(ModBlocks.ENDER_ORE);
    public ModRecipesProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, ENDER_ORE, RecipeCategory.MISC, ModItems.ENDER_ALLOY_SCRAP, 0.7f, 200, "ender_scrap");
        generateToolRecipes(exporter, "potion", POTIONS_TAG);
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
        generateToolRecipes(exporter, "glass", Items.GLASS);
        generateToolRecipes(exporter, "slime", Items.SLIME_BALL);
        generateToolRecipes(exporter, "string", Items.STRING);
        generateToolRecipes(exporter, "ender_alloy", ModItems.ENDER_ALLOY_INGOT);

        generateArmorRecipes(exporter, "emerald", Items.EMERALD);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_ALLOY_INGOT, 1)
                .pattern("AAA")
                .pattern("AOE")
                .pattern("EEE")
                .input('E', Items.OBSIDIAN)
                .input('O', Items.ENDER_PEARL)
                .input('A', ModItems.ENDER_ALLOY_SCRAP)
                .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .criterion(hasItem(ModItems.ENDER_ALLOY_SCRAP), conditionsFromItem(ModItems.ENDER_ALLOY_SCRAP))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, "ender_alloy_ingot_recipe"));
        // 为每种工具类型生成锻造台升级配方
        String[] toolTypes = {"sword", "pickaxe", "axe", "shovel", "hoe"};

        for (String toolType : toolTypes) {
            // 获取对应的原版下界合金工具
            Item netheriteTool = getNetheriteTool(toolType);
            // 获取对应的末影合金工具
            Item enderAlloyTool = ModItems.TOOLS.get("ender_alloy_" + toolType);

            // 创建锻造台配方
            SmithingTransformRecipeJsonBuilder.create(
                            Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), // 使用原版下界合金升级模板
                            Ingredient.ofItems(netheriteTool), // 基础工具：下界合金工具
                            Ingredient.ofItems(ModItems.ENDER_ALLOY_INGOT, Items.ENDER_EYE), // 升级材料：末影合金锭 + 末影之眼
                            RecipeCategory.TOOLS, // 配方类别
                            enderAlloyTool // 升级后的工具
                    )
                    .criterion(hasItem(netheriteTool), conditionsFromItem(netheriteTool))
                    .criterion(hasItem(ModItems.ENDER_ALLOY_INGOT), conditionsFromItem(ModItems.ENDER_ALLOY_INGOT))
                    .offerTo(exporter, new Identifier(Strangetools.MOD_ID,
                            "ender_alloy_" + toolType + "_upgrade"));
        }
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

    private void generateToolRecipes(Consumer<RecipeJsonProvider> exporter,
                                     String material,
                                     TagKey<Item> materialTag) {
        // 剑
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_sword"))
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" S ")
                .input('M', materialTag)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_sword"));

        // 镐
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_pickaxe"))
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialTag)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_pickaxe"));

        // 斧
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_axe"))
                .pattern("MM ")
                .pattern("MS ")
                .pattern(" S ")
                .input('M', materialTag)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_axe"));

        // 锹
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_shovel"))
                .pattern(" M ")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialTag)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_shovel"));

        // 锄
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_hoe"))
                .pattern("MM ")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialTag)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_hoe"));
    }

    // 重载方法 - 支持物品
    private void generateToolRecipes(Consumer<RecipeJsonProvider> exporter,
                                     String material,
                                     ItemConvertible materialItem) {
        // 剑
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_sword"))
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_sword"));

        // 镐
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_pickaxe"))
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_pickaxe"));

        // 斧
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_axe"))
                .pattern("MM ")
                .pattern("MS ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_axe"));

        // 锹
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_shovel"))
                .pattern(" M ")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_shovel"));

        // 锄
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.TOOLS.get(material + "_hoe"))
                .pattern("MM ")
                .pattern(" S ")
                .pattern(" S ")
                .input('M', materialItem)
                .input('S', Items.STICK)
                .criterion(hasItem(materialItem), conditionsFromItem(materialItem))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, material + "_hoe"));
    }

    // 辅助方法：获取对应的下界合金工具
    private Item getNetheriteTool(String toolType) {
        switch (toolType) {
            case "sword":
                return Items.NETHERITE_SWORD;
            case "pickaxe":
                return Items.NETHERITE_PICKAXE;
            case "axe":
                return Items.NETHERITE_AXE;
            case "shovel":
                return Items.NETHERITE_SHOVEL;
            case "hoe":
                return Items.NETHERITE_HOE;
            default:
                throw new IllegalArgumentException("未知工具类型: " + toolType);
        }
    }
}
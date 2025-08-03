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
import java.util.function.Function;

public class ModRecipesProvider extends FabricRecipeProvider {
    public static final TagKey<Item> POTIONS_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(Strangetools.MOD_ID, "potions"));
    public static final TagKey<Item> DIRT_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(Strangetools.MOD_ID, "dirt"));
    public static final List<ItemConvertible> ENDER_ORE = List.of(ModBlocks.ENDER_ORE);
    public static final List<ItemConvertible> VOID_ORE = List.of(ModBlocks.VOID_ORE);
    public static final List<ItemConvertible> VOID_INGOT = List.of(ModItems.RAW_VOID);

    public ModRecipesProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, ENDER_ORE, RecipeCategory.MISC, ModItems.ENDER_ALLOY_SCRAP, 0.7f, 200, "ender_scrap");
        offerBlasting(exporter, ENDER_ORE, RecipeCategory.MISC, ModItems.ENDER_ALLOY_SCRAP, 0.7f, 200, "ender_scrap");
        offerSmelting(exporter, VOID_ORE, RecipeCategory.MISC, ModItems.VOID_INGOT, 0.7f, 200, "VOID_INGOT_ingot");
        offerBlasting(exporter, VOID_ORE, RecipeCategory.MISC, ModItems.VOID_INGOT, 0.7f, 200, "VOID_INGOT_ingot");
        offerSmelting(exporter, VOID_INGOT, RecipeCategory.MISC, ModItems.VOID_INGOT, 0.7f, 200, "VOID_INGOT_ingot");
        offerBlasting(exporter, VOID_INGOT, RecipeCategory.MISC, ModItems.VOID_INGOT, 0.7f, 200, "VOID_INGOT_ingot");
        generateToolRecipes(exporter, "potion", POTIONS_TAG);
        generateToolRecipes(exporter, "dirt", DIRT_TAG);
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
        generateToolRecipes(exporter, "apple", Items.APPLE);
        generateToolRecipes(exporter, "golden_apple", Items.GOLDEN_APPLE);
        generateToolRecipes(exporter, "bedrock", Items.BEDROCK);
        generateToolRecipes(exporter, "bone", Items.BONE);
        generateToolRecipes(exporter, "nether_star", Items.NETHER_STAR);
        generateToolRecipes(exporter, "netherrack", Items.NETHERRACK);
        generateToolRecipes(exporter, "glass", Items.GLASS);
        generateToolRecipes(exporter, "slime", Items.SLIME_BALL);
        generateToolRecipes(exporter, "string", Items.STRING);
        generateToolRecipes(exporter, "ender_alloy", ModItems.ENDER_ALLOY_INGOT);
        generateToolRecipes(exporter, "end_stone", Items.END_STONE);
        generateToolRecipes(exporter, "chorus_fruit", Items.CHORUS_FRUIT);
        generateToolRecipes(exporter, "void", ModItems.VOID_INGOT);
        generateToolRecipes(exporter, "melon", Items.MELON_SLICE);
        generateToolRecipes(exporter, "sweet_berries", Items.SWEET_BERRIES);
        generateToolRecipes(exporter, "glow_berries", Items.GLOW_BERRIES);
        generateToolRecipes(exporter, "carrot", Items.CARROT);
        generateToolRecipes(exporter, "golden_carrot", Items.GOLDEN_CARROT);

        generateArmorRecipes(exporter, "emerald", Items.EMERALD);
        generateArmorRecipes(exporter, "lapis", Items.LAPIS_LAZULI);
        generateArmorRecipes(exporter, "copper", Items.COPPER_INGOT);

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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_ALLOY_INGOT, 1)
                .pattern("EEE")
                .pattern("EVE")
                .pattern("EEE")
                .input('E', Items.ENDER_PEARL)
                .input('V', ModItems.VOID_INGOT)
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .criterion(hasItem(ModItems.VOID_INGOT), conditionsFromItem(ModItems.VOID_INGOT))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, "void_pearl_recipe"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE, 2)
                .pattern("DED")
                .pattern("DSD")
                .pattern("DDD")
                .input('E', ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE)
                .input('D', Items.DIAMOND)
                .input('S', Items.END_STONE)
                .criterion(hasItem(ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE), conditionsFromItem(ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .criterion(hasItem(Items.END_STONE), conditionsFromItem(Items.END_STONE))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, "ender_alloy_upgrade_recipe"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE, 2)
                .pattern("DED")
                .pattern("DSD")
                .pattern("DDD")
                .input('E', ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE)
                .input('D', Items.APPLE)
                .input('S', Items.GOLDEN_APPLE)
                .criterion(hasItem(ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE), conditionsFromItem(ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE))
                .criterion(hasItem(Items.APPLE), conditionsFromItem(Items.APPLE))
                .criterion(hasItem(Items.GOLDEN_APPLE), conditionsFromItem(Items.GOLDEN_APPLE))
                .offerTo(exporter, new Identifier(Strangetools.MOD_ID, "golden_apple_upgrade_recipe"));

        generateToolUpgradeRecipes(exporter,
                "ender_alloy",
                this::getDiamondTool,
                toolType -> ModItems.TOOLS.get("ender_alloy_" + toolType),
                ModItems.ENDER_ALLOY_INGOT,
                ModItems.ENDER_ALLOY_UPGRADE_SMITHING_TEMPLATE);
        generateToolUpgradeRecipes(exporter,
                "nether_star",
                this::getNetheriteTool,
                toolType -> ModItems.TOOLS.get("nether_star_" + toolType),
                Items.NETHER_STAR,
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        generateToolUpgradeRecipes(exporter,
                "enchanted_golden_apple",
                toolType -> ModItems.TOOLS.get("golden_apple_" + toolType),
                toolType -> ModItems.TOOLS.get("enchanted_golden_apple_" + toolType),
                Items.ENCHANTED_GOLDEN_APPLE,
                ModItems.APPLE_UPGRADE_SMITHING_TEMPLATE);
        generateToolUpgradeRecipes(exporter,
                "golden_apple",
                toolType -> ModItems.TOOLS.get("apple_" + toolType),
                toolType -> ModItems.TOOLS.get("golden_apple_" + toolType),
                Items.GOLD_INGOT,
                Items.GOLDEN_APPLE);
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

    // 辅助方法：获取对应的工具
    private Item getDiamondTool(String toolType) {
        return switch (toolType) {
            case "sword" -> Items.DIAMOND_SWORD;
            case "pickaxe" -> Items.DIAMOND_PICKAXE;
            case "axe" -> Items.DIAMOND_AXE;
            case "shovel" -> Items.DIAMOND_SHOVEL;
            case "hoe" -> Items.DIAMOND_HOE;
            default -> throw new IllegalArgumentException("未知工具类型: " + toolType);
        };
    }
    private Item getNetheriteTool(String toolType) {
        return switch (toolType) {
            case "sword" -> Items.NETHERITE_SWORD;
            case "pickaxe" -> Items.NETHERITE_PICKAXE;
            case "axe" -> Items.NETHERITE_AXE;
            case "shovel" -> Items.NETHERITE_SHOVEL;
            case "hoe" -> Items.NETHERITE_HOE;
            default -> throw new IllegalArgumentException("未知工具类型: " + toolType);
        };
    }
    /**
     * 生成工具升级配方
     *
     * @param materialName 材料名称（用于配方ID）
     * @param baseToolGetter 基础工具获取函数
     * @param resultToolGetter 结果工具获取函数
     * @param upgradeMaterial 升级材料
     * @param template 升级模板
     */
    private void generateToolUpgradeRecipes(Consumer<RecipeJsonProvider> exporter,
                                            String materialName,
                                            Function<String, Item> baseToolGetter,
                                            Function<String, Item> resultToolGetter,
                                            Item upgradeMaterial,
                                            Item template) {
        String[] toolTypes = {"sword", "pickaxe", "axe", "shovel", "hoe"};

        for (String toolType : toolTypes) {
            Item baseTool = baseToolGetter.apply(toolType);
            Item resultTool = resultToolGetter.apply(toolType);

            if (baseTool == null || resultTool == null) {
                continue; // 跳过无效的工具类型
            }

            SmithingTransformRecipeJsonBuilder.create(
                            Ingredient.ofItems(template),
                            Ingredient.ofItems(baseTool),
                            Ingredient.ofItems(upgradeMaterial),
                            RecipeCategory.TOOLS,
                            resultTool
                    )
                    .criterion(hasItem(baseTool), conditionsFromItem(baseTool))
                    .criterion(hasItem(upgradeMaterial), conditionsFromItem(upgradeMaterial))
                    .offerTo(exporter, new Identifier(Strangetools.MOD_ID,
                            materialName + "_" + toolType + "_upgrade"));
        }
    }
}
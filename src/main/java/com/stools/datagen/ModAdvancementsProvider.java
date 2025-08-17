// ModAdvancementsProvider.java
package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModAdvancementsProvider extends FabricAdvancementProvider {
    public ModAdvancementsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        // 主成就
        Advancement root = Advancement.Builder.create()
                .display(
                        Items.CRAFTING_TABLE,
                        Text.translatable("advancement.strangetools.root.title"),
                        Text.translatable("advancement.strangetools.root.description"),
                        new Identifier("minecraft", "textures/block/sandstone.png"),
                        AdvancementFrame.TASK,
                        true, true, false
                )
                .criterion("has_any_tool", InventoryChangedCriterion.Conditions.items(
                        ModItems.TOOLS.get("dirt_sword"),
                        ModItems.TOOLS.get("copper_sword"),
                        ModItems.TOOLS.get("cake_sword"))
                )
                .build(consumer, Strangetools.MOD_ID + ":root");

        Advancement toolBranch = Advancement.Builder.create()
                .parent(root)
                .display(
                        Items.IRON_PICKAXE, // 使用更合适的图标
                        Text.translatable("advancement.strangetools.tool_master.title"),
                        Text.translatable("advancement.strangetools.tool_master.description"),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false
                )
                .criterion("has_sword", InventoryChangedCriterion.Conditions.items(ModItems.TOOLS.get("dirt_sword")))
                .criterion("has_pickaxe", InventoryChangedCriterion.Conditions.items(ModItems.TOOLS.get("copper_pickaxe")))
                .criterion("has_axe", InventoryChangedCriterion.Conditions.items(ModItems.TOOLS.get("cake_axe")))
                .requirements(new String[][]{{"has_sword", "has_pickaxe", "has_axe"}})
                .build(consumer, Strangetools.MOD_ID + ":tool_master");

        createToolAdvancement(consumer, toolBranch, "dirt_sword", "anything_tool");
        createToolAdvancement(consumer, toolBranch, "cake_sword", "edible_weapon");
        createObsidianToolAdvancement(consumer, toolBranch);

        // 盔甲分支
        Advancement armorBranch = Advancement.Builder.create()
                .parent(root)
                .display(
                        Items.IRON_CHESTPLATE, // 使用更合适的图标
                        Text.translatable("advancement.strangetools.armor_master.title"),
                        Text.translatable("advancement.strangetools.armor_master.description"),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false
                )
                .criterion("has_helmet", InventoryChangedCriterion.Conditions.items(ModItems.ARMORS.get("copper_helmet")))
                .criterion("has_chestplate", InventoryChangedCriterion.Conditions.items(ModItems.ARMORS.get("redstone_chestplate")))
                .criterion("has_leggings", InventoryChangedCriterion.Conditions.items(ModItems.ARMORS.get("lapis_leggings")))
                .requirements(new String[][]{{"has_helmet", "has_chestplate", "has_leggings"}})
                .build(consumer, Strangetools.MOD_ID + ":armor_master");

        createArmorAdvancement(consumer, armorBranch, "redstone", "redstone_armor");
        createFullArmorSetAdvancement(consumer, armorBranch, "lapis", "full_weird_set");

        // 高级材料分支
        Advancement alloyBranch = createMaterialAdvancement(consumer, root,
                Items.NETHERITE_INGOT, "alloy_master");

        createMaterialAdvancement(consumer, alloyBranch, ModItems.ENDER_ALLOY_INGOT,
                "ender_alloy_master");

        createToolAdvancement(consumer, alloyBranch, "ender_alloy_sword", "ender_alloy_tool");

        // 探索分支
        Advancement exploreBranch = createMaterialAdvancement(consumer, root,
                Items.ENDER_PEARL, "explorer");

        createMaterialAdvancement(consumer, exploreBranch, ModItems.VOID_PEARL, "void_explorer");
    }

    private void createToolAdvancement(Consumer<Advancement> consumer,
                                       Advancement parent,
                                       String toolId,
                                       String advancementId) {

        ItemConvertible toolItem = ModItems.TOOLS.get(toolId);
        if (toolItem == null) return;

        Advancement.Builder.create()
                .parent(parent)
                .display(
                        toolItem,
                        Text.translatable("advancement.strangetools." + advancementId + ".title"),
                        Text.translatable("advancement.strangetools." + advancementId + ".description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false
                )
                .criterion("has_tool", InventoryChangedCriterion.Conditions.items(toolItem))
                .build(consumer, Strangetools.MOD_ID + ":" + advancementId);
    }

    private void createArmorAdvancement(Consumer<Advancement> consumer,
                                        Advancement parent,
                                        String material,
                                        String advancementId) {

        ItemConvertible armorItem = ModItems.ARMORS.get(material + "_chestplate");
        if (armorItem == null) return;

        Advancement.Builder.create()
                .parent(parent)
                .display(
                        armorItem,
                        Text.translatable("advancement.strangetools." + advancementId + ".title"),
                        Text.translatable("advancement.strangetools." + advancementId + ".description"),
                        null,
                        AdvancementFrame.TASK,
                        true, true, false
                )
                .criterion("has_armor", InventoryChangedCriterion.Conditions.items(armorItem))
                .build(consumer, Strangetools.MOD_ID + ":" + advancementId);
    }

    private Advancement createMaterialAdvancement(Consumer<Advancement> consumer,
                                                  Advancement parent,
                                                  ItemConvertible item,
                                                  String advancementId) {

        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        item,
                        Text.translatable("advancement.strangetools." + advancementId + ".title"),
                        Text.translatable("advancement.strangetools." + advancementId + ".description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(item))
                .build(consumer, Strangetools.MOD_ID + ":" + advancementId);
    }

    private Advancement createFullArmorSetAdvancement(Consumer<Advancement> consumer,
                                                      Advancement parent,
                                                      String material,
                                                      String advancementId) {

        ItemConvertible helmet = ModItems.ARMORS.get(material + "_helmet");
        ItemConvertible chestplate = ModItems.ARMORS.get(material + "_chestplate");
        ItemConvertible leggings = ModItems.ARMORS.get(material + "_leggings");
        ItemConvertible boots = ModItems.ARMORS.get(material + "_boots");
        if (helmet == null || chestplate == null || leggings == null || boots == null) {
            return null;
        }

        return Advancement.Builder.create()
                .parent(parent)
                .display(
                        boots,
                        Text.translatable("advancement.strangetools." + advancementId + ".title"),
                        Text.translatable("advancement.strangetools." + advancementId + ".description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false
                )
                .criterion("has_full_set", InventoryChangedCriterion.Conditions.items(
                        helmet, chestplate, leggings, boots
                ))
                .build(consumer, Strangetools.MOD_ID + ":" + advancementId);
    }

    private void createObsidianToolAdvancement(Consumer<Advancement> consumer, Advancement parent) {
        ItemConvertible obsidianPickaxe = ModItems.TOOLS.get("obsidian_pickaxe");
        if (obsidianPickaxe == null) return;

        Advancement.Builder.create()
                .parent(parent)
                .display(
                        obsidianPickaxe,
                        Text.translatable("advancement.strangetools.obsidian_tool.title"),
                        Text.translatable("advancement.strangetools.obsidian_tool.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false
                )
                .criterion("has_obsidian_pickaxe", InventoryChangedCriterion.Conditions.items(obsidianPickaxe))
                .build(consumer, Strangetools.MOD_ID + ":obsidian_tool");
    }
}
package com.stools.datagen;

import com.stools.Strangetools;
import com.stools.block.ModBlocks;
import com.stools.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.text.Text;

import java.util.List;

public class ModZhCnLangProvider extends FabricLanguageProvider {
    public ModZhCnLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "zh_cn");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("advancement.strangetools.tool_master.title", "工具大师");

        translationBuilder.add("advancement.strangetools.armor_master.title", "护甲大师");
        translationBuilder.add("advancement.strangetools.tool_master.description", "制作剑、镐、斧三种不同类型的工具");
        translationBuilder.add("advancement.strangetools.armor_master.description", "制作头盔、胸甲、护腿三种不同部位的盔甲");

        translationBuilder.add("advancement.strangetools.ender_alloy_master.title", "末影合金大师");
        translationBuilder.add("advancement.strangetools.ender_alloy_master.description", "合成末影合金锭");

        translationBuilder.add("advancement.strangetools.ender_alloy_tool.title", "末影合金工具");
        translationBuilder.add("advancement.strangetools.ender_alloy_tool.description", "制作末影合金工具");

        translationBuilder.add("advancement.strangetools.explorer.title", "探险家");
        translationBuilder.add("advancement.strangetools.explorer.description", "开始你的探索之旅");
        translationBuilder.add("advancement.strangetools.root.title", "奇怪的工具匠");
        translationBuilder.add("advancement.strangetools.root.description", "用原版物品制作第一件奇怪的工具");

        translationBuilder.add("advancement.strangetools.anything_tool.title", "万物皆可工具");
        translationBuilder.add("advancement.strangetools.anything_tool.description", "用泥土制作工具");

        translationBuilder.add("advancement.strangetools.edible_weapon.title", "蛋糕之剑");
        translationBuilder.add("advancement.strangetools.edible_weapon.description", "用蛋糕制作武器");

        translationBuilder.add("advancement.strangetools.redstone_armor.title", "红石护体");
        translationBuilder.add("advancement.strangetools.redstone_armor.description", "制作红石盔甲");

        translationBuilder.add("advancement.strangetools.full_weird_set.title", "青金覆体");
        translationBuilder.add("advancement.strangetools.full_weird_set.description", "收集全套青金石盔甲");
        translationBuilder.add("advancement.strangetools.alloy_master.title", "下界合金");
        translationBuilder.add("advancement.strangetools.alloy_master.description", "获得下界合金锭");

        translationBuilder.add("advancement.strangetools.void_explorer.title", "虚空探险者");
        translationBuilder.add("advancement.strangetools.void_explorer.description", "获得虚空珍珠");

        translationBuilder.add("advancement.strangetools.obsidian_tool.title", "看着不错");
        translationBuilder.add("advancement.strangetools.obsidian_tool.description", "获得黑曜石镐");
        // 物品翻译
        translationBuilder.add(ModItems.AMETHYST_INGOT,"紫水晶锭");
        translationBuilder.add(ModItems.SLICE_OF_CAKE, "蛋糕片");
        translationBuilder.add(ModItems.TEST_ITEM, "神秘东西");
        translationBuilder.add(ModItems.ENDER_ALLOY_INGOT, "末影合金锭");
        translationBuilder.add(ModItems.ENDER_ALLOY_SCRAP, "末影合金碎片");
        translationBuilder.add(ModItems.RAW_VOID, "虚空矿坯");
        translationBuilder.add(ModItems.VOID_INGOT, "虚空锭");
        translationBuilder.add(ModBlocks.ENDER_ORE, "末影矿石");
        translationBuilder.add(ModBlocks.VOID_ORE, "虚空矿石");
        translationBuilder.add(ModItems.VOID_PEARL, "虚空珍珠");
        translationBuilder.add("itemGroup.strangetools.tools_group", "奇奇怪怪的工具");
        translationBuilder.add("itemGroup.strangetools.armor_group", "奇奇怪怪的盔甲");
        translationBuilder.add("item.strangetools.ender_alloy_upgrade_smithing_template", "锻造模板");
        translationBuilder.add("upgrade.strangetools.ender_alloy_upgrade", "末影合金升级");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.applies_to", "钻石装备");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.ingredients", "末影合金锭");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.base_slot_description", "放入待升级的钻石盔甲/武器");
        translationBuilder.add("item.strangetools.smithing_template.ender_alloy_upgrade.additions_slot_description", "放入末影合金锭");
        translationBuilder.add("item.strangetools.apple_upgrade_smithing_template", "锻造模板");
        translationBuilder.add("upgrade.strangetools.apple_upgrade", "附魔金苹果升级");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.applies_to", "金苹果装备");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.ingredients", "附魔金苹果");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.base_slot_description", "放入待升级的金苹果盔甲/武器");
        translationBuilder.add("item.strangetools.smithing_template.apple_upgrade.additions_slot_description", "放入附魔金苹果");
        generateItemTranslations(translationBuilder, ModItems.TOOL_IDS);
        generateItemTranslations(translationBuilder, ModItems.ARMOR_IDS);

        // 配置界面翻译
        addConfigTranslations(translationBuilder);
    }

    private void generateItemTranslations(TranslationBuilder translationBuilder, List<String> itemIds) {
        for (String itemId : itemIds) {
            int lastIndex = itemId.lastIndexOf('_');
            if (lastIndex == -1) continue;

            String material = itemId.substring(0, lastIndex);
            String type = itemId.substring(lastIndex + 1);

            String materialName = getMaterialName(material);
            String typeName = getTypeName(type);

            if (materialName != null && typeName != null) {
                translationBuilder.add("item." + Strangetools.MOD_ID + "." + itemId, materialName + typeName);
            }
        }
    }

    private String getMaterialName(String material) {
        return switch (material) {
            case "copper" -> "铜";
            case "emerald" -> "绿宝石";
            case "lapis" -> "青金石";
            case "redstone" -> "红石";
            case "quartz" -> "石英";
            case "coal" -> "煤炭";
            case "cake" -> "蛋糕";
            case "obsidian" -> "黑曜石";
            case "prismarine" -> "海晶石";
            case "rotten_flesh" -> "腐肉";
            case "glowstone" -> "萤石";
            case "blaze_powder" -> "烈焰粉";
            case "apple" -> "苹果";
            case "golden_apple" -> "金苹果";
            case "enchanted_golden_apple" -> "附魔金苹果";
            case "bedrock" -> "基岩";
            case "bone" -> "骨头";
            case "nether_star" -> "下界之星";
            case "netherrack" -> "下界岩";
            case "glass" -> "玻璃";
            case "slime" -> "史莱姆";
            case "potion" -> "药水";
            case "string" -> "线";
            case "ender_alloy" -> "末影合金";
            case "end_stone" -> "末地石";
            case "chorus_fruit" -> "紫颂果";
            case "void" -> "虚空";
            case "dirt" -> "泥土";
            case "melon" -> "西瓜";
            case "sweet_berries" -> "甜浆果";
            case "glow_berries" -> "发光浆果";
            case "carrot" -> "胡萝卜";
            case "golden_carrot" -> "金胡萝卜";
            case "potato" -> "马铃薯";
            case "poisonous_potato" -> "毒马铃薯";
            case "amethyst" -> "紫水晶";
            case "flint" -> "燧石";
            case "wheat" -> "小麦";
            default -> null;
        };
    }

    private String getTypeName(String type) {
        return switch (type) {
            case "sword" -> "剑";
            case "pickaxe" -> "镐";
            case "axe" -> "斧";
            case "shovel" -> "锹";
            case "hoe" -> "锄";
            case "helmet" -> "头盔";
            case "chestplate" -> "胸甲";
            case "leggings" -> "护腿";
            case "boots" -> "靴子";
            default -> null;
        };
    }

    private void addConfigTranslations(TranslationBuilder translationBuilder) {
        // ==== 配置标题和类别 ====
        translationBuilder.add("text.autoconfig.strangetools.title", "奇奇怪怪的工具配置");

        // 类别名称
        translationBuilder.add("text.autoconfig.strangetools.category.general", "通用设置");
        translationBuilder.add("text.autoconfig.strangetools.category.tool_effects", "工具效果");
        translationBuilder.add("text.autoconfig.strangetools.category.armor_effects", "盔甲效果");
        translationBuilder.add("text.autoconfig.strangetools.category.glowstone_effects", "萤石效果");
        translationBuilder.add("text.autoconfig.strangetools.category.glass_effects", "玻璃效果");
        translationBuilder.add("text.autoconfig.strangetools.category.glass_tool", "玻璃工具设置");

        // ==== 通用设置 ====
        translationBuilder.add("text.autoconfig.strangetools.option.general.enableDebugMode", "启用调试模式");
        translationBuilder.add("text.autoconfig.strangetools.option.general.enableDebugMode.@Tooltip",
                "在控制台输出详细的调试信息");

        translationBuilder.add("text.autoconfig.strangetools.option.general.enableAllEffects", "启用所有效果");
        translationBuilder.add("text.autoconfig.strangetools.option.general.enableAllEffects.@Tooltip",
                "全局启用/禁用所有模组效果");

        // ==== 工具效果 ====
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolEffects", "启用工具效果");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolEffects.@Tooltip",
                "所有工具相关效果的总开关");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolSkills", "启用工具技能");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.enableToolSkills.@Tooltip",
                "启用工具的特殊能力");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.emeraldDropChance", "绿宝石掉落概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.emeraldDropChance.@Tooltip",
                "绿宝石工具击中时掉落绿宝石的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.copperIgniteChance", "铜点燃概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.copperIgniteChance.@Tooltip",
                "铜制工具点燃目标的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.quartzExtraDamage", "石英额外伤害");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.quartzExtraDamage.@Tooltip",
                "石英工具造成的额外魔法伤害值");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.rottenFleshHungerChance", "腐肉饥饿概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.rottenFleshHungerChance.@Tooltip",
                "腐肉工具攻击时使目标饥饿的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.endStoneDamageBonus", "末地石伤害加成 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.endStoneDamageBonus.@Tooltip",
                "末地石工具对末地生物造成的额外伤害百分比");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.sweetBerriesPoisonChance", "甜浆果中毒概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.sweetBerriesPoisonChance.@Tooltip",
                "甜浆果工具攻击时使目标中毒的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoPoisonChance", "毒马铃薯中毒概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoPoisonChance.@Tooltip",
                "毒马铃薯工具攻击时使目标中毒的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoEatPoisonChance", "食用毒马铃薯中毒概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.toolEffects.poisonousPotatoEatPoisonChance.@Tooltip",
                "食用毒马铃薯工具后自身中毒的概率");

        // ==== 盔甲效果 ====
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.enableArmorEffects", "启用盔甲效果");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.enableArmorEffects.@Tooltip",
                "所有盔甲相关效果的总开关");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectChance", "盔甲反弹概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectChance.@Tooltip",
                "盔甲将伤害反弹给攻击者的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectDamage", "盔甲反弹伤害");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.armorReflectDamage.@Tooltip",
                "盔甲反弹给攻击者的伤害值");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperPushChance", "铜击退概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperPushChance.@Tooltip",
                "铜盔甲在雷暴天气下击退攻击者的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperCleanseChance", "铜净化概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.armorEffects.copperCleanseChance.@Tooltip",
                "铜盔甲清除穿戴者一个负面效果的概率");

        // ==== 萤石效果 ====
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.enableEffects", "启用萤石效果");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.enableEffects.@Tooltip",
                "萤石相关效果的开关");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.damageAmount", "伤害值");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.damageAmount.@Tooltip",
                "萤石工具攻击时对周围亡灵生物造成的伤害值");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.range", "效果范围");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.range.@Tooltip",
                "萤石工具使用技能时影响周围亡灵生物的范围");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.glowingDuration", "发光持续时间 (秒)");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.glowingDuration.@Tooltip",
                "萤石工具攻击时使亡灵生物发光的持续时间");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.speedDuration", "速度持续时间 (秒)");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.speedDuration.@Tooltip",
                "萤石工具使用技能使亡灵生物和使用者获得速度的持续时间");

        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.durabilityCost", "耐久消耗");
        translationBuilder.add("text.autoconfig.strangetools.option.glowstoneEffects.durabilityCost.@Tooltip",
                "萤石工具触发效果时消耗的耐久度");

        // ==== 玻璃效果 ====
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.enableEffects", "启用玻璃效果");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.enableEffects.@Tooltip",
                "玻璃相关效果的开关");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectChance", "破碎反弹概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectChance.@Tooltip",
                "玻璃工具破碎时反弹伤害的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectDamage", "破碎反弹伤害");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterReflectDamage.@Tooltip",
                "玻璃工具破碎时反弹的伤害值");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterDurabilityCost", "破碎耐久消耗");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.shatterDurabilityCost.@Tooltip",
                "玻璃工具破碎时消耗的耐久度");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDurabilityCost", "冲刺耐久消耗");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDurabilityCost.@Tooltip",
                "玻璃工具冲刺时消耗的耐久度");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDuration", "冲刺持续时间 (秒)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.dashDuration.@Tooltip",
                "玻璃工具冲刺效果的持续时间");

        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.cutDoubleDropChance", "切割双倍掉落概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassEffects.cutDoubleDropChance.@Tooltip",
                "玻璃工具破坏方块时获得双倍掉落的概率");

        // ==== 玻璃工具 ====
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.enableDoubleDrop", "启用双倍掉落");
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.enableDoubleDrop.@Tooltip",
                "允许玻璃工具有时掉落双倍物品");

        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropChance", "双倍掉落概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropChance.@Tooltip",
                "玻璃工具破坏方块时获得双倍掉落的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropDurabilityCost", "双倍掉落耐久消耗");
        translationBuilder.add("text.autoconfig.strangetools.option.glassTool.doubleDropDurabilityCost.@Tooltip",
                "玻璃工具触发双倍掉落时消耗的额外耐久度");
        // ==== 紫水晶效果 ====
        translationBuilder.add("text.autoconfig.strangetools.category.amethyst_effects", "紫水晶效果");

        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.enableEffects", "启用紫水晶效果");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.enableEffects.@Tooltip[0]",
                "紫水晶工具效果的总开关");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.enableEffects.@Tooltip[1]",
                "禁用此选项将关闭所有紫水晶工具的特殊效果");

        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeDamage", "主动技能伤害");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeDamage.@Tooltip[0]",
                "紫水晶工具主动技能造成的伤害值");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeDamage.@Tooltip[1]",
                "注意：主动技能会消耗大量耐久");

        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeRange", "主动技能范围");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeRange.@Tooltip",
                "紫水晶工具主动技能的影响范围");

        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeDurabilityCost", "主动技能耐久消耗");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.activeDurabilityCost.@Tooltip",
                "每次使用主动技能消耗的耐久值");

        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.passiveCrystalChance", "被动水晶触发概率 (%)");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.passiveCrystalChance.@Tooltip",
                "攻击时触发水晶共鸣造成额外伤害的概率");

        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.passiveCrystalDamage", "被动水晶额外伤害");
        translationBuilder.add("text.autoconfig.strangetools.option.amethystEffects.passiveCrystalDamage.@Tooltip",
                "水晶共鸣造成的额外伤害值");
    }
}
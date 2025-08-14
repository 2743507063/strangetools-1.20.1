package com.stools.item;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ModTrades {
    // 主世界工具
    private static final List<String> MAIN_WORLD_MATERIALS = Arrays.asList(
            "dirt", "copper", "emerald", "lapis", "redstone", "coal",
            "obsidian", "prismarine", "rotten_flesh",
            "bone", "glass", "slime", "string", "amethyst", "flint"
    );

    // 盔甲材料
    private static final List<String> ARMOR_MATERIALS = Arrays.asList(
            "copper", "emerald", "lapis", "redstone"
    );

    // 工具类型
    private static final List<String> TOOL_TYPES = Arrays.asList(
            "sword", "pickaxe", "axe", "shovel", "hoe"
    );

    public static void registerTrades() {
        registerToolsmithTrades();
        registerArmorerTrades();
        registerWanderingTraderTrades();
    }

    private static void registerToolsmithTrades() {
        // 工具匠等级1-2交易（低级工具）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.TOOLSMITH, 1, factories -> {
            addToolsForMaterials(factories, Arrays.asList("dirt", "coal", "string", "rotten_flesh", "flint"), 1, 4);
        });

        // 工具匠等级3交易（中级工具）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.TOOLSMITH, 3, factories -> {
            addToolsForMaterials(factories, Arrays.asList("copper", "lapis", "redstone", "bone", "slime", "glass"), 3, 7);
        });

        // 工具匠等级4-5交易（高级工具）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.TOOLSMITH, 5, factories -> {
            addToolsForMaterials(factories, Arrays.asList("emerald", "obsidian", "prismarine", "amethyst"), 5, 12);
        });
    }

    private static void addToolsForMaterials(List<TradeOffers.Factory> factories, List<String> materials, int baseEmeralds, int maxUses) {
        for (String material : materials) {
            if (!MAIN_WORLD_MATERIALS.contains(material)) continue;

            for (String toolType : TOOL_TYPES) {
                String toolId = material + "_" + toolType;
                Item tool = ModItems.TOOLS.get(toolId);
                if (tool != null) {
                    // 根据工具类型调整价格
                    final int emeraldCount;
                    if (toolType.equals("axe") || toolType.equals("sword")) {
                        emeraldCount = baseEmeralds + 2; // 战斗工具更贵
                    } else {
                        emeraldCount = baseEmeralds;
                    }

                    factories.add((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, emeraldCount),
                            new ItemStack(tool),
                            maxUses, // 最大交易次数
                            8, // 经验值
                            0.05f // 价格乘数
                    ));
                }
            }
        }
    }

    private static void registerArmorerTrades() {
        // 盔甲匠等级1交易（低级盔甲）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 1, factories -> {
            addArmorForMaterial(factories, "copper", 4, 8);
        });

        // 盔甲匠等级2交易（中级盔甲）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 2, factories -> {
            addArmorForMaterial(factories, "redstone", 6, 10);
        });

        // 盔甲匠等级3交易（高级盔甲）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 3, factories -> {
            addArmorForMaterial(factories, "lapis", 8, 12);
        });

        // 盔甲匠等级4-5交易（顶级盔甲）
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.ARMORER, 5, factories -> {
            addArmorForMaterial(factories, "emerald", 12, 15);
        });
    }

    private static void addArmorForMaterial(List<TradeOffers.Factory> factories, String material, int baseEmeralds, int maxUses) {
        if (!ARMOR_MATERIALS.contains(material)) return;

        String[] armorTypes = {"helmet", "chestplate", "leggings", "boots"};
        for (String armorType : armorTypes) {
            String armorId = material + "_" + armorType;
            Item armor = ModItems.ARMORS.get(armorId);
            if (armor != null) {
                final int emeraldCount;
                // 胸甲最贵，然后是头盔和护腿，靴子最便宜
                if (armorType.equals("chestplate")) {
                    emeraldCount = baseEmeralds + 3;
                } else if (armorType.equals("helmet") || armorType.equals("leggings")) {
                    emeraldCount = baseEmeralds + 1;
                } else {
                    emeraldCount = baseEmeralds;
                }

                factories.add((entity, random) -> new TradeOffer(
                        new ItemStack(Items.EMERALD, emeraldCount),
                        new ItemStack(armor),
                        maxUses,
                        10, // 经验值
                        0.05f
                ));
            }
        }
    }

    private static void registerWanderingTraderTrades() {
        // 流浪商人出售特殊工具（较低概率）
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add((entity, random) -> {
                // 随机选择一个主世界工具
                String material = MAIN_WORLD_MATERIALS.get(random.nextInt(MAIN_WORLD_MATERIALS.size()));
                String toolType = TOOL_TYPES.get(random.nextInt(TOOL_TYPES.size()));
                String toolId = material + "_" + toolType;

                Item tool = ModItems.TOOLS.get(toolId);
                if (tool == null) return null;

                // 随机价格（3-8个绿宝石）
                int emeraldCount = 3 + random.nextInt(6);

                return new TradeOffer(
                        new ItemStack(Items.EMERALD, emeraldCount),
                        new ItemStack(tool),
                        3, // 最大交易次数
                        5, // 经验值
                        0.1f // 价格乘数
                );
            });

            // 添加一些特殊盔甲交易（较低概率）
            factories.add((entity, random) -> {
                if (random.nextFloat() < 0.3f) { // 30% 概率
                    String material = ARMOR_MATERIALS.get(random.nextInt(ARMOR_MATERIALS.size()));
                    String armorType = new String[]{"helmet", "chestplate", "leggings", "boots"}[random.nextInt(4)];
                    String armorId = material + "_" + armorType;

                    Item armor = ModItems.ARMORS.get(armorId);
                    if (armor == null) return null;

                    int emeraldCount = 8 + random.nextInt(8); // 8-15个绿宝石

                    return new TradeOffer(
                            new ItemStack(Items.EMERALD, emeraldCount),
                            new ItemStack(armor),
                            2, // 最大交易次数
                            8, // 经验值
                            0.1f
                    );
                }
                return null;
            });
        });
    }
}
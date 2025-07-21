package com.stools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "strangetools")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public int configVersion = 1;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public General general = new General();

    @ConfigEntry.Category("tool_effects")
    @ConfigEntry.Gui.TransitiveObject
    public ToolEffects toolEffects = new ToolEffects();

    @ConfigEntry.Category("armor_effects")
    @ConfigEntry.Gui.TransitiveObject
    public ArmorEffects armorEffects = new ArmorEffects();

    @ConfigEntry.Category("glowstone_effects")
    @ConfigEntry.Gui.CollapsibleObject
    public GlowstoneEffects glowstoneEffects = new GlowstoneEffects();

    @ConfigEntry.Category("glass_effects")
    @ConfigEntry.Gui.CollapsibleObject
    public GlassEffects glassEffects = new GlassEffects();

    public static class General {
        @ConfigEntry.Gui.Tooltip
        public boolean enableDebugMode = false;

        @ConfigEntry.Gui.RequiresRestart
        public boolean enableAllEffects = true;
    }

    public static class ToolEffects {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean enableToolEffects = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float emeraldDropChance = 1.0f; // 1%

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float copperIgniteChance = 30.0f; // 30%

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float quartzExtraDamage = 2.0f;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float rottenFleshHungerChance = 40.0f; // 40%
    }

    public static class ArmorEffects {
        @ConfigEntry.Gui.Tooltip
        public boolean enableArmorEffects = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float armorReflectChance = 30.0f; // 30%

        @ConfigEntry.BoundedDiscrete(min = 0, max = 10)
        @ConfigEntry.Gui.Tooltip
        public float armorReflectDamage = 2.0f;
    }
    public static class GlowstoneEffects {
        @ConfigEntry.Gui.Tooltip
        public boolean enableEffects = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float damageAmount = 2.0f;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 30)
        @ConfigEntry.Gui.Tooltip
        public float range = 8.0f; // 驱散范围

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public int glowingDuration = 10; // 发光效果持续时间（秒）

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public int speedDuration = 5; // 玩家加速持续时间（秒）

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public int durabilityCost = 10; // 右键能力耐久消耗
    }

    // 新增玻璃工具专属配置
    public static class GlassEffects {
        @ConfigEntry.Gui.Tooltip
        public boolean enableEffects = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float shatterReflectChance = 30.0f; // 破碎反击概率(%)

        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        @ConfigEntry.Gui.Tooltip
        public float shatterReflectDamage = 2.0f; // 破碎反击伤害

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public int shatterDurabilityCost = 20; // 破碎反击耐久消耗

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public int dashDurabilityCost = 30; // 透明疾行耐久消耗

        @ConfigEntry.BoundedDiscrete(min = 0, max = 60)
        @ConfigEntry.Gui.Tooltip
        public int dashDuration = 5; // 透明疾行持续时间（秒）

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @ConfigEntry.Gui.Tooltip
        public float cutDoubleDropChance = 10.0f; // 玻璃切割双倍掉落概率(%)
    }
}
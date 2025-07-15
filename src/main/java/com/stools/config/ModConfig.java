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
    @ConfigEntry.Gui.CollapsibleObject
    public GlowstoneEffects glowstoneEffects = new GlowstoneEffects();

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
}
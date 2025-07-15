package com.stools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "strangetools")
public class ModConfig implements ConfigData {

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
        public float emeraldDropChance = 25.0f; // 25%

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
}
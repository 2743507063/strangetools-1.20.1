package com.stools.config;

import com.stools.Strangetools;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.util.ActionResult;

@Config(name = "strangetools")
public class ClothModConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("general") // 明确指定分类
    public int configVersion = 1;

    // ========== General Settings ==========
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralSettings general = new GeneralSettings();

    public static class GeneralSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableDebugMode = false;

        @ConfigEntry.Gui.Tooltip
        public boolean enableAllEffects = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean coloredDurabilityBar = false;
    }

    // ========== Tool Effects ==========
    @ConfigEntry.Category("tool_effects")
    @ConfigEntry.Gui.TransitiveObject
    public ToolEffectsSettings toolEffects = new ToolEffectsSettings();

    public static class ToolEffectsSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableToolEffects = true;

        @ConfigEntry.Gui.Tooltip
        public boolean enableToolSkills = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float emeraldDropChance = 1.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float copperIgniteChance = 30.0f;

        @ConfigEntry.Gui.Tooltip
        public float quartzExtraDamage = 2.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float rottenFleshHungerChance = 40.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float endStoneDamageBonus = 25f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float sweetBerriesPoisonChance = 5.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float poisonousPotatoPoisonChance = 15.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float poisonousPotatoEatPoisonChance = 60.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float blueIceSlowChance = 10.0f;
    }

    // ========== Armor Effects ==========
    @ConfigEntry.Category("armor_effects")
    @ConfigEntry.Gui.TransitiveObject
    public ArmorEffectsSettings armorEffects = new ArmorEffectsSettings();

    public static class ArmorEffectsSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableArmorEffects = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float armorReflectChance = 30.0f;

        @ConfigEntry.Gui.Tooltip
        public float armorReflectDamage = 2.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float copperPushChance = 5.0f;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float copperCleanseChance = 15.0f;
    }

    // ========== Glowstone Effects ==========
    @ConfigEntry.Category("glowstone_effects")
    @ConfigEntry.Gui.TransitiveObject
    public GlowstoneEffectsSettings glowstoneEffects = new GlowstoneEffectsSettings();

    @ConfigEntry.Category("amethyst_effects")
    @ConfigEntry.Gui.TransitiveObject
    public AmethystEffectsSettings amethystEffects = new AmethystEffectsSettings();

    public static class GlowstoneEffectsSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableEffects = true;

        @ConfigEntry.Gui.Tooltip
        public float damageAmount = 2.0f;

        @ConfigEntry.Gui.Tooltip
        public float range = 8.0f;

        @ConfigEntry.Gui.Tooltip
        public int glowingDuration = 10;

        @ConfigEntry.Gui.Tooltip
        public int speedDuration = 5;

        @ConfigEntry.Gui.Tooltip
        public int durabilityCost = 10;
    }

    // ========== Glass Effects ==========
    @ConfigEntry.Category("glass_effects")
    @ConfigEntry.Gui.TransitiveObject
    public GlassEffectsSettings glassEffects = new GlassEffectsSettings();

    public static class GlassEffectsSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableEffects = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float shatterReflectChance = 30.0f;

        @ConfigEntry.Gui.Tooltip
        public float shatterReflectDamage = 2.0f;

        @ConfigEntry.Gui.Tooltip
        public int shatterDurabilityCost = 20;

        @ConfigEntry.Gui.Tooltip
        public int dashDurabilityCost = 30;

        @ConfigEntry.Gui.Tooltip
        public int dashDuration = 5;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float cutDoubleDropChance = 10.0f;
    }

    // ========== Glass Tool ==========
    @ConfigEntry.Category("glass_tool")
    @ConfigEntry.Gui.TransitiveObject
    public GlassToolSettings glassTool = new GlassToolSettings();

    public static class GlassToolSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableDoubleDrop = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float doubleDropChance = 10.0f;

        @ConfigEntry.Gui.Tooltip
        public int doubleDropDurabilityCost = 2;
    }
    public static class AmethystEffectsSettings {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean enableEffects = true;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public float activeDamage = 4.0f;

        @ConfigEntry.Gui.Tooltip
        public float activeRange = 5.0f;

        @ConfigEntry.Gui.Tooltip
        public int activeDurabilityCost = 20;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public float passiveCrystalChance = 15.0f;

        @ConfigEntry.Gui.Tooltip
        public float passiveCrystalDamage = 1.0f;
    }

    // 复制配置到基础配置
    public void copyTo(BaseModConfig baseConfig) {
        baseConfig.configVersion = this.configVersion;

        // General
        baseConfig.general.enableDebugMode = this.general.enableDebugMode;
        baseConfig.general.enableAllEffects = this.general.enableAllEffects;

        // Tool Effects
        baseConfig.toolEffects.enableToolEffects = this.toolEffects.enableToolEffects;
        baseConfig.toolEffects.enableToolSkills = this.toolEffects.enableToolSkills;
        baseConfig.toolEffects.emeraldDropChance = this.toolEffects.emeraldDropChance;
        baseConfig.toolEffects.copperIgniteChance = this.toolEffects.copperIgniteChance;
        baseConfig.toolEffects.quartzExtraDamage = this.toolEffects.quartzExtraDamage;
        baseConfig.toolEffects.rottenFleshHungerChance = this.toolEffects.rottenFleshHungerChance;
        baseConfig.toolEffects.endStoneDamageBonus = this.toolEffects.endStoneDamageBonus;
        baseConfig.toolEffects.sweetBerriesPoisonChance = this.toolEffects.sweetBerriesPoisonChance;
        baseConfig.toolEffects.poisonousPotatoPoisonChance = this.toolEffects.poisonousPotatoPoisonChance;
        baseConfig.toolEffects.poisonousPotatoEatPoisonChance = this.toolEffects.poisonousPotatoEatPoisonChance;

        // Armor Effects
        baseConfig.armorEffects.enableArmorEffects = this.armorEffects.enableArmorEffects;
        baseConfig.armorEffects.armorReflectChance = this.armorEffects.armorReflectChance;
        baseConfig.armorEffects.armorReflectDamage = this.armorEffects.armorReflectDamage;
        baseConfig.armorEffects.copperPushChance = this.armorEffects.copperPushChance;
        baseConfig.armorEffects.copperCleanseChance = this.armorEffects.copperCleanseChance;

        // Glowstone Effects
        baseConfig.glowstoneEffects.enableEffects = this.glowstoneEffects.enableEffects;
        baseConfig.glowstoneEffects.damageAmount = this.glowstoneEffects.damageAmount;
        baseConfig.glowstoneEffects.range = this.glowstoneEffects.range;
        baseConfig.glowstoneEffects.glowingDuration = this.glowstoneEffects.glowingDuration;
        baseConfig.glowstoneEffects.speedDuration = this.glowstoneEffects.speedDuration;
        baseConfig.glowstoneEffects.durabilityCost = this.glowstoneEffects.durabilityCost;

        // Glass Effects
        baseConfig.glassEffects.enableEffects = this.glassEffects.enableEffects;
        baseConfig.glassEffects.shatterReflectChance = this.glassEffects.shatterReflectChance;
        baseConfig.glassEffects.shatterReflectDamage = this.glassEffects.shatterReflectDamage;
        baseConfig.glassEffects.shatterDurabilityCost = this.glassEffects.shatterDurabilityCost;
        baseConfig.glassEffects.dashDurabilityCost = this.glassEffects.dashDurabilityCost;
        baseConfig.glassEffects.dashDuration = this.glassEffects.dashDuration;
        baseConfig.glassEffects.cutDoubleDropChance = this.glassEffects.cutDoubleDropChance;

        // Glass Tool
        baseConfig.glassTool.enableDoubleDrop = this.glassTool.enableDoubleDrop;
        baseConfig.glassTool.doubleDropChance = this.glassTool.doubleDropChance;
        baseConfig.glassTool.doubleDropDurabilityCost = this.glassTool.doubleDropDurabilityCost;

        baseConfig.amethystEffects.enableEffects = this.amethystEffects.enableEffects;
        baseConfig.amethystEffects.activeDamage = this.amethystEffects.activeDamage;
        baseConfig.amethystEffects.activeRange = this.amethystEffects.activeRange;
        baseConfig.amethystEffects.activeDurabilityCost = this.amethystEffects.activeDurabilityCost;
        baseConfig.amethystEffects.passiveCrystalChance = this.amethystEffects.passiveCrystalChance;
        baseConfig.amethystEffects.passiveCrystalDamage = this.amethystEffects.passiveCrystalDamage;
        baseConfig.general.coloredDurabilityBar = this.general.coloredDurabilityBar;
        baseConfig.toolEffects.blueIceSlowChance = this.toolEffects.blueIceSlowChance;
    }

    // 初始化 Cloth Config
    public static void initialize() {
        if (isClothConfigLoaded()) {
            AutoConfig.register(ClothModConfig.class, JanksonConfigSerializer::new);
            AutoConfig.getConfigHolder(ClothModConfig.class).registerSaveListener((manager, data) -> {
                data.copyTo(ModConfigManager.CONFIG);
                return ActionResult.SUCCESS;
            });
            Strangetools.LOGGER.info("Cloth Config initialized");
        }
    }

    // 检查 Cloth Config 是否加载
    private static boolean isClothConfigLoaded() {
        try {
            Class.forName("me.shedaniel.autoconfig.AutoConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
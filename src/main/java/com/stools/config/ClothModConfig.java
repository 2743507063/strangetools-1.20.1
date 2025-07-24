package com.stools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

// 仅当 cloth-config 存在时，这个类才会被加载
@Config(name = "strangetools")
public class ClothModConfig implements ConfigData {
    // 复用基础配置类的字段结构
    @ConfigEntry.Gui.Excluded
    public int configVersion = 1;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public BaseModConfig.General general = new BaseModConfig.General();

    @ConfigEntry.Category("tool_effects")
    @ConfigEntry.Gui.TransitiveObject
    public BaseModConfig.ToolEffects toolEffects = new BaseModConfig.ToolEffects();
    @ConfigEntry.Gui.Tooltip(count = 2)
    public float endStoneDamageBonus = 25f; // 对末地生物的额外伤害百分比

    @ConfigEntry.Category("armor_effects")
    @ConfigEntry.Gui.TransitiveObject
    public BaseModConfig.ArmorEffects armorEffects = new BaseModConfig.ArmorEffects();

    @ConfigEntry.Category("glowstone_effects")
    @ConfigEntry.Gui.CollapsibleObject
    public BaseModConfig.GlowstoneEffects glowstoneEffects = new BaseModConfig.GlowstoneEffects();

    @ConfigEntry.Category("glass_effects")
    @ConfigEntry.Gui.CollapsibleObject
    public BaseModConfig.GlassEffects glassEffects = new BaseModConfig.GlassEffects();

    @ConfigEntry.Category("glass_tool")
    @ConfigEntry.Gui.TransitiveObject
    public BaseModConfig.GlassTool glassTool = new BaseModConfig.GlassTool();

    // 从基础配置同步数据
    public void copyFrom(BaseModConfig baseConfig) {
        this.configVersion = baseConfig.configVersion;
        this.general = baseConfig.general;
        this.toolEffects = baseConfig.toolEffects;
        this.armorEffects = baseConfig.armorEffects;
        this.glowstoneEffects = baseConfig.glowstoneEffects;
        this.glassEffects = baseConfig.glassEffects;
        this.glassTool = baseConfig.glassTool;
    }

    // 同步到基础配置
    public void copyTo(BaseModConfig baseConfig) {
        baseConfig.configVersion = this.configVersion;
        baseConfig.general = this.general;
        baseConfig.toolEffects = this.toolEffects;
        baseConfig.armorEffects = this.armorEffects;
        baseConfig.glowstoneEffects = this.glowstoneEffects;
        baseConfig.glassEffects = this.glassEffects;
        baseConfig.glassTool = this.glassTool;
    }
}
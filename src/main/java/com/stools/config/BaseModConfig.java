package com.stools.config;

public class BaseModConfig {
    public int configVersion = 1;

    public General general = new General();
    public ToolEffects toolEffects = new ToolEffects();
    public ArmorEffects armorEffects = new ArmorEffects();
    public GlowstoneEffects glowstoneEffects = new GlowstoneEffects();
    public GlassEffects glassEffects = new GlassEffects();
    public GlassTool glassTool = new GlassTool();

    public static class General {
        public boolean enableDebugMode = false;
        public boolean enableAllEffects = true;
    }

    public static class ToolEffects {
        public boolean enableToolEffects = true;
        public boolean enableToolSkills = true;
        public float emeraldDropChance = 1.0f;
        public float copperIgniteChance = 30.0f;
        public float quartzExtraDamage = 2.0f;
        public float rottenFleshHungerChance = 40.0f;
        public float endStoneDamageBonus = 25f;
        public float sweetBerriesPoisonChance = 5.0f;
        public float poisonousPotatoPoisonChance = 15.0f;
        public float poisonousPotatoEatPoisonChance = 60.0f;
    }

    public static class ArmorEffects {
        public boolean enableArmorEffects = true;
        public float armorReflectChance = 30.0f;
        public float armorReflectDamage = 2.0f;
        public float copperPushChance = 5.0f;
        public float copperCleanseChance = 15.0f;
    }

    public static class GlowstoneEffects {
        public boolean enableEffects = true;
        public float damageAmount = 2.0f;
        public float range = 8.0f;
        public int glowingDuration = 10;
        public int speedDuration = 5;
        public int durabilityCost = 10;
    }

    public static class GlassEffects {
        public boolean enableEffects = true;
        public float shatterReflectChance = 30.0f;
        public float shatterReflectDamage = 2.0f;
        public int shatterDurabilityCost = 20;
        public int dashDurabilityCost = 30;
        public int dashDuration = 5;
        public float cutDoubleDropChance = 10.0f;
    }

    public static class GlassTool {
        public boolean enableDoubleDrop = true;
        public float doubleDropChance = 10.0f;
        public int doubleDropDurabilityCost = 2;
    }
}
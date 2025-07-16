package com.stools.enchantment;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.stools.Strangetools;

public class ModEnchantments {
    public static final DensityEnchantment DENSITY = new DensityEnchantment();

    public static void registerEnchantments() {
        Registry.register(Registries.ENCHANTMENT,
                new Identifier(Strangetools.MOD_ID, "density"),
                DENSITY
        );
    }
}
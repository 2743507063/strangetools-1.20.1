package com.stools.sound;

import com.stools.Strangetools;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent TEST = register("test");
    public static final SoundEvent MACE_SMASH_GROUND = register("mace.smash_ground");
    public static final SoundEvent MACE_HEAVY_SMASH_GROUND = register("mace.heavy_smash_ground");
    public static final SoundEvent MACE_SMASH_AIR = register("mace.smash_air");
    private static SoundEvent register(String name) {
        Identifier id = new Identifier(Strangetools.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void registerSounds() {

    }
}

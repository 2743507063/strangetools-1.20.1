package com.stools.event;

import com.stools.Strangetools;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEvents {
    public static void register() {
        CakeToolUseEvent.register();
        RottenFleshToolUseEvent.register();
        ToolEffectHandler.register();
        ArmorEffectHandler.register();
        GlowstoneToolUseEvent.register();
        BlazePowderToolUseEvent.register();
        GoldenAppleToolUseEvent.register();
        EnchantedGoldenAppleToolUseEvent.register();
        BedrockToolUseEvent.register();
        BoneToolUseEvent.register();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getWorlds().iterator().hasNext()) {
                BoneToolUseEvent.updateBoneShields(server.getOverworld());
            }
        });
        Strangetools.LOGGER.info("Registering Mod Events for " + Strangetools.MOD_ID);
    }
}
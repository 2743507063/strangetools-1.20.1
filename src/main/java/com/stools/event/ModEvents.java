package com.stools.event;

import com.stools.Strangetools;
import com.stools.event.glass.*;
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
        NetherStarToolUseEvent.register();
        GlassToolPassiveEvent.register();
        GlassToolUseEvent.register();
        GlassToolBreakEvent.register();
        GlassToolBottleEvent.register();
        GlassToolPassiveEvent.register();
        GlassToolSpecialSkillEvent.register();
        SlimeToolUseEvent.register();
        PotionToolUseEvent.register();
        StringToolUseEvent.register();
        EnderAlloyToolUseEvent.register();
        ChorusFruitToolUseEvent.register();
        VoidToolMiningEvent.register();
        VoidToolUseEvent.register();
        AppleToolUseEvent.register();
        MelonToolUseEvent.register();
        SweetBerriesToolUseEvent.register();
        GlowBerriesToolUseEvent.register();
        CarrotToolUseEvent.register();
        GoldenCarrotToolUseEvent.register();
        PotatoToolUseEvent.register();
        PoPotatoToolUseEvent.register();
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            EnderAlloyToolUseEvent.tick(world);
            VoidToolUseEvent.updateRifts(world);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getWorlds().iterator().hasNext()) {
                BoneToolUseEvent.updateBoneShields(server.getOverworld());
                NetherStarToolUseEvent.updateRifts(server.getOverworld());
            }
        });
        Strangetools.LOGGER.info("Registering Mod Events for " + Strangetools.MOD_ID);
    }
}
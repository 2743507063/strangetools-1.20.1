package com.stools.event;

import com.stools.Strangetools;

public class ModEvents {
    public static void register() {
        CakeToolUseEvent.register();
        RottenFleshToolUseEvent.register();
        Strangetools.LOGGER.info("Registering Mod Events for " + Strangetools.MOD_ID);
    }
}
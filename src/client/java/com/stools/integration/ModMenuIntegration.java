package com.stools.integration;

import com.stools.config.ClothModConfig;
import com.stools.config.ModConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (!ModConfigManager.clothConfigPresent) {
                return new Screen(Text.of("Configuration Error")) {
                    @Override
                    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                        this.renderBackground(context);
                        context.drawCenteredTextWithShadow(
                                this.textRenderer,
                                Text.of("Cloth Config API is not installed"),
                                this.width / 2,
                                this.height / 2 - 20,
                                0xFFFFFF
                        );
                        context.drawCenteredTextWithShadow(
                                this.textRenderer,
                                Text.of("Install Cloth Config to modify settings"),
                                this.width / 2,
                                this.height / 2,
                                0xAAAAAA
                        );
                        super.render(context, mouseX, mouseY, delta);
                    }
                };
            }
            return AutoConfig.getConfigScreen(ClothModConfig.class, parent).get();
        };
    }
}
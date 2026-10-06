package dev.visualadjuster;

import dev.visualadjuster.config.VisualAdjusterConfig;
import dev.visualadjuster.config.VisualAdjusterConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VisualAdjusterClient implements ClientModInitializer {
    public static final String MOD_ID = "visualadjuster";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        VisualAdjusterConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == VisualAdjusterConfigScreen.getOpenInstance()) {
                VisualAdjusterConfigScreen.syncLiveValues();
            }
        });
        LOGGER.info("Visual Adjuster loaded");
    }
}

package dev.visualadjuster.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.visualadjuster.render.ShieldTransform;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class VisualAdjusterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("VisualAdjuster");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("visualadjuster.json");

    public static VisualAdjusterConfig INSTANCE = new VisualAdjusterConfig();

    public ShieldMode shieldMode = ShieldMode.VANILLA;
    public float shieldHeight;
    public float shieldSize = 1.0F;
    public float shieldWidth = 1.0F;
    public float shieldDepth;
    public float shieldAngle;

    public static void load() {
        if (!Files.exists(PATH)) {
            INSTANCE = new VisualAdjusterConfig();
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
            VisualAdjusterConfig loaded = GSON.fromJson(reader, VisualAdjusterConfig.class);
            if (loaded != null) {
                INSTANCE = loaded;
                if (INSTANCE.shieldMode == null) {
                    INSTANCE.shieldMode = ShieldMode.VANILLA;
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not read visualadjuster.json; using defaults", e);
            INSTANCE = new VisualAdjusterConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException e) {
            LOGGER.warn("Could not save visualadjuster.json", e);
        }
    }

    public ShieldTransform createTransform(boolean rendersOnRight) {
        if (shieldMode == ShieldMode.TEXTURE_PACK) {
            return new ShieldTransform();
        }

        float sideOffset = shieldMode == ShieldMode.SIDE ? (rendersOnRight ? 4.0F : -4.0F) : 0.0F;
        float lowOffset = shieldMode == ShieldMode.LOW ? -5.0F : 0.0F;
        return new ShieldTransform(
                shieldSize * shieldWidth, shieldSize, shieldSize,
                0.0F, shieldMode == ShieldMode.SIDE ? 45.0F : 0.0F, shieldAngle,
                sideOffset, shieldHeight + lowOffset, shieldDepth);
    }

    public enum ShieldMode {
        SIDE,
        LOW,
        VANILLA,
        TEXTURE_PACK
    }
}

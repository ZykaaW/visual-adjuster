package dev.visualadjuster.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class VisualAdjusterConfigScreen {
    private static final int SLIDER_PRECISION = 100;
    private static Screen openInstance;
    private static final List<Runnable> liveSyncs = new ArrayList<>();

    private VisualAdjusterConfigScreen() {
    }

    public static Screen getOpenInstance() {
        return openInstance;
    }

    public static void syncLiveValues() {
        for (Runnable sync : liveSyncs) {
            sync.run();
        }
    }

    public static Screen create(Screen parent) {
        VisualAdjusterConfig config = VisualAdjusterConfig.INSTANCE;
        liveSyncs.clear();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("visualadjuster.title"))
                .setSavingRunnable(VisualAdjusterConfig::save);
        ConfigEntryBuilder entries = builder.entryBuilder();
        ConfigCategory shield = builder.getOrCreateCategory(Text.translatable("visualadjuster.category.shield"));

        AbstractConfigListEntry<VisualAdjusterConfig.ShieldMode> modeEntry = entries
                .startEnumSelector(Text.translatable("visualadjuster.setting.mode"),
                        VisualAdjusterConfig.ShieldMode.class, config.shieldMode)
                .setDefaultValue(VisualAdjusterConfig.ShieldMode.VANILLA)
                .setEnumNameProvider(mode -> Text.translatable(
                        "visualadjuster.mode." + mode.name().toLowerCase(Locale.ROOT)))
                .setSaveConsumer(mode -> config.shieldMode = mode)
                .build();
        shield.addEntry(modeEntry);
        liveSyncs.add(() -> config.shieldMode = modeEntry.getValue());

        shield.addEntry(slider(entries, "height", -16.0F, 16.0F, 0.0F, "/16",
                () -> config.shieldHeight, value -> config.shieldHeight = value));
        shield.addEntry(slider(entries, "size", 0.25F, 3.0F, 1.0F, "x",
                () -> config.shieldSize, value -> config.shieldSize = value));
        shield.addEntry(slider(entries, "width", 0.25F, 3.0F, 1.0F, "x",
                () -> config.shieldWidth, value -> config.shieldWidth = value));
        shield.addEntry(slider(entries, "depth", -16.0F, 16.0F, 0.0F, "/16",
                () -> config.shieldDepth, value -> config.shieldDepth = value));
        shield.addEntry(slider(entries, "angle", -90.0F, 90.0F, 0.0F, "°",
                () -> config.shieldAngle, value -> config.shieldAngle = value));

        openInstance = builder.build();
        return openInstance;
    }

    private static AbstractConfigListEntry<Integer> slider(ConfigEntryBuilder entries, String setting,
                                                            float minimum, float maximum, float defaultValue,
                                                            String unit, Supplier<Float> getter,
                                                            Consumer<Float> setter) {
        int minimumValue = Math.round(minimum * SLIDER_PRECISION);
        int maximumValue = Math.round(maximum * SLIDER_PRECISION);
        int defaultSliderValue = Math.round(defaultValue * SLIDER_PRECISION);
        int currentValue = Math.round(getter.get() * SLIDER_PRECISION);

        AbstractConfigListEntry<Integer> entry = entries
                .startIntSlider(Text.translatable("visualadjuster.setting." + setting),
                        currentValue, minimumValue, maximumValue)
                .setDefaultValue(defaultSliderValue)
                .setTextGetter(value -> Text.literal(format(value, unit)))
                .setSaveConsumer(value -> setter.accept(value / (float) SLIDER_PRECISION))
                .build();
        liveSyncs.add(() -> setter.accept(entry.getValue() / (float) SLIDER_PRECISION));
        return entry;
    }

    private static String format(int value, String unit) {
        float amount = value / (float) SLIDER_PRECISION;
        if (unit.equals("/16")) {
            return String.format(Locale.ROOT, "%.2f%s", amount, unit);
        }
        if (unit.equals("°")) {
            return String.format(Locale.ROOT, "%.0f%s", amount, unit);
        }
        return String.format(Locale.ROOT, "%.2f%s", amount, unit);
    }
}

package dev.muon.healthbars_dd_compat;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class HealthBarsDDCompatConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue LEVEL_PREFIX;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        LEVEL_PREFIX = builder
                .comment("Show level before the mob name (e.g., \"Lv. 99 Zombie\").")
                .define("level_prefix", false);

        SPEC = builder.build();
    }

    private HealthBarsDDCompatConfig() {
        // NO-OP
    }
}

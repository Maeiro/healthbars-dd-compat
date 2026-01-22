package dev.muon.healthbars_dd_compat;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(HealthBarsDynamicDifficultyCompat.MOD_ID)
public class HealthBarsDynamicDifficultyCompat {
    public static final String MOD_ID = "healthbars_dd_compat";

    public HealthBarsDynamicDifficultyCompat() {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, HealthBarsDDCompatConfig.SPEC);
    }
}

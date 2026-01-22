package dev.muon.healthbars_dd_compat.compat;

import dev.muon.dynamic_difficulty.api.LevelingAPI;
import dev.muon.dynamic_difficulty.client.LevelPlateHandler;
import dev.muon.healthbars_dd_compat.HealthBarsDDCompatConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public final class DynamicDifficultyCompat {

    private DynamicDifficultyCompat() {
        // NO-OP
    }

    public static Component appendLevelSuffix(Component baseName, LivingEntity entity) {
        if (!LevelingAPI.canHaveLevel(entity)) {
            return baseName;
        }
        if (!LevelingAPI.hasLevel(entity)) {
            return baseName;
        }
        if (!LevelingAPI.shouldShowLevel(entity)) {
            return baseName;
        }

        int level = LevelingAPI.getLevel(entity);
        int color = LevelPlateHandler.getLevelColorRGB(Minecraft.getInstance().player, entity);
        Component levelComponent = Component.translatable("healthbars.dynamic_difficulty.level_suffix", level);
        if (color != -1) {
            levelComponent = levelComponent.copy().withStyle(style -> style.withColor(color));
        }

        if (HealthBarsDDCompatConfig.LEVEL_PREFIX.get()) {
            return Component.empty()
                    .append(levelComponent)
                    .append(CommonComponents.SPACE)
                    .append(baseName);
        }

        return Component.empty()
                .append(baseName)
                .append(CommonComponents.SPACE)
                .append(levelComponent);
    }
}

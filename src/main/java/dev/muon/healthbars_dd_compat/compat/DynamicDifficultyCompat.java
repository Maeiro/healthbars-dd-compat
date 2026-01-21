package dev.muon.healthbars_dd_compat.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;
import java.util.OptionalInt;

public final class DynamicDifficultyCompat {
    private static final String LEVELING_API_CLASS = "dev.muon.dynamic_difficulty.api.LevelingAPI";
    private static final Method HAS_LEVEL_METHOD;
    private static final Method CAN_HAVE_LEVEL_METHOD;
    private static final Method SHOULD_SHOW_LEVEL_METHOD;
    private static final Method GET_LEVEL_METHOD;
    private static final Method GET_LEVEL_COLOR_METHOD;
    private static final boolean AVAILABLE;

    static {
        Method hasLevel = null;
        Method canHaveLevel = null;
        Method shouldShowLevel = null;
        Method getLevel = null;
        Method getLevelColor = null;
        boolean available = false;
        try {
            Class<?> apiClass = Class.forName(LEVELING_API_CLASS);
            hasLevel = apiClass.getMethod("hasLevel", Entity.class);
            canHaveLevel = apiClass.getMethod("canHaveLevel", Entity.class);
            shouldShowLevel = apiClass.getMethod("shouldShowLevel", Entity.class);
            getLevel = apiClass.getMethod("getLevel", LivingEntity.class);
            Class<?> plateHandlerClass = Class.forName("dev.muon.dynamic_difficulty.client.LevelPlateHandler");
            getLevelColor = plateHandlerClass.getMethod("getLevelColorRGB", Player.class, LivingEntity.class);
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }

        HAS_LEVEL_METHOD = hasLevel;
        CAN_HAVE_LEVEL_METHOD = canHaveLevel;
        SHOULD_SHOW_LEVEL_METHOD = shouldShowLevel;
        GET_LEVEL_METHOD = getLevel;
        GET_LEVEL_COLOR_METHOD = getLevelColor;
        AVAILABLE = available;
    }

    private DynamicDifficultyCompat() {
        // NO-OP
    }

    public static Component appendLevelSuffix(Component baseName, LivingEntity entity) {
        OptionalInt level = getVisibleLevel(entity);
        if (level.isEmpty()) {
            return baseName;
        }

        int color = getLevelColor(entity);
        Component levelComponent = Component.translatable("healthbars.dynamic_difficulty.level_suffix", level.getAsInt());
        if (color != -1) {
            levelComponent = levelComponent.copy().withStyle(style -> style.withColor(color));
        }

        return Component.empty()
                .append(baseName)
                .append(CommonComponents.SPACE)
                .append(levelComponent);
    }

    private static OptionalInt getVisibleLevel(LivingEntity entity) {
        if (!AVAILABLE) {
            return OptionalInt.empty();
        }

        try {
            if (!(boolean) CAN_HAVE_LEVEL_METHOD.invoke(null, entity)) {
                return OptionalInt.empty();
            }
            if (!(boolean) HAS_LEVEL_METHOD.invoke(null, entity)) {
                return OptionalInt.empty();
            }
            if (!(boolean) SHOULD_SHOW_LEVEL_METHOD.invoke(null, entity)) {
                return OptionalInt.empty();
            }

            return OptionalInt.of((int) GET_LEVEL_METHOD.invoke(null, entity));
        } catch (Throwable ignored) {
            return OptionalInt.empty();
        }
    }

    private static int getLevelColor(LivingEntity entity) {
        if (GET_LEVEL_COLOR_METHOD == null) {
            return -1;
        }

        try {
            Player player = Minecraft.getInstance().player;
            return (int) GET_LEVEL_COLOR_METHOD.invoke(null, player, entity);
        } catch (Throwable ignored) {
            return -1;
        }
    }
}

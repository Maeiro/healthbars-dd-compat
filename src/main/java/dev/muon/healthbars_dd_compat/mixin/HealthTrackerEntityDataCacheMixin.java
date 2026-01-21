package dev.muon.healthbars_dd_compat.mixin;

import dev.muon.healthbars_dd_compat.compat.DynamicDifficultyCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Pseudo
@Mixin(targets = "fuzs.healthbars.client.helper.HealthTracker$EntityDataCache", remap = false)
public abstract class HealthTrackerEntityDataCacheMixin {

    @Inject(
            method = "of(Lnet/minecraft/world/entity/LivingEntity;)Lfuzs/healthbars/client/helper/HealthTracker$EntityDataCache;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void ddCompat$appendLevel(LivingEntity livingEntity, CallbackInfoReturnable<Object> cir) {
        Object original = cir.getReturnValue();
        if (original == null) {
            return;
        }

        try {
            Method displayNameMethod = original.getClass().getMethod("displayName");
            Component displayName = (Component) displayNameMethod.invoke(original);
            Component updated = DynamicDifficultyCompat.appendLevelSuffix(displayName, livingEntity);
            if (updated == displayName) {
                return;
            }

            int health = (int) original.getClass().getMethod("health").invoke(original);
            int maxHealth = (int) original.getClass().getMethod("maxHealth").invoke(original);
            int armorValue = (int) original.getClass().getMethod("armorValue").invoke(original);
            double renderOffset = (double) original.getClass().getMethod("renderOffset").invoke(original);

            Constructor<?> ctor = original.getClass().getDeclaredConstructor(
                    Component.class,
                    int.class,
                    int.class,
                    int.class,
                    double.class
            );
            Object replacement = ctor.newInstance(updated, health, maxHealth, armorValue, renderOffset);
            cir.setReturnValue(replacement);
        } catch (Throwable ignored) {
            // Fall back to original value when reflection fails.
        }
    }
}

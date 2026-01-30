package dev.xdart.nomoredarkness.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class NoMoreDarknessClient implements ClientModInitializer {

    private static final StatusEffectInstance NIGHT_VISION =
            new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                    StatusEffectInstance.INFINITE, 0, false,
                    false, false);

    @Override
    public void onInitializeClient() {

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            applyEffect(client);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null) return;
            if (!isSingleplayer(client)) return;

            var effect = client.player.getStatusEffect(StatusEffects.NIGHT_VISION);

            if (effect == null || !effect.isInfinite()) {
                applyEffect(client);
            }
        });
    }

    private void applyEffect(MinecraftClient c_) {
        c_.player.addStatusEffect(NIGHT_VISION);
    }

    private boolean isSingleplayer(MinecraftClient c) {
        return c.player != null && c.getServer() != null && c.getServer().isRemote();
    }
}

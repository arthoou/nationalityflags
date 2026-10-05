package com.arthou.nationalityflags;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

public final class FlagClientBootstrap {
    private static final KeyMapping OPEN_SELECTOR = new KeyMapping(
        "key.nationalityflags.open_flags",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_M,
        "key.categories.nationalityflags"
    );
    private static boolean initialized;

    private FlagClientBootstrap() {
    }

    public static void init(IEventBus modEventBus) {
        if (initialized) {
            return;
        }
        initialized = true;
        modEventBus.addListener(FlagClientBootstrap::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(FlagClientBootstrap::onClientTick);
        NeoForge.EVENT_BUS.addListener(FlagClientBootstrap::onClientLogout);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_SELECTOR);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        FlagHiddenNamesBridge.sync();
        if (minecraft.player == null || minecraft.level == null || minecraft.screen != null) {
            return;
        }
        while (OPEN_SELECTOR.consumeClick()) {
            minecraft.setScreen(new FlagSelectorScreen(0));
        }
    }

    private static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        FlagClientState.clear();
        FlagHiddenNamesBridge.clear();
    }
}

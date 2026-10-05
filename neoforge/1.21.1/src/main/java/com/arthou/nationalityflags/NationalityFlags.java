package com.arthou.nationalityflags;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

@Mod(NationalityFlags.MOD_ID)
public final class NationalityFlags {
    public static final String MOD_ID = "nationalityflags";

    public NationalityFlags(IEventBus modBus) {
        modBus.addListener(FlagNetwork::register);

        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onPlayerLoggedOut);
        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onPlayerRespawn);
        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onPlayerChangedDimension);
        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onPlayerClone);
        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onTabListNameFormat);
        NeoForge.EVENT_BUS.addListener(FlagServerEvents::onServerTick);
        NeoForge.EVENT_BUS.addListener(FlagCommands::register);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            FlagClientBootstrap.init(modBus);
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}

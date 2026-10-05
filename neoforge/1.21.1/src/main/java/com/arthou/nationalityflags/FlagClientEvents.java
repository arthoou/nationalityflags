package com.arthou.nationalityflags;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

@EventBusSubscriber(modid = NationalityFlags.MOD_ID, value = Dist.CLIENT)
public final class FlagClientEvents {
    private FlagClientEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderNameTagEarly(RenderNameTagEvent event) {
        decorateNameTag(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagLate(RenderNameTagEvent event) {
        decorateNameTag(event);
    }

    private static void decorateNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        Component decorated = FlagClientState.decorate(player, event.getContent());
        if (decorated != event.getContent()) {
            event.setContent(decorated);
        }
    }
}

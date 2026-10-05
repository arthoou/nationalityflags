package com.arthou.nationalityflags;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

public final class FlagHiddenNamesServerBridge {
    private static final String NAME_DATA_CLASS = "github.pitbox46.hiddennames.data.NameData";
    private static boolean initialized;
    private static boolean available;
    private static Field dataField;
    private static Method getDisplayNameMethod;

    private FlagHiddenNamesServerBridge() {
    }

    public static Component displayName(ServerPlayer player) {
        Component hiddenName = hiddenDisplayName(player.getUUID());
        if (hiddenName != null && !hiddenName.getString().isBlank()) {
            return FlagData.stripPrefix(hiddenName);
        }

        return Component.literal(player.getGameProfile().getName());
    }

    private static Component hiddenDisplayName(UUID playerId) {
        if (!init()) {
            return null;
        }

        try {
            Object data = dataField.get(null);
            if (!(data instanceof Map<?, ?> nameDataMap)) {
                return null;
            }

            Object nameData = nameDataMap.get(playerId);
            if (nameData == null) {
                return null;
            }

            Object value = getDisplayNameMethod.invoke(nameData);
            return value instanceof Component component ? component : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static boolean init() {
        if (initialized) {
            return available;
        }

        initialized = true;
        try {
            Class<?> nameDataClass = Class.forName(NAME_DATA_CLASS);
            dataField = nameDataClass.getField("DATA");
            getDisplayNameMethod = nameDataClass.getMethod("getDisplayName");
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }

        return available;
    }
}

package com.arthou.nationalityflags;

import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FlagHiddenNamesBridge {
    private static final String NAME_DATA_CLASS = "github.pitbox46.hiddennames.data.NameData";
    private static boolean initialized;
    private static boolean available;
    private static Field dataField;
    private static Method getDisplayNameMethod;
    private static Method setDisplayNameMethod;

    private FlagHiddenNamesBridge() {
    }

    public static void sync() {
        if (!init()) {
            return;
        }
        try {
            Object data = dataField.get(null);
            if (!(data instanceof Map<?, ?> nameDataMap)) {
                return;
            }
            Map<UUID, List<String>> states = FlagClientState.snapshot();
            for (Map.Entry<?, ?> entry : nameDataMap.entrySet()) {
                if (!(entry.getKey() instanceof UUID playerId)) {
                    continue;
                }
                Object nameData = entry.getValue();
                Component current = readDisplayName(nameData);
                if (current == null) {
                    continue;
                }
                List<String> countries = states.getOrDefault(playerId, List.of());
                if (countries.isEmpty()) {
                    restoreBase(nameData, current);
                    continue;
                }

                Component desired = FlagClientState.decorate(playerId, current.copy());
                if (!desired.getString().equals(current.getString())) {
                    writeDisplayName(nameData, desired);
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static void clear() {
    }

    private static void restoreBase(Object nameData, Component current) {
        if (FlagData.hasPrefix(current)) {
            writeDisplayName(nameData, FlagData.stripPrefix(current));
        }
    }

    private static Component readDisplayName(Object nameData) {
        try {
            Object value = getDisplayNameMethod.invoke(nameData);
            if (value instanceof Component component) {
                return component;
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static void writeDisplayName(Object nameData, Component displayName) {
        try {
            setDisplayNameMethod.invoke(nameData, displayName);
        } catch (Throwable ignored) {
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
            setDisplayNameMethod = nameDataClass.getMethod("setDisplayName", Component.class);
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }
        return available;
    }
}

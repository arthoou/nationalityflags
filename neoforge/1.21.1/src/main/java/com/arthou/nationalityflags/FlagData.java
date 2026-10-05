package com.arthou.nationalityflags;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public final class FlagData {
    public static final List<String> LEGACY_FLAG_TAG_PREFIXES = List.of("nf_flag_", "nf_1211_flag_");
    public static final String FLAG_TAG_PREFIX = "nf_28_flag_";
    public static final String SEPARATOR = "\ue999";

    private FlagData() {
    }

    public static void toggleCountry(Entity entity, String countryId) {
        FlagCountry country = FlagCountry.byId(countryId);
        if (entity == null || country == null) {
            return;
        }

        String tag = FLAG_TAG_PREFIX + country.id;
        if (entity.getTags().contains(tag)) {
            entity.removeTag(tag);
        } else {
            entity.addTag(tag);
        }
    }

    public static List<String> selectedCountries(Entity entity) {
        List<String> selected = new ArrayList<>();
        if (entity == null) {
            return selected;
        }

        for (FlagCountry country : FlagCountry.values()) {
            if (entity.getTags().contains(FLAG_TAG_PREFIX + country.id)) {
                selected.add(country.id);
            }
        }
        return selected;
    }

    public static FlagCountry activeCountry(Entity entity) {
        return activeCountry(selectedCountries(entity), entity == null || entity.level() == null ? 0L : entity.level().getGameTime());
    }

    public static FlagCountry activeCountry(List<String> countries, long gameTime) {
        if (countries == null || countries.isEmpty()) {
            return null;
        }
        String id = countries.get((int) ((gameTime / 40L) % countries.size()));
        return FlagCountry.byId(id);
    }

    public static Component prefixFor(List<String> countries, long gameTime) {
        FlagCountry country = activeCountry(countries, gameTime);
        return country == null ? null : Component.literal(country.glyph + " " + SEPARATOR + " ");
    }

    public static Component prefixFor(Entity entity) {
        FlagCountry country = activeCountry(entity);
        return country == null ? null : Component.literal(country.glyph + " " + SEPARATOR + " ");
    }

    public static Component decorate(Entity entity, Component name) {
        return decorate(prefixFor(entity), name);
    }

    public static Component decorate(Component prefix, Component name) {
        if (prefix == null || name == null) {
            return name;
        }
        String rawName = name.getString();
        if (rawName.contains(SEPARATOR)) {
            return Component.empty().append(prefix).append(stripPrefix(name));
        }
        return Component.empty().append(prefix).append(name.copy());
    }

    public static boolean hasPrefix(Component name) {
        return name != null && name.getString().contains(SEPARATOR);
    }

    public static MutableComponent stripPrefix(Component component) {
        if (component == null) {
            return Component.empty();
        }

        MutableComponent copy = component.copy();
        if (removePrefixSibling(copy)) {
            return copy;
        }

        return Component.literal(stripPrefixText(component.getString()));
    }

    private static boolean removePrefixSibling(MutableComponent component) {
        List<Component> siblings = component.getSiblings();
        for (int index = 0; index < siblings.size(); index++) {
            if (siblings.get(index).getString().contains(SEPARATOR)) {
                siblings.remove(index);
                return true;
            }
        }
        return false;
    }

    public static void clearLegacyFlags(Entity entity) {
        if (entity == null) {
            return;
        }
        for (String prefix : LEGACY_FLAG_TAG_PREFIXES) {
            for (FlagCountry country : FlagCountry.values()) {
                entity.removeTag(prefix + country.id);
            }
        }
    }

    public static String stripPrefixText(String raw) {
        if (raw == null) {
            return "";
        }
        int separator = raw.indexOf(SEPARATOR);
        if (separator < 0) {
            return raw;
        }
        int end = separator + SEPARATOR.length();
        while (end < raw.length() && raw.charAt(end) == ' ') {
            end++;
        }
        return raw.substring(end);
    }
}

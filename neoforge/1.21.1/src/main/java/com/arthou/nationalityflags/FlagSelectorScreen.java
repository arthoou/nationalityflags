package com.arthou.nationalityflags;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class FlagSelectorScreen extends Screen {
    private static final ResourceLocation PAGE_1_TEXTURE = NationalityFlags.id("textures/screens/selector.png");
    private static final ResourceLocation PAGE_2_TEXTURE = NationalityFlags.id("textures/screens/selector2.png");
    private static final int GUI_WIDTH = 279;
    private static final int GUI_HEIGHT = 306;
    private static final int TEX_WIDTH = 1199;
    private static final int TEX_HEIGHT = 1312;
    private static final int[] BUTTON_X = {-119, -75, -30, 15, 59, 104, -119, -74, -30, 15, 59, 104, -119, -75, -30, 15, 59, 104, -119, -75, -30, 14, 59, 104, -119, -75, -30, 14, 59, 104};
    private static final int[] BUTTON_Y = {-86, -86, -86, -86, -86, -86, -43, -43, -43, -43, -43, -43, 0, 0, 0, 0, 0, 0, 43, 43, 42, 42, 42, 42, 86, 86, 86, 86, 86, 86};

    private final int page;

    public FlagSelectorScreen(int page) {
        super(Component.literal("Flags"));
        this.page = page;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int left = left();
        int top = top();
        graphics.blit(page == 0 ? PAGE_1_TEXTURE : PAGE_2_TEXTURE, left, top, GUI_WIDTH, GUI_HEIGHT, 0, 0, TEX_WIDTH, TEX_HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        renderPlayerName(graphics, left, top);
        renderTooltip(graphics, mouseX, mouseY, left, top);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        int left = left();
        int top = top();
        FlagCountry[] countries = countries();
        for (int i = 0; i < countries.length; i++) {
            int x = left + BUTTON_X[i] + 128;
            int y = top + BUTTON_Y[i] + 154;
            if (mouseX >= x && mouseX < x + 38 && mouseY >= y && mouseY < y + 36) {
                FlagNetwork.toggleFlag(countries[i].id);
                if (Minecraft.getInstance().player != null) {
                    FlagClientState.setPlayerFlags(Minecraft.getInstance().player.getUUID(), toggleLocal(countries[i].id));
                }
                return true;
            }
        }

        if (page == 0 && mouseX >= left + 280 && mouseX < left + 310 && mouseY >= top + 135 && mouseY < top + 170) {
            Minecraft.getInstance().setScreen(new FlagSelectorScreen(1));
            return true;
        }
        if (page == 1 && mouseX >= left - 35 && mouseX < left && mouseY >= top + 135 && mouseY < top + 170) {
            Minecraft.getInstance().setScreen(new FlagSelectorScreen(0));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private java.util.List<String> toggleLocal(String countryId) {
        if (Minecraft.getInstance().player == null) {
            return java.util.List.of();
        }
        java.util.List<String> current = new java.util.ArrayList<>(FlagClientState.flagsFor(Minecraft.getInstance().player.getUUID()));
        if (current.contains(countryId)) {
            current.remove(countryId);
        } else {
            current.add(countryId);
        }
        return current;
    }

    private void renderPlayerName(GuiGraphics graphics, int left, int top) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        Component name = FlagClientState.decorate(Minecraft.getInstance().player, Minecraft.getInstance().player.getName());
        String text = name.getString();
        graphics.drawString(font, name, left + 140 - font.width(text) / 2, top + 42, 0xFFFFFFFF, false);
    }

    private void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, int left, int top) {
        FlagCountry[] countries = countries();
        for (int i = 0; i < countries.length; i++) {
            int x = left + BUTTON_X[i] + 128;
            int y = top + BUTTON_Y[i] + 154;
            if (mouseX >= x && mouseX < x + 38 && mouseY >= y && mouseY < y + 36) {
                String playerName = Minecraft.getInstance().player == null ? "Player" : Minecraft.getInstance().player.getName().getString();
                graphics.renderTooltip(font, Component.literal(countries[i].glyph + " " + FlagData.SEPARATOR + " " + playerName), mouseX, mouseY);
                return;
            }
        }
    }

    private int left() {
        return (width - GUI_WIDTH) / 2;
    }

    private int top() {
        return (height - GUI_HEIGHT) / 2;
    }

    private FlagCountry[] countries() {
        return page == 0 ? FlagCountry.PAGE_1 : FlagCountry.PAGE_2;
    }
}

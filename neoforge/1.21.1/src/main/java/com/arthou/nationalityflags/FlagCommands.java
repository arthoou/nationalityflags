package com.arthou.nationalityflags;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class FlagCommands {
    private FlagCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("flhelp").executes(context -> {
            context.getSource().sendSuccess(() -> Component.literal("Press M to choose your flags."), false);
            return 1;
        }));
    }
}

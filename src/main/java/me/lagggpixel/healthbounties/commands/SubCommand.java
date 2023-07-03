package me.lagggpixel.healthbounties.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SubCommand {

    @NotNull
    public abstract String getCommand();

    @NotNull
    public abstract String getDescription();

    @NotNull
    public abstract String getPermission();

    @NotNull
    public abstract String getUsage();

    @Nullable
    public abstract String getTabCompletion(CommandSender commandSender, String command, String[] args);

    public abstract void execute(CommandSender commandSender, String command, String[] args);

}

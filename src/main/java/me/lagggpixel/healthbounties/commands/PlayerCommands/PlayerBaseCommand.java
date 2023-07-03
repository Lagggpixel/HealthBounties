package me.lagggpixel.healthbounties.commands.PlayerCommands;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.commands.PlayerCommands.SubCommands.CheckSubCmd;
import me.lagggpixel.healthbounties.commands.PlayerCommands.SubCommands.WithdrawSubCmd;
import me.lagggpixel.healthbounties.commands.SubCommand;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerBaseCommand implements TabExecutor {
    private final List<SubCommand> subCommands = new ArrayList<>();

    public PlayerBaseCommand() {
        try {
            Objects.requireNonNull(Main.getInstance().getCommand("healthbounties")).setExecutor(this);
            Objects.requireNonNull(Main.getInstance().getCommand("healthbounties")).setTabCompleter(this);
        }
        catch (NullPointerException exception) {
            exception.printStackTrace();
        }
        subCommands.add(new WithdrawSubCmd());
        subCommands.add(new CheckSubCmd());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            String subCommandName = strings[0];
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getCommand().equalsIgnoreCase(subCommandName)) {
                    if (commandSender.hasPermission(subCommand.getPermission())) {
                        subCommand.execute(commandSender, s, strings);
                    } else {
                        commandSender.sendMessage(Lang.NO_PERMS.toTextComponentWithPrefix(null));
                    }
                    return true;
                }
            }
            sendHelp(commandSender);
        }
        sendHelp(commandSender);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            List<String> completions = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getCommand().startsWith(strings[0])) {
                    completions.add(subCommand.getCommand());
                }
            }
            return completions;
        }
        return null;
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage(Main.getInstance().returnChatFormatting("Available sub commands:"));
        for (SubCommand subCommand : subCommands) {
            commandSender.sendMessage(Main.getInstance().returnChatFormatting("- " + subCommand.getCommand() + ": " + subCommand.getDescription()));
        }
    }
}

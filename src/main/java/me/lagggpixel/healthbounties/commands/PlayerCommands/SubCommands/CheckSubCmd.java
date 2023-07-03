package me.lagggpixel.healthbounties.commands.PlayerCommands.SubCommands;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.commands.SubCommand;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CheckSubCmd extends SubCommand {

    @Override
    public @NotNull String getCommand() {
        return "check";
    }

    @Override
    public @NotNull String getDescription() {
        return "check the amount of bounties you have";
    }

    @Override
    public @NotNull String getPermission() {
        return "healthbounties.player.check";
    }

    @Override
    public @NotNull String getUsage() {
        return "/%command% check";
    }

    @Override
    public @Nullable String getTabCompletion(CommandSender commandSender, String command, String[] args) {
        return null;
    }

    @Override
    public void execute(CommandSender commandSender, String command, String[] args) {
        if (commandSender instanceof Player sender) {
            if (args.length != 1) {
                sender.sendMessage(Main.getInstance().returnChatFormatting(getUsage().replace("%command%", command)));
                return;
            }
            int amount = BountyManager.getBountyOrDefault(sender.getUniqueId());
            sender.sendMessage(Lang.CHECK_BOUNTIES.toTextComponentWithPrefix(Map.of("{amount}", String.valueOf(amount))));
        }
        else {
            commandSender.sendMessage(Lang.PLAYER_ONLY.toTextComponentWithPrefix(null));
        }
    }
}

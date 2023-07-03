package me.lagggpixel.healthbounties.commands.AdminCommands.SubCommands;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.commands.SubCommand;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class UnbanSubCmd extends SubCommand {
    @Override
    public @NotNull String getCommand() {
        return "unban";
    }

    @Override
    public @NotNull String getDescription() {
        return "unban to a player";
    }

    @Override
    public @NotNull String getPermission() {
        return "healthbounties.admin.unban";
    }

    @Override
    public @NotNull String getUsage() {
        return "/%command% unban [player]";
    }

    @Override
    public @Nullable String getTabCompletion(CommandSender commandSender, String command, String[] args) {
        return null;
    }

    @Override
    public void execute(CommandSender commandSender, String command, String[] args) {
        if (args.length != 1 && args.length != 2) {
            commandSender.sendMessage(Main.getInstance().returnChatFormatting(getUsage().replace("%command%", command)));
            return;
        }

        if (args.length == 1) {
            if (commandSender instanceof Player sender) BountyManager.openAdminUnbanMenu(sender);
            else commandSender.sendMessage(Lang.PLAYER_ONLY.toTextComponentWithPrefix(null));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        UUID targetUuid = target.getUniqueId();
        if (!BountyManager.containsPlayer(targetUuid)) {
            commandSender.sendMessage(Lang.PLAYER_NOT_BANNED.toTextComponentWithPrefix(Map.of(
                    "{player}", args[1]
            )));
            return;
        }
        if (!BountyManager.isPlayerBanned(targetUuid)) {
            commandSender.sendMessage(Lang.PLAYER_NOT_BANNED.toTextComponentWithPrefix(Map.of(
                    "{player}", args[1]
            )));
            return;
        }

        BountyManager.unbanPlayer(targetUuid);

        commandSender.sendMessage(Lang.UNBANNED_PLAYER_ADMIN.toTextComponentWithPrefix(Map.of(
                "{player}", args[1]
        )));
    }
}

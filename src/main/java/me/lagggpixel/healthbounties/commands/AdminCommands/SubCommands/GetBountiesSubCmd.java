

package me.lagggpixel.healthbounties.commands.AdminCommands.SubCommands;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.commands.SubCommand;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class GetBountiesSubCmd extends SubCommand {
    @Override
    public @NotNull String getCommand() {
        return "getbounties";
    }

    @Override
    public @NotNull String getDescription() {
        return "get the bounties of a player";
    }

    @Override
    public @NotNull String getPermission() {
        return "healthbounties.admin.getbounties";
    }

    @Override
    public @NotNull String getUsage() {
        return "%command% getbounties <player>";
    }

    @Override
    public @Nullable String getTabCompletion(CommandSender commandSender, String command, String[] args) {
        return null;
    }

    @Override
    public void execute(CommandSender commandSender, String command, String[] args) {

        if (args.length != 2) {
            commandSender.sendMessage(Main.getInstance().returnChatFormatting(getUsage().replace("%command%", command)));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        UUID targetUuid = target.getUniqueId();

        int var1 = BountyManager.getBounty(targetUuid);

        commandSender.sendMessage(Lang.GET_BOUNTIES.toTextComponentWithPrefix(Map.of(
                "{player}", args[1],
                "{amount}", String.valueOf(var1)
        )));

    }
}

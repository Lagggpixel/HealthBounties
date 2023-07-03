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

public class AddBountiesSubCmd extends SubCommand {
    @Override
    public @NotNull String getCommand() {
        return "addbounties";
    }

    @Override
    public @NotNull String getDescription() {
        return "add bounties to a player";
    }

    @Override
    public @NotNull String getPermission() {
        return "healthbounties.admin.addbounties";
    }

    @Override
    public @NotNull String getUsage() {
        return "/%command% addbounties <player> <amount>";
    }

    @Override
    public @Nullable String getTabCompletion(CommandSender commandSender, String command, String[] args) {
        return null;
    }

    @Override
    public void execute(CommandSender commandSender, String command, String[] args) {

        if (args.length != 3) {
            commandSender.sendMessage(Main.getInstance().returnChatFormatting(getUsage().replace("%command%", command)));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        UUID targetUuid = target.getUniqueId();
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException exception) {
            commandSender.sendMessage(Lang.MUST_BE_NUMBER.toTextComponentWithPrefix(null));
            return;
        }
        BountyManager.addBounty(targetUuid, amount);
        if (target.isOnline()) {
            assert target.getPlayer() != null;
            BountyManager.updateHealth(target.getPlayer());
            BountyManager.checkIsBanned(target.getPlayer());
        }
        commandSender.sendMessage(Lang.SET_BOUNTIES.toTextComponentWithPrefix(Map.of(
                "{player}", args[1],
                "{amount}", String.valueOf(BountyManager.getBounty(targetUuid))
        )));

    }
}

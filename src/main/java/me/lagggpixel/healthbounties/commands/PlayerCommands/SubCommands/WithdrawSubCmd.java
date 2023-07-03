package me.lagggpixel.healthbounties.commands.PlayerCommands.SubCommands;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.commands.SubCommand;
import me.lagggpixel.healthbounties.modules.UUID.UuidGenerator;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.vouchers.VoucherBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WithdrawSubCmd extends SubCommand {
    @Override
    public @NotNull String getCommand() {
        return "withdraw";
    }

    @Override
    public @NotNull String getDescription() {
        return "withdraw bounties";
    }

    @Override
    public @NotNull String getPermission() {
        return "healthbounties.player.withdraw";
    }

    @Override
    public @NotNull String getUsage() {
        return "/%command% withdraw <amount>";
    }

    @Override
    public @Nullable String getTabCompletion(CommandSender commandSender, String command, String[] args) {
        return null;
    }

    @Override
    public void execute(CommandSender commandSender, String command, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length != 2) {
                commandSender.sendMessage(Main.getInstance().returnChatFormatting(getUsage().replace("%command%", command)));
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                commandSender.sendMessage(Lang.MUST_BE_NUMBER.toTextComponentWithPrefix(null));
                return;
            }

            int playerHas = BountyManager.getBountyOrDefault(player.getUniqueId());

            if (amount >= playerHas) {
                player.sendMessage(Lang.NOT_ENOUGH_BOUNTIES.toTextComponentWithPrefix(Map.of("{amount}", String.valueOf(amount))));
                return;
            }

            BountyManager.removeBounty(player.getUniqueId(), amount);
            BountyManager.updateHealth(player);

            ItemStack voucher = VoucherBuilder.createVoucher(UuidGenerator.generateVoucherUUID(), amount);

            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItem(player.getLocation(), voucher);
            }
            else {
                player.getInventory().addItem(voucher);
            }
            player.sendMessage(Lang.WITHDRAW_SUCCESSFUL.toTextComponentWithPrefix(Map.of("{amount}", String.valueOf(amount))));
        }
        else {
            commandSender.sendMessage(Lang.PLAYER_ONLY.toTextComponentWithPrefix(null));
        }
    }
}

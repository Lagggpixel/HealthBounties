package me.lagggpixel.healthbounties.listeners;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import me.lagggpixel.healthbounties.modules.coins.CoinsManager;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.revive.ReviveManager;
import me.lagggpixel.healthbounties.modules.vouchers.VoucherBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class PlayerInteractListener implements Listener {

    public PlayerInteractListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractMainHand(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR) ) return;
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        Player player = event.getPlayer();

        //<editor-fold desc="Bounty voucher checker">
        if (VoucherBuilder.isVoucher(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
            UUID uuid = VoucherBuilder.getVoucherUUID(itemStack);
            Integer amount = VoucherBuilder.getVoucherAmount(itemStack);

            if (Main.getInstance().getVoucherMap().containsKey(uuid) && Main.getInstance().getVoucherMap().get(uuid).equals(amount)) {
                if (player.getInventory().getItemInMainHand().getAmount() == 0 || player.getInventory().getItemInMainHand().getAmount() == 1) {
                    player.getInventory().setItemInMainHand(null);
                } else {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                }
                Main.getInstance().getVoucherMap().remove(uuid);
                BountyManager.addBounty(player.getUniqueId(), amount);
                player.sendMessage(Lang.CLAIMED_BOUNTIES.toTextComponentWithPrefix(Map.of("{amount}", amount.toString(), "{source}", Lang.CLAIM_SOURCE_VOUCHER.toString())));
            } else {
                player.sendMessage(Lang.UNKNOWN_VOUCHER.toTextComponentWithPrefix(null));
            }
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Bounty coin checker">
        if (CoinsManager.isCoinItem(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
            if (player.getInventory().getItemInMainHand().getAmount() == 0 || player.getInventory().getItemInMainHand().getAmount() == 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            }
            BountyManager.addBounty(player.getUniqueId(), 50);
            player.sendMessage(Lang.CLAIMED_BOUNTIES.toTextComponentWithPrefix(Map.of("{amount}", "50", "{source}", Lang.CLAIM_SOURCE_COIN.toString())));
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Revive map checker">
        if (ReviveManager.isReviveItem(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
            BountyManager.openPlayerUnbanMenu(player);
            return;
        }
        // </editor-fold>
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractOffHand(PlayerInteractEvent event) {

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR) ) return;
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Player player = event.getPlayer();

        //<editor-fold desc="Bounty voucher checker">
        if (VoucherBuilder.isVoucher(event.getPlayer().getInventory().getItemInOffHand())) {
            event.setCancelled(true);
            ItemStack itemStack = event.getPlayer().getInventory().getItemInOffHand();
            UUID uuid = VoucherBuilder.getVoucherUUID(itemStack);
            Integer amount = VoucherBuilder.getVoucherAmount(itemStack);

            if (Main.getInstance().getVoucherMap().containsKey(uuid) && Main.getInstance().getVoucherMap().get(uuid).equals(amount)) {
                if (player.getInventory().getItemInOffHand().getAmount() == 0 || player.getInventory().getItemInOffHand().getAmount() == 1) {
                    player.getInventory().setItemInOffHand(null);
                } else {
                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                }
                Main.getInstance().getVoucherMap().remove(uuid);
                BountyManager.addBounty(player.getUniqueId(), amount);
                player.sendMessage(Lang.CLAIMED_BOUNTIES.toTextComponentWithPrefix(Map.of("{amount}", amount.toString(), "{source}", Lang.CLAIM_SOURCE_VOUCHER.toString())));
            } else {
                player.sendMessage(Lang.UNKNOWN_VOUCHER.toTextComponentWithPrefix(null));
            }
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Bounty coin checker">
        if (CoinsManager.isCoinItem(event.getPlayer().getInventory().getItemInOffHand())) {
            event.setCancelled(true);

            if (player.getInventory().getItemInOffHand().getAmount() == 0 || player.getInventory().getItemInOffHand().getAmount() == 1) {
                player.getInventory().setItemInOffHand(null);
            } else {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
            }
            BountyManager.addBounty(player.getUniqueId(), 50);
            player.sendMessage(Lang.CLAIMED_BOUNTIES.toTextComponentWithPrefix(Map.of("{amount}", "50", "{source}", Lang.CLAIM_SOURCE_COIN.toString())));
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Revive map checker">
        if (ReviveManager.isReviveItem(event.getPlayer().getInventory().getItemInOffHand())) {
            event.setCancelled(true);
            BountyManager.openPlayerUnbanMenu(player);
            return;
        }
        //</editor-fold>
    }
}

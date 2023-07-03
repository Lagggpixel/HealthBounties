package me.lagggpixel.healthbounties.modules.menu.menus;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.bounty.BountyManager;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.menu.PaginatedMenu;
import me.lagggpixel.healthbounties.modules.menu.PlayerMenuUtility;
import me.lagggpixel.healthbounties.modules.revive.ReviveManager;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerRevivePaginatedMenu extends PaginatedMenu {

    NamespacedKey playerHeadNameSpacedKey = new NamespacedKey(Main.getInstance(), "uuid");

    public PlayerRevivePaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public TextComponent getMenuName() {
        return Main.getInstance().returnChatFormatting("Choose a player to revive");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack itemStack = e.getCurrentItem();

        ArrayList<UUID> players = BountyManager.getBannedPlayers();

        if (itemStack == null) return;

        String itemType = itemStack.getItemMeta().getPersistentDataContainer().get(itemTypeNameSpacedKey, PersistentDataType.STRING);

        if (itemType == null) return;

        switch (itemType) {
            case "filter" -> {}
            case "players" -> {
                UUID uuid = UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(playerHeadNameSpacedKey, PersistentDataType.STRING)));
                @NotNull String playerName;
                if (Bukkit.getOfflinePlayer(uuid).getName() != null) {
                    playerName = Objects.requireNonNull(Bukkit.getOfflinePlayer(uuid).getName());
                }
                else playerName = uuid.toString();
                if (!BountyManager.isPlayerBanned(uuid)) {
                    p.sendMessage(Lang.PLAYER_NOT_BANNED.toTextComponentWithPrefix(Map.of("{player}", playerName)));
                    return;
                }

                if (ReviveManager.isReviveItem(p.getInventory().getItemInMainHand())) {
                    if (p.getInventory().getItemInMainHand().getAmount() == 0 || p.getInventory().getItemInMainHand().getAmount() == 1) {
                        p.getInventory().setItemInMainHand(null);
                    } else {
                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                    }
                }
                else if (ReviveManager.isReviveItem(p.getInventory().getItemInOffHand())) {
                    if (p.getInventory().getItemInOffHand().getAmount() == 0 || p.getInventory().getItemInOffHand().getAmount() == 1) {
                        p.getInventory().setItemInOffHand(null);
                    } else {
                        p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount() - 1);
                    }
                }
                else {
                    p.sendMessage(Lang.NO_REVIVE_MAP_IN_HAND.toTextComponentWithPrefix(null));
                    return;
                }

                BountyManager.unbanPlayer(uuid);

                p.sendMessage(Lang.REVIVE_PLAYER.toTextComponentWithPrefix(Map.of("{player}", playerName)));
                p.closeInventory();
            }
            case "button" -> {
                String function = itemStack.getItemMeta().getPersistentDataContainer().get(itemFunctionNameSpaceKey, PersistentDataType.STRING);
                if (function == null) return;
                switch (function) {
                    case "close" -> {
                        p.closeInventory();
                    }
                    case "right" -> {
                        if (!((index + 1) >= players.size())) {
                            page = page + 1;
                            super.open();
                        } else {
                            p.sendMessage(Main.getInstance().returnChatFormatting("&7You are already on the last page."));
                        }
                    }
                    case "left" -> {
                        if (page == 0) {
                            p.sendMessage(Main.getInstance().returnChatFormatting("&7You are already on the first page."));
                        } else {
                            page = page - 1;
                            super.open();
                        }
                    }
                }

            }
        }
    }

    @Override
    public void setMenuItems() {

        addMenuBorder();
        ArrayList<UUID> players = BountyManager.getBannedPlayers();

        if (players.isEmpty()) return;


        for (int i = 0; i < getMaxItemsPerPage(); i++) {
            index = getMaxItemsPerPage() * page + i;
            if (index >= players.size()) break;
            if (players.get(index) != null) {

                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
                itemMeta.displayName(Main.getInstance().returnChatFormatting("&6&lRevive &e" + Bukkit.getOfflinePlayer(players.get(index)).getName()));
                itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(players.get(index)));

                itemMeta.getPersistentDataContainer().set(itemTypeNameSpacedKey, PersistentDataType.STRING, "players");
                itemMeta.getPersistentDataContainer().set(playerHeadNameSpacedKey, PersistentDataType.STRING, players.get(index).toString());

                itemStack.setItemMeta(itemMeta);
                inventory.addItem(itemStack);
            }
        }
    }
}

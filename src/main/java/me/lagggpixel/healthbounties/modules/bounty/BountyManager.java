package me.lagggpixel.healthbounties.modules.bounty;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.menu.menus.AdminUnbanPaginatedMenu;
import me.lagggpixel.healthbounties.modules.menu.menus.PlayerRevivePaginatedMenu;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class BountyManager {

    public static void addKillBounty(@NotNull UUID player) {
        addBounty(player, Main.getInstance().getPlayerKillBounties());
    }

    public static void removeDeathBounty(@NotNull UUID player) {
        int bounties = getBountyOrDefault(player);
        if (bounties < 2000) {
            removeBounty(player, 100);
        }
        else if (bounties < 3000) {
            removeBounty(player, 200);
        }
        else if (bounties < 4000) {
            removeBounty(player, 300);
        }
        else {
            removeBounty(player, 400);
        }
    }

    @NotNull
    public static Boolean checkIsBanned(@NotNull Player player) {
        if (isPlayerBanned(player.getUniqueId())) {
            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
            player.kick(Lang.BANNED.toTextComponent(), PlayerKickEvent.Cause.BANNED);
            return true;
        }
        else return false;
    }

    @NotNull
    public static Boolean isPlayerBanned(@NotNull UUID player) {
        Integer bounty = getBounty(player);
        if (bounty == null) return false;
        return bounty<=0;
    }

    public static void banPlayer(@NotNull UUID player) {
        setBounty(player, 0);
    }

    public static void unbanPlayer(@NotNull UUID player) {
        setBounty(player, Main.getInstance().getDefaultUnbanBounty());
    }

    public static void updateHealth(@NotNull Player player) {
        int bounty = getBountyOrDefault(player.getUniqueId());
        int heartCount = Math.floorDiv((bounty-1000), 300) + 10;
        if (heartCount < Main.getInstance().getMinHearts()) heartCount = Main.getInstance().getMinHearts();
        if (heartCount > Main.getInstance().getMaxHearts()) heartCount = Main.getInstance().getMaxHearts();
        int health = heartCount*2;
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
    }

    public static void addBounty(@NotNull UUID player, @NotNull Integer amount) {
        int int1 = getBountyOrDefault(player);
        int int2 = int1+amount;
        if (amount >= Main.getInstance().getMaxBounties()) int2 = Main.getInstance().getMaxBounties();
        setBounty(player, Math.max(int2, 0));
    }

    public static void removeBounty(@NotNull UUID player, @NotNull Integer amount) {
        int int1 = getBountyOrDefault(player);
        int int2 = int1-amount;
        setBounty(player, Math.max(int2, 0));
    }


    public static void setBounty(@NotNull UUID player, @NotNull Integer amount) {
        setDefaultBountyIfAbsent(player);
        if (amount >= Main.getInstance().getMaxBounties()) amount = Main.getInstance().getMaxBounties();
        Main.getInstance().getBountyMap().replace(player, amount);
    }

    @Nullable
    public static Integer getBounty(@NotNull UUID player) {
        if (!containsPlayer(player)) return null;
        return Main.getInstance().getBountyMap().get(player);
    }

    @NotNull
    public static Integer getBountyOrDefault(@NotNull UUID player) {
        if (!containsPlayer(player)) setDefaultBounty(player);
        return Main.getInstance().getBountyMap().get(player);
    }


    public static void setDefaultBounty(@NotNull UUID player) {
        Main.getInstance().getBountyMap().remove(player);
        Main.getInstance().getBountyMap().put(player, Main.getInstance().getDefaultBounty());
    }

    public static void setDefaultBountyIfAbsent(@NotNull UUID player) {
        Main.getInstance().getBountyMap().putIfAbsent(player, Main.getInstance().getDefaultBounty());
    }

    @NotNull
    public static Boolean containsPlayer(@NotNull UUID uuid) {
        return Main.getInstance().getBountyMap().containsKey(uuid);
    }

    public static void openAdminUnbanMenu(@NotNull Player player) {
        new AdminUnbanPaginatedMenu(Main.getInstance().getPlayerMenuUtility(player)).open();
    }

    public static void openPlayerUnbanMenu(@NotNull Player player) {
        new PlayerRevivePaginatedMenu(Main.getInstance().getPlayerMenuUtility(player)).open();
    }

    public static ArrayList<UUID> getBannedPlayers() {
        ArrayList<UUID> players = new ArrayList<>();

        Main.getInstance().getBountyMap().forEach((k, v) -> {
            if (isPlayerBanned(k)) players.add(k);
        });

        return players;
    }

}

package me.lagggpixel.healthbounties.modules.lang;

import me.lagggpixel.healthbounties.Main;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public enum Lang {
    PREFIX("prefix", "&eHealthBounties&7: &r"),
    INVALID_ARGS("invalid-args", "&cInvalid args!"),
    PLAYER_ONLY("player-only", "Sorry but that can only be run by a player!"),
    MUST_BE_NUMBER("must-be-number", "&cYou need to specify a number."),
    NO_PERMS("no-permissions", "&cYou don''t have permission for that!"),
    GET_BOUNTIES("get-bounties-command", "&c{player} has {amount} bounties!"),
    SET_BOUNTIES("set-bounties-command", "&c{player} now has {amount} bounties!"),
    SELF_BOUNTIES("self-bounties-command", "&cYou have {amount} bounties!"),
    BANNED("banned-kick-message", "&cYou have been banned from the server\nas you don't have any more hearts."),
    PLAYER_NOT_BANNED("player-not-banned", "&c{player} is not banned."),
    UNBANNED_PLAYER_ADMIN("unbanned-player", "&cYou have unbanned {player}."),
    UNKNOWN_VOUCHER("unknown-voucher", "&cThat voucher is not a valid voucher. Is it duped?"),
    UNKNOWN_COIN("unknown-coin", "&cThat bounty coin is not a valid bounty coin. Is it duped?"),
    UNKNOWN_BOUNTY_REVIVE_MAP("unknown-bounty-revive-map", "&cThat bounty revive map is not a valid bounty revive map. Is it duped?"),
    CLAIMED_BOUNTIES("claimed-bounties", "&cYou have claimed {amount} bounties from a {source}."),
    NOT_ENOUGH_BOUNTIES("not-enough-bounties", "&cYou don't have enough bounties to withdraw {amount}."),
    NOT_ENOUGH_BOUNTIES_TO_CRAFT("not-enough-bounties", "&cYou don't have enough bounties to craft a bounty card."),
    WITHDRAW_SUCCESSFUL("withdraw-successful", "&cYou have successfully withdraw {amount} bounties."),
    CHECK_BOUNTIES("check-bounties", "&cYou have {amount} bounties."),
    REVIVE_PLAYER("revived-player", "&cYou have revived {player}."),
    NO_REVIVE_MAP_IN_HAND("no-revive-map-in-hand", "&cYou don't have a revive map in your hand."),
    CLAIM_SOURCE_VOUCHER("bounty.voucher", "bounty card"),
    CLAIM_SOURCE_COIN("bounty.coin", "bounty coin");

    private final String path;
    private final String def;
    private static YamlConfiguration LANG;

    /**
     * Lang enum constructor.
     * @param path The string path.
     * @param start The default string.
     */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    public TextComponent toTextComponent() {
        return Main.getInstance().returnChatFormatting(LANG.getString(this.path, def));
    }

    public String toString() {
        return LANG.getString(this.path, def);
    }

    public TextComponent toTextComponentWithPrefix(@Nullable Map<String, String> placeholders) {

        String[] var1 = {Lang.LANG.getString(Lang.PREFIX.path, Lang.PREFIX.def) + LANG.getString(this.path, def)};
        if (placeholders != null) {
            placeholders.forEach((k, v) -> var1[0] = var1[0].replace(k, v));
        }

        return Main.getInstance().returnChatFormatting(var1[0]);
    }

    /**
     * Get the default value of the path.
     * @return The default value of the path.
     */
    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }
}
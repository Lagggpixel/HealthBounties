package me.lagggpixel.healthbounties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import me.lagggpixel.healthbounties.commands.AdminCommands.AdminBaseCommand;
import me.lagggpixel.healthbounties.commands.PlayerCommands.PlayerBaseCommand;
import me.lagggpixel.healthbounties.listeners.*;
import me.lagggpixel.healthbounties.modules.coins.CoinRecipe;
import me.lagggpixel.healthbounties.modules.lang.Lang;
import me.lagggpixel.healthbounties.modules.menu.PlayerMenuUtility;
import me.lagggpixel.healthbounties.modules.revive.ReviveRecipe;
import me.lagggpixel.healthbounties.modules.vouchers.VoucherBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public final class Main extends JavaPlugin {

    private static Main INSTANCE;

    private Gson bounty_gson;
    private Map<UUID, Integer> bountyMap;

    private Gson voucher_gson;
    private Map<UUID, Integer> voucherMap;


    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    private Integer defaultBounty;
    private Integer maxBounties;
    private Integer defaultUnbanBounty;
    private Integer bountiesPerHeart;
    private Integer playerKillBounties;
    private Boolean environmentDeathBountyDeduction;
    private Integer minHearts;
    private Integer maxHearts;

    public YamlConfiguration LANG;
    public File LANG_FILE;

    @Override
    public void onEnable() {

        INSTANCE = this;

        loadConfig();
        registerListeners();
        registerCommands();
        setupRecipes();
    }

    @Override
    public void onDisable() {
        saveData();
    }

    private void registerListeners() {
        new PlayerJoinListener();
        new PlayerDeathListener();
        new PlayerRespawnListener();
        new InventoryClickListener();
        new PlayerInteractListener();
    }

    private void registerCommands() {
        new AdminBaseCommand();
        new PlayerBaseCommand();
    }

    private void loadConfig() {
        //<editor-fold desc="Load default config.yml">
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        this.defaultBounty = this.getConfig().getInt("default-bounties", 1000);
        this.maxBounties = this.getConfig().getInt("max-bounties", 4000);
        this.defaultUnbanBounty = this.getConfig().getInt("unban-bounties",1000);
        this.bountiesPerHeart = this.getConfig().getInt("bounties-per-heart", 300);
        this.playerKillBounties = this.getConfig().getInt("player-kill-bounties", 100);
        this.environmentDeathBountyDeduction = this.getConfig().getBoolean("environmental-kill-bounty-deduction", true);
        this.minHearts = this.getConfig().getInt("min-hearts", 10);
        this.maxHearts = this.getConfig().getInt("max-hearts", 20);
        //</editor-fold>

        //<editor-fold desc="Load lang configuration">
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            try {
                getDataFolder().mkdir();
                langFile.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(langFile);
                    defConfig.save(langFile);
                    Lang.setFile(defConfig);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Main.getInstance().getServer().getLogger().severe("Couldn't create language file.");
                Main.getInstance().getServer().getLogger().severe("This is a fatal error. Now disabling");
                this.onDisable();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
        for (Lang item : Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        Main.getInstance().LANG = conf;
        Main.getInstance().LANG_FILE = langFile;
        try {
            conf.save(Main.getInstance().getLANG_FILE());
        } catch (IOException e) {
            Main.getInstance().getServer().getLogger().log(Level.WARNING, "Failed to save lang.yml.");
            Main.getInstance().getServer().getLogger().log(Level.WARNING, "Report this stack trace to the plugin creator.");
            e.printStackTrace();
        }
        //</editor-fold>

        //<editor-fold desc="Load voucher data">
        voucher_gson = new GsonBuilder().setPrettyPrinting().create();
        File voucherDataFile = new File(getDataFolder()+"/data/", "voucher_data.json");
        if (!voucherDataFile.exists()) {
            try {
                voucherDataFile.getParentFile().mkdirs();
                voucherDataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            voucherMap = new HashMap<>();
        }
        else {
            try (FileReader reader = new FileReader(voucherDataFile)) {
                voucherMap = voucher_gson.fromJson(reader, new TypeToken<Map<UUID, Integer>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
                voucherMap = new HashMap<>();
            }
        }
        //</editor-fold>

        //<editor-fold desc="Load bounty data">
        bounty_gson = new GsonBuilder().setPrettyPrinting().create();
        File bountyDataFile = new File(getDataFolder()+"/data/", "bounty_data.json");
        if (!bountyDataFile.exists()) {
            try {
                bountyDataFile.getParentFile().mkdirs();
                bountyDataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bountyMap = new HashMap<>();
        }
        else {
            try (FileReader reader = new FileReader(bountyDataFile)) {
                bountyMap = bounty_gson.fromJson(reader, new TypeToken<Map<UUID, Integer>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
                bountyMap = new HashMap<>();
            }
        }
        //</editor-fold>


        //<editor-fold desc="Load bounty card/voucher configuration files">
        VoucherBuilder.loadConfiguration();
        //</editor-fold>
    }

    private void setupRecipes() {
        CoinRecipe.loadConfiguration();
        CoinRecipe.registerCoinRecipe();
        ReviveRecipe.loadConfiguration();
        ReviveRecipe.registerReviveRecipe();
    }

    private void saveVoucherData() {
        File dataFile = new File(getDataFolder()+"/data/", "voucher_data.json");

        try (FileWriter writer = new FileWriter(dataFile)) {
            voucher_gson.toJson(voucherMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveBountyData() {
        File dataFile = new File(getDataFolder()+"/data/", "bounty_data.json");

        try (FileWriter writer = new FileWriter(dataFile)) {
            bounty_gson.toJson(bountyMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        saveVoucherData();
        saveBountyData();
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public @NotNull TextComponent returnChatFormatting(String string) {
        return LegacyComponentSerializer.legacy('&').deserialize(string);
    }

    public PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) {

            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p);
        }
    }


}

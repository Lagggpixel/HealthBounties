package me.lagggpixel.healthbounties.modules.vouchers;

import me.lagggpixel.healthbounties.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class VoucherBuilder {
    private static final NamespacedKey voucherUuidNameSpacedKey = new NamespacedKey(Main.getInstance(), "voucher_uuid");
    private static final NamespacedKey voucherAmountNameSpacedKey = new NamespacedKey(Main.getInstance(), "voucher_amount");

    private static final String fileName = "bounty_voucher.yml";
    private static Configuration config;

    public static void loadConfiguration() {
        File configFile = new File(Main.getInstance().getDataFolder(), fileName);

        if (!configFile.exists()) {
            Main.getInstance().saveResource(fileName, false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static ItemStack createVoucher(UUID uuid, int amount) {

        TextComponent itemName = Main.getInstance().returnChatFormatting(config.getString("name"));
        List<String> itemLoreString = config.getStringList("lore");
        ArrayList<Component> itemLoreComponent = new ArrayList<>();
        for (String var1 : itemLoreString) {
            itemLoreComponent.add(Main.getInstance().returnChatFormatting(var1));
        }

        ItemStack voucherItem = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(config.getString("material")))));
        ItemMeta voucherMeta = voucherItem.getItemMeta();

        voucherMeta.displayName(itemName);
        voucherMeta.lore(itemLoreComponent);

        voucherMeta.getPersistentDataContainer().set(voucherUuidNameSpacedKey, PersistentDataType.STRING, uuid.toString());
        voucherMeta.getPersistentDataContainer().set(voucherAmountNameSpacedKey, PersistentDataType.INTEGER, amount);

        voucherItem.setItemMeta(voucherMeta);

        Main.getInstance().getVoucherMap().put(uuid, amount);

        return voucherItem;
    }

    @NotNull
    public static Boolean isVoucher(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return false;
        if (itemStack.getItemMeta().getPersistentDataContainer().get(voucherUuidNameSpacedKey, PersistentDataType.STRING) == null) return false;
        if (itemStack.getItemMeta().getPersistentDataContainer().get(voucherAmountNameSpacedKey, PersistentDataType.INTEGER) == null) return false;
        else return true;
    }

    @Nullable
    public static UUID getVoucherUUID(ItemStack itemStack) {
        String strUUID = itemStack.getItemMeta().getPersistentDataContainer().get(voucherUuidNameSpacedKey, PersistentDataType.STRING);
        if (strUUID == null) return null;
        else return UUID.fromString(strUUID);
    }

    @Nullable
    public static Integer getVoucherAmount(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(voucherAmountNameSpacedKey, PersistentDataType.INTEGER);
    }

}

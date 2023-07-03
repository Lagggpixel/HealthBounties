package me.lagggpixel.healthbounties.modules.revive;

import me.lagggpixel.healthbounties.Main;
import me.lagggpixel.healthbounties.modules.revive.ReviveManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReviveRecipe {

    private static final String fileName = "revive_recipe.yml";
    private static Configuration config;

    public static void loadConfiguration() {
        File configFile = new File(Main.getInstance().getDataFolder(), fileName);

        if (!configFile.exists()) {
            Main.getInstance().saveResource(fileName, false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void registerReviveRecipe() {
        //<editor-fold desc="Load craft result from configuration">
        Material productMaterial = Material.getMaterial(Objects.requireNonNull(config.getString("product")));
        String name = config.getString("name");
        List<String> loreString = config.getStringList("lore");
        List<Component> loreComponent = new ArrayList<>();
        for (String s : loreString) {
            loreComponent.add(Main.getInstance().returnChatFormatting(s));
        }

        assert productMaterial != null;
        ItemStack itemStack = new ItemStack(productMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Main.getInstance().returnChatFormatting(name));
        itemMeta.lore(loreComponent);
        itemMeta.getPersistentDataContainer().set(ReviveManager.reviveItemNameSpacedKey, PersistentDataType.STRING, "reviveItem");
        itemStack.setItemMeta(itemMeta);
        //</editor-fold>


        //<editor-fold desc="Load and register recipe">
        String[] recipeShape = config.getStringList("recipe").toArray(new String[0]);
        ShapedRecipe recipe = new ShapedRecipe(ReviveManager.reviveRecipeNameSpacedKey, itemStack);
        recipe.shape(recipeShape);
        HashMap<Character, Material> ingredients = new HashMap<>();
        ConfigurationSection ingredientsSection = config.getConfigurationSection("ingredients_key");
        assert ingredientsSection != null;
        for (String key : ingredientsSection.getKeys(false)) {
            ingredients.putIfAbsent(key.charAt(0), Material.valueOf(ingredientsSection.getString(key)));
        }

        ingredients.forEach(recipe::setIngredient);

        Bukkit.addRecipe(recipe);
        //</editor-fold>

    }


}

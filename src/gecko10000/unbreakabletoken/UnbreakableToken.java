package gecko10000.unbreakabletoken;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class UnbreakableToken extends JavaPlugin {

    final MiniMessage miniMessage = MiniMessage.miniMessage();
    final NamespacedKey tokenKey = new NamespacedKey(this, "unbreakable_token");

    @Override
    public void onEnable() {
        reloadConfig();
        new Listeners(this);
        new CommandHandler().register();
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
    }

    ItemStack getItem() {
        ItemStack item = new ItemStack(Material.getMaterial(getConfig().getString("item.material")));
        ItemMeta meta = item.getItemMeta();
        meta.displayName(miniMessage.deserialize(getConfig().getString("item.name")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        List<Component> lore = getConfig().getStringList("item.lore").stream().map(s -> miniMessage.deserialize(s).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)).toList();
        meta.lore(lore);
        meta.setEnchantmentGlintOverride(getConfig().getBoolean("item.is_enchanted"));
        meta.setUnbreakable(getConfig().getBoolean("item.is_unbreakable"));
        meta.getPersistentDataContainer().set(tokenKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

}

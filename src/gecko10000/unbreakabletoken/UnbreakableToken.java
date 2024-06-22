package gecko10000.unbreakabletoken;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.Name;
import java.util.List;
import java.util.stream.Collectors;

public class UnbreakableToken extends JavaPlugin {

    final MiniMessage miniMessage = MiniMessage.miniMessage();
    final NamespacedKey tokenKey = new NamespacedKey(this, "unbreakable_token");

    @Override
    public void onEnable() {
        new Listeners(this);
        new CommandHandler(this);
    }

    ItemStack getItem() {
        ItemStack item = new ItemStack(Material.getMaterial(getConfig().getString("item.material")));
        ItemMeta meta = item.getItemMeta();
        meta.displayName(miniMessage.deserialize(getConfig().getString("item.name")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        List<Component> lore = getConfig().getStringList("item.lore").stream().map(s -> miniMessage.deserialize(s).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)).toList();
        meta.lore(lore);
        if (getConfig().getBoolean("is_enchanted")) {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (getConfig().getBoolean("is_unbreakable")) {
            meta.setUnbreakable(true);
        }
        meta.getPersistentDataContainer().set(tokenKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

}

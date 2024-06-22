package gecko10000.unbreakabletoken;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.itemutils.ItemUtils;

public class CommandHandler {

    private final UnbreakableToken plugin;

    public CommandHandler(UnbreakableToken plugin) {
        this.plugin = plugin;
        new CommandParser(plugin.getResource("command.rdcml")).parse().register(plugin.getName().toLowerCase(), this);
    }

    @CommandHook("give")
    public void give(CommandSender sender, Player player, int amount) {
        ItemUtils.give(player, plugin.getItem().asQuantity(amount));
        sender.sendMessage(
                plugin.miniMessage.deserialize(
                        "<green>Gave <player> " +
                                (amount == 1 ? "an unbreakable token." : "<amount> unbreakable tokens."),
                        Placeholder.component("player", player.name()),
                        Placeholder.unparsed("amount", String.valueOf(amount))
                )
        );
    }

}

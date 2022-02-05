package xiaokun.forge.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.Config;
import xiaokun.forge.util.CreateInventory;
import xiaokun.forge.util.Message;


/**
 * 打开锻造界面命令
 * @author 小坤
 */
public class OpenCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dz.use")) {
            Message.playerMessage(sender, Message.getMessage("NoPermission"));
            return;
        }
        if (sender instanceof Player) {
            Inventory inv;
            if (Config.isHistoryForge()){
                inv = CreateInventory.getInventory(0,(Player) sender);
            }else {
                inv = CreateInventory.getInventory(1, (Player) sender);
            }
            ((Player) sender).openInventory(inv);
        } else {
            Message.sendMessage(Message.getMessage("NotExecuted"));
        }
    }
}

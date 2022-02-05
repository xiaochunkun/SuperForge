package xiaokun.forge.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.Config;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.config.LevelConfig;
import xiaokun.forge.config.PlayerData;
import xiaokun.forge.util.Message;

/**
 * 重载配置文件命令
 * @author 小坤
 */
public class ReloadCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("dz.reload")) {
            Message.playerMessage(sender, Message.getMessage("NoPermission"));
            return;
        }else {
            Config.loadConfig();
            ItemConfig.loadConfig();
            Message.loadConfig();
            LevelConfig.loadConfig();
            if (sender instanceof Player) {
                Message.playerMessage(sender, Message.getMessage("Reload"));
            }else {
                Message.sendMessage(Message.getMessage("Reload"));
            }
        }
    }
}

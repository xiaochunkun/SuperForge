package xiaokun.forge.command;

import com.google.common.collect.Maps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xiaokun.forge.command.subcommand.*;
import xiaokun.forge.util.Message;

import java.util.Map;

/**
 * @author 小坤
 */
public class CommandHandler implements CommandExecutor {
    private static Map<String, SubCommand> subCommandMap = Maps.newHashMap();

    /**
     * 监听命令启动
     */
    public CommandHandler() {
        registerCommand("open", new OpenCommand());
        registerCommand("reload", new ReloadCommand());
        registerCommand("help", new HelpCommand());
        registerCommand("map", new MapCommand());
        registerCommand("material", new MaterialCommand());
        registerCommand("item", new ItemCommand());
        registerCommand("level", new LevelCommand());
        registerCommand("study",new StudyCommand());
        registerCommand("console",new ConsoleCommand());
    }

    /**
     * 注册指令
     */
    private static void registerCommand(String subCommandName, SubCommand subCommand) {
        if (subCommandMap.containsKey(subCommandName)) {
            Message.sendMessage("§f发现重复注册指令!");
        }
        subCommandMap.put(subCommandName, subCommand);
    }

    /**
     * 接收到命令
     *
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            subCommandMap.get("help").execute(sender, args);
            return true;
        }

        if (!subCommandMap.containsKey(args[0])) {
            Message.playerMessage(sender, "§c未知指令!");
            return true;
        }

        SubCommand subCommand = subCommandMap.get(args[0]);
        subCommand.execute(sender, args);
        return true;
    }
}

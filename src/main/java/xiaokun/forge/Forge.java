package xiaokun.forge;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xiaokun.forge.api.PAPI;
import xiaokun.forge.command.CommandHandler;
import xiaokun.forge.config.Config;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.config.LevelConfig;
import xiaokun.forge.listener.ClickEvent;
import xiaokun.forge.listener.InteractEvent;
import xiaokun.forge.util.Message;

import java.io.File;
import java.util.stream.IntStream;

/**
 * @author 小坤
 */
public class Forge extends JavaPlugin {

    @Getter
    private static final int[] versionSplit = new int[3];

    @Getter
    private static JavaPlugin plugin;

    @Getter
    private static String pluginName;

    @Getter
    private static File pluginFile;

    @Getter
    private static boolean mm;

    @Override
    public void onEnable() {

        plugin = this;
        pluginName = plugin.getName();
        pluginFile = this.getDataFolder();
        String version = Bukkit.getBukkitVersion().split("-")[0].replace(" ", "");
        String[] strSplit = version.split("[.]");
        IntStream.range(0, strSplit.length).forEachOrdered(i -> versionSplit[i] = Integer.valueOf(strSplit[i]));


        Message.sendMessage("§a" + pluginName + "已启动");

        Message.sendMessage("当前服务器版本： §a" + version);

        //判断PlaceholderAPI是否存在
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPI(this).register();
            Message.sendMessage("§aFind Placeholders");
        } else {
            Message.sendMessage("§cNo Find PlaceholderAPI!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            mm = true;
            Message.sendMessage("MythicMobs");
        } else {
            Message.sendMessage("Find MythicMobs!");
        }

        //命令注册
        Bukkit.getPluginCommand("dz").setExecutor(new CommandHandler());

        //事件监听
        Bukkit.getPluginManager().registerEvents(new ClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);

        //配置文件加载
        Config.loadConfig();
        ItemConfig.loadConfig();
        Message.loadConfig();
        LevelConfig.loadConfig();

    }
}


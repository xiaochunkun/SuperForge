package xiaokun.forge.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import xiaokun.forge.Forge;
import xiaokun.forge.util.Message;

import java.io.File;
import java.io.IOException;

/**
 * @author 小坤
 */
public class LevelConfig {
    @Getter
    private static File file = new File(Forge.getPluginFile(), "level.yml");

    @Getter
    private static YamlConfiguration yml = new YamlConfiguration();

    /**
     * 加载level.yml
     */
    public static void loadConfig() {
        if (!file.exists()) {
            Forge.getPlugin().saveResource("level.yml", false);
            Message.sendMessage("§a生成默认 level.yml 成功");
        }
        try {
            yml.load(file);
            Message.sendMessage("§a读取 level.yml 成功");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Message.sendMessage("§a读取 level.yml 时发生错误");
        }
    }

    /**
     * 获取一个随机的品质
     *
     * @return
     */
    public static String getQuality() {
        ConfigurationSection section = yml.getConfigurationSection("Quality");
        double sjs = Math.random();
        for (String key : section.getKeys(false)) {
            if ((sjs > section.getDouble(key + ".mix")) && (sjs < section.getDouble(key + ".max"))) {
                return key;
            }
        }
        return null;
    }

    /**
     * 获取该品质的属性计算格式
     *
     * @param level
     * @return
     */
    public static String getAttribute(String level) {
        return yml.getString("Quality." + level + ".attribute");
    }

    /**
     * 获取等级所需要的经验
     * @param level
     * @return
     */
    public static int getExp(String level) {
        return yml.getInt("Level." + level + ".exp");
    }

    public static String getName(String level){
        return yml.getString("Level." + level + ".name");

    }
}

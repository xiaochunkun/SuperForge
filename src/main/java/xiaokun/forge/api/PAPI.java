package xiaokun.forge.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xiaokun.forge.config.LevelConfig;
import xiaokun.forge.config.PlayerData;

/**
 * Placeholder变量注册
 * @author 小坤
 */
public class PAPI extends PlaceholderExpansion {
    private JavaPlugin plugin;

    public PAPI(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "dz";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        if (identifier.equalsIgnoreCase("level")) {
            return String.valueOf(PlayerData.getLevel(player));
        }
        if (identifier.equalsIgnoreCase("exp")) {
            return String.valueOf(PlayerData.getExp(player));
        }
        if (identifier.equalsIgnoreCase("needExp")) {
            if (LevelConfig.getExp(String.valueOf(PlayerData.getLevel(player))) > 0) {
                return String.valueOf(LevelConfig.getExp(String.valueOf(PlayerData.getLevel(player))) - PlayerData.getExp(player));
            } else {
                return "0";
            }
        }
        if (identifier.equalsIgnoreCase("nextLevel")) {
            return String.valueOf(LevelConfig.getExp(String.valueOf(PlayerData.getLevel(player))));
        }
        if (identifier.equalsIgnoreCase("name")) {
            return PlayerData.getLevelName(player);
        }
        return null;
    }
}

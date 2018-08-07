package cn.minezone.nation;


import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author mcard
 */
public class PlaceholderHook extends EZPlaceholderHook {

    private final String NAME = "name";
    private final String NATION_RANK = "nation_rank";
    private final String WORLD_RANK = "world_rank";

    public PlaceholderHook(Plugin plugin) {
        super(plugin, "nation");
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null) {
            return "传入的玩家不能为null";
        }
        if (NAME.equalsIgnoreCase(params)) {
            return NationApi.getPlayerNation(p);
        }
        if (NATION_RANK.equalsIgnoreCase(params)) {
            return NationApi.getPlayerRankInNation(p) + "";
        }
        if (WORLD_RANK.equalsIgnoreCase(params)) {
            return NationApi.getPlayerRankInWorld(p) + "";
        }
        return null;
    }
}

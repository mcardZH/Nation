package cn.minezone.nation;

import org.bukkit.OfflinePlayer;


/**
 * @author mcard
 */
public class NationPlayer implements Comparable<NationPlayer> {

    private OfflinePlayer player;
    private String name;
    private int point;

    public NationPlayer(OfflinePlayer player, String name, int point) {
        this.player = player;
        this.name = name;
        this.point = point;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public int compareTo(NationPlayer o) {
        if (o.getPoint() < point) {
            return -1;
        }
        return 1;
        // -1小于 0等于 1大于
    }
}

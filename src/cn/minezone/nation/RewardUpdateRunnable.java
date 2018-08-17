package cn.minezone.nation;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Date;

/**
 * @author mcard
 */
public class RewardUpdateRunnable extends BukkitRunnable {

    private Plugin plugin;
    private boolean isUpdate = false;

    public RewardUpdateRunnable(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (new Date().getHours() == plugin.getConfig().getInt("update-time", 0) && !isUpdate) {
            isUpdate = true;
            //确认已经更新
        } else if (new Date().getHours() != plugin.getConfig().getInt("update-time", 0)) {
            isUpdate = false;
            return;
        }
        File f = new File(plugin.getDataFolder(), "rewards.yml");
        f.delete();
    }
}

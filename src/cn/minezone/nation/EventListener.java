package cn.minezone.nation;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }
        Player entity = (Player) e.getEntity();
        Player damage = (Player) e.getDamager();

        if (NationAPI.getPlayerNation(entity).equals(NationAPI.getPlayerNation(damage))) {
            e.setCancelled(true);
        }

    }
}

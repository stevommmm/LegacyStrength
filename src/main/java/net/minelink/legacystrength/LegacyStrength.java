package net.minelink.legacystrength;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public final class LegacyStrength extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void recalculateDamage(EntityDamageByEntityEvent event) {
        // Do nothing if event is inherited (hacky way to ignore mcMMO AoE attacks)
        if (event.getClass() != EntityDamageByEntityEvent.class) return;

        // Do nothing if cause of damage isn't an entity attack
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        // Do nothing if damage is not directly from a player
        Entity entity = event.getDamager();
        if (!(entity instanceof Player)) return;

        // Do nothing if player doesn't have the Strength effect
        Player player = (Player) event.getDamager();
        if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) return;

        // Convert the old damage into a new damage with reduced Strength
        event.setDamage(StrengthDamageHelper.convertDamage(player, event.getDamage()));
    }

}

package me.izarak.trailsPlus.events;

import me.izarak.trailsPlus.TrailsPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UniversalEvents implements Listener {

    final TrailsPlus main;

    public UniversalEvents(TrailsPlus main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinInitPlayerTrails(PlayerJoinEvent e) {
        main.loadPlayerData(e.getPlayer());
    }

}

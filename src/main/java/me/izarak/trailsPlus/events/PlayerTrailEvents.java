package me.izarak.trailsPlus.events;

import me.izarak.trailsPlus.PlayerTrails;
import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.ITrailAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTrailEvents implements Listener {

    final TrailsPlus main;

    public PlayerTrailEvents(TrailsPlus main) {
        this.main = main;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PlayerTrails trail = main.getPlayerTrail(onlinePlayer);
                    if (trail == null)
                        continue;
                    trail.getTrailAction().onTick(main, onlinePlayer);
                }
            }
        }.runTaskTimer(main, 0, 5);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerTrails trail = main.getPlayerTrail(player);
        if (trail == null)
            return;
        trail.getTrailAction().onActivate(main, player);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        main.saveSelectedTrails();

        PlayerTrails trail = main.removePlayerTrail(player);
        if (trail != null) {
            ITrailAction action = trail.getTrailAction();
            action.onDeactivate(main, player);
            action.onQuit(main, e);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        PlayerTrails trail = main.getPlayerTrail(player);
        if (trail == null)
            return;
        trail.getTrailAction().onTeleport(main, e);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        PlayerTrails trail = main.getPlayerTrail(player);
        if (trail == null)
            return;
        trail.getTrailAction().onMove(main, e);
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent e) {
        if (e.getAnimationType() != PlayerAnimationType.ARM_SWING)
            return;
        Player player = e.getPlayer();
        PlayerTrails trail = main.getPlayerTrail(player);
        if (trail == null)
            return;
        trail.getTrailAction().onAttack(main, e);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        PlayerTrails trail = main.getPlayerTrail(player);
        if (trail == null)
            return;
        trail.getTrailAction().onDamaged(main, e);
    }

}

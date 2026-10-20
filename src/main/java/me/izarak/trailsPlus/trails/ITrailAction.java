package me.izarak.trailsPlus.trails;

import me.izarak.trailsPlus.TrailsPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public interface ITrailAction {

    void displayTrail(TrailsPlus main, Player player);

    default void onEnable(TrailsPlus main) {}
    default void onDisable(TrailsPlus main) {}

    default void onActivate(TrailsPlus main, Player player) {}
    default void onDeactivate(TrailsPlus main, Player player) {}
    default void onMove(TrailsPlus main, PlayerMoveEvent e) {}
    default void onAttack(TrailsPlus main, PlayerInteractEvent e) {}
    default void onDamaged(TrailsPlus main, EntityDamageEvent e) {}
    default void onTick(TrailsPlus main, Player player) {}
    default void onQuit(TrailsPlus main, PlayerQuitEvent e) {}
    default void onTeleport(TrailsPlus main, PlayerTeleportEvent e) {}

}

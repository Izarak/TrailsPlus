package me.izarak.trailsPlus.trails;

import me.izarak.trailsPlus.TrailsPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public interface IPlayerTrailAction extends ITrailAction {

    default void onMove(TrailsPlus main, PlayerMoveEvent e) {}
    default void onAttack(TrailsPlus main, PlayerAnimationEvent e) {}
    default void onDamaged(TrailsPlus main, EntityDamageEvent e) {}
    default void onQuit(TrailsPlus main, PlayerQuitEvent e) {}
    default void onTeleport(TrailsPlus main, PlayerTeleportEvent e) {}

}

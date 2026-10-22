package me.izarak.trailsPlus.trails;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.projectile.ProjectileTrails;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.List;

public interface IProjectileTrailAction extends ITrailAction {

    default boolean tickAfterHit() {
        return true;
    }

    default List<ProjectileTrails> getIncompatableTrails() {
        return List.of();
    }

    default void onProjectileTick(TrailsPlus main, Projectile projectile) {}
    default void onLaunch(TrailsPlus main, ProjectileLaunchEvent e) {}
    default void onProjectileHit(TrailsPlus main, ProjectileHitEvent e) {}

}

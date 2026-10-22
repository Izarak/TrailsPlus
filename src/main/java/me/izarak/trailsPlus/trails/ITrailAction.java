package me.izarak.trailsPlus.trails;

import me.izarak.trailsPlus.TrailsPlus;
import org.bukkit.entity.Player;

public interface ITrailAction {

    void displayTrail(TrailsPlus main, Player player);

    default void onEnable(TrailsPlus main) {}
    default void onDisable(TrailsPlus main) {}

    /**
     * Called after onEnable
     */
    default void onActivate(TrailsPlus main, Player player) {}
    /**
     * Called Before onDisable
     */
    default void onDeactivate(TrailsPlus main, Player player) {}
    default void onTick(TrailsPlus main, Player player) {}

}

package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class CherryLeavesTrail implements IPlayerTrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        player.getWorld().spawnParticle(Particle.CHERRY_LEAVES, player.getLocation().add(0, player.getHeight() + 1, 0), 1, .3, .1, .3);
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }
}

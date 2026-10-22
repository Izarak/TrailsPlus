package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class HeartsTrail implements IPlayerTrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, player.getHeight() + .5, 0), 1, .5, .25, .5, 0);
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }
}

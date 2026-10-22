package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RainCloudTrail implements IPlayerTrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {

        player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, player.getHeight() + 1, 0), 5, .5, .1, .5, 0, new Particle.DustOptions(Color.GRAY, 2f));
        player.getWorld().spawnParticle(Particle.FALLING_WATER, player.getLocation().add(0, player.getHeight() + 1, 0), 2, .5, .1, .5, 0);

    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }
}

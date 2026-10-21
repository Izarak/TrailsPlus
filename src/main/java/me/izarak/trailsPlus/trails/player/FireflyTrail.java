package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.ITrailAction;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class FireflyTrail implements ITrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        player.getWorld().spawnParticle(Particle.FIREFLY, player.getEyeLocation(), 1);
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }
}

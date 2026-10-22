package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BothOmenTrail implements IPlayerTrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        double halfHeight = player.getHeight() / 2;
        player.getWorld().spawnParticle(Particle.RAID_OMEN, player.getLocation().add(0, halfHeight, 0), 1, .25, halfHeight, .25);
        player.getWorld().spawnParticle(Particle.TRIAL_OMEN, player.getLocation().add(0, halfHeight, 0), 1, .25, halfHeight, .25);
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }

}

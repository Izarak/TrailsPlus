package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FlameStepTrail implements IPlayerTrailAction {
    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        if (player.getLocation().subtract(0, 0.05, 0).getBlock().getType().isAir())
            return;
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, .1, 0), 2, .25, 0, .25, 0);
    }

    @Override
    public void onMove(TrailsPlus main, PlayerMoveEvent e) {
        displayTrail(main, e.getPlayer());
    }

    @Override
    public void onDamaged(TrailsPlus main, EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        player.getWorld().spawnParticle(Particle.LAVA, player.getLocation().add(0, player.getHeight() / 2, 0), 5, .25, player.getHeight() / 3, .25);
    }

    @Override
    public void onQuit(TrailsPlus main, PlayerQuitEvent e) {
        Player player = e.getPlayer();
        double halfHeight = player.getHeight() / 2;
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, halfHeight, 0), 50, .25, halfHeight, .25, .25);
    }
}

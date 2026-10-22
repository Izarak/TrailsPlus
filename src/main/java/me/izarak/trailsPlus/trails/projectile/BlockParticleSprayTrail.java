package me.izarak.trailsPlus.trails.projectile;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IProjectileTrailAction;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlockParticleSprayTrail implements IProjectileTrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
    }

    @Override
    public void onProjectileHit(TrailsPlus main, ProjectileHitEvent e) {
        Block block = e.getHitBlock();
        if (block == null)
            return;
        Location location = e.getEntity().getLocation();
        BlockFace face = e.getHitBlockFace();
        Vector direction = face.getDirection();
        for (int i = 0; i < 20; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    location.getWorld().spawnParticle(Particle.BLOCK, location, 0, direction.getX(), direction.getY(), direction.getZ(), 1, block.getBlockData());
                }
            }.runTaskLater(main, i);
        }
    }
}

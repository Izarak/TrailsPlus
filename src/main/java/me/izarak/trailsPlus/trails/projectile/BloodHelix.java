package me.izarak.trailsPlus.trails.projectile;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IProjectileTrailAction;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class BloodHelix implements IProjectileTrailAction {

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
    }

    @Override
    public void onProjectileTick(TrailsPlus main, Projectile projectile) {
        if (projectile == null || !projectile.isValid() || projectile.isOnGround()) {
            return;
        }

        Location loc = projectile.getLocation().clone();
        Vector velocity = projectile.getVelocity();
        double yaw = Math.toRadians(loc.getYaw());
        double pitch = Math.toRadians(loc.getPitch());
        double radius = 0.6;

        int interpolationSteps = 15;

        for (int step = 0; step < interpolationSteps; step++) {
            double t = (double) step / interpolationSteps;

            double backX = loc.getX() - (velocity.getX() * 5.0 * t);
            double backY = loc.getY() - (velocity.getY() * 5.0 * t);
            double backZ = loc.getZ() - (velocity.getZ() * 5.0 * t);
            double historicalTicksLived = projectile.getTicksLived() - (5.0 * t);
            double rotationOffset = historicalTicksLived * 0.75;
            double angle = (2 * Math.PI) + rotationOffset;
            double initialX = Math.cos(angle) * radius;
            double initialY = Math.sin(angle) * radius;
            double initialZ = 0;
            double pitchY = initialY * Math.cos(pitch) - initialZ * Math.sin(pitch);
            double pitchZ = initialY * Math.sin(pitch) + initialZ * Math.cos(pitch);
            double finalX = initialX * Math.cos(yaw) + pitchZ * Math.sin(yaw);
            double finalZ = -initialX * Math.sin(yaw) + pitchZ * Math.cos(yaw);
            Location particleLoc = new Location(
                    loc.getWorld(),
                    backX + finalX,
                    backY + pitchY,
                    backZ + finalZ
            );
            Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.RED, 1.5F);
            loc.getWorld().spawnParticle(Particle.DUST, particleLoc, 1, 0.0, 0.0, 0.0, 0.0, dustOptions, true);
        }
    }

}

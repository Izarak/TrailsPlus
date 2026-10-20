package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.ITrailAction;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ContagionTrail implements ITrailAction {

    final double range = 1.5;

    final Map<LivingEntity, Long> infectedEntities = new HashMap<>();

    @Override
    public void onEnable(TrailsPlus main) {
        final long runnablePeriod = 5;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = infectedEntities.keySet().size(); i > 0; i--) {
                    LivingEntity entity = (LivingEntity) infectedEntities.keySet().toArray()[i - 1];
                    displayTrail(main, entity);
                    long timeRemaining = infectedEntities.get(entity);
                    infectedEntities.put(entity, timeRemaining - runnablePeriod);
                    infectNearby(entity, timeRemaining);
                    if (timeRemaining <= 0)
                        infectedEntities.remove(entity);
                }
            }
        }.runTaskTimer(main, 0, runnablePeriod);
    }

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        displayTrail(main, (LivingEntity) player);
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
        infectNearby(player, 20 * 15);
    }

    private void infectNearby(LivingEntity entity, long time) {
        for (Entity nearbyEntity : entity.getNearbyEntities(range, range, range)) {
            if (!(nearbyEntity instanceof LivingEntity livingEntity) || livingEntity.getType() == EntityType.ARMOR_STAND)
                continue;
            infectedEntities.put(livingEntity, time);
        }
    }

    private void displayTrail(TrailsPlus main, LivingEntity entity) {
        double halfHeight = entity.getHeight() / 2;
        entity.getWorld().spawnParticle(Particle.EFFECT, entity.getLocation().add(0, halfHeight, 0), 2, 0, halfHeight / 2, 0, new Particle.Spell(Color.GREEN, 1f));
    }

}

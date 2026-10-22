package me.izarak.trailsPlus.events;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.projectile.ProjectileTrails;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ProjectileTrailEvents implements Listener {

    final TrailsPlus main;

    final NamespacedKey attachedTrailsKey,
            shooterKey;

    final Set<Projectile> trackedProjectiles = new HashSet<>();

    public ProjectileTrailEvents(TrailsPlus main) {
        this.main = main;
        attachedTrailsKey = new NamespacedKey(main, "attached_projectile_trails");
        shooterKey = new NamespacedKey(main, "attached_projectile_trails_shooter");

        new BukkitRunnable() {
            @Override
            public void run() {
                List<Projectile> projectileList = trackedProjectiles.stream().toList();
                for (int size = projectileList.size(); size > 0; size--) {
                    Projectile projectile = projectileList.get(size - 1);
                    if (!projectile.isValid()) {
                        trackedProjectiles.remove(projectile);
                        continue;
                    }
                    PersistentDataContainer pdc = projectile.getPersistentDataContainer();
                    if (!pdc.has(attachedTrailsKey, PersistentDataType.STRING) || !pdc.has(shooterKey, PersistentDataType.STRING)) {
                        trackedProjectiles.remove(projectile);
                        continue;
                    }
                    OfflinePlayer shooter = Bukkit.getOfflinePlayer(UUID.fromString(pdc.get(shooterKey, PersistentDataType.STRING)));
                    Set<ProjectileTrails> projectileTrailsList = new HashSet<>();

                    for (String str : pdc.get(attachedTrailsKey, PersistentDataType.STRING).split(",")) {
                        projectileTrailsList.add(ProjectileTrails.valueOf(str));
                    }

                    projectileTrailsList.forEach(trail -> trail.getTrailAction().onProjectileTick(main, projectile));

                    if (!shooter.isOnline())
                        continue;

                    main.getProjectileTrails(shooter.getPlayer()).forEach(trail -> trail.getTrailAction().onTick(main, shooter.getPlayer()));
                }
            }
        }.runTaskTimer(main, 0, 5);

    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile.getShooter() instanceof Player player))
            return;
        List<String> trailNames = new ArrayList<>();
        main.getProjectileTrails(player).forEach(trail -> {
            trail.getTrailAction().onLaunch(main, e);
            trailNames.add(trail.toString());
        });
        projectile.getPersistentDataContainer().set(attachedTrailsKey, PersistentDataType.STRING, String.join(",", trailNames));
        projectile.getPersistentDataContainer().set(shooterKey, PersistentDataType.STRING, player.getUniqueId().toString());

        trackedProjectiles.add(projectile);
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        PersistentDataContainer pdc = projectile.getPersistentDataContainer();
        if (!pdc.has(attachedTrailsKey, PersistentDataType.STRING) || !pdc.has(shooterKey, PersistentDataType.STRING))
            return;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(pdc.get(shooterKey, PersistentDataType.STRING)));

        if (!offlinePlayer.isOnline())
            return;

        main.getProjectileTrails(offlinePlayer.getPlayer()).forEach(trail -> trail.getTrailAction().onProjectileHit(main, e));
    }



}

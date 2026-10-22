package me.izarak.trailsPlus.trails.projectile;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IProjectileTrailAction;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.List;

import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.*;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_FUCHSIA;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_GRAY;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_GREEN;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_LIME;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_MAROON;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_NAVY;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_OLIVE;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_ORANGE;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_PURPLE;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_RED;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_SILVER;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_TEAL;
import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.ARROW_YELLOW;

public class ArrowPurpleTrail implements IProjectileTrailAction {
    @Override
    public void onLaunch(TrailsPlus main, ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow))
            return;
        arrow.setColor(Color.PURPLE);
    }

    @Override
    public List<ProjectileTrails> getIncompatableTrails() {
        return List.of(
                ARROW_AQUA,
                ARROW_BLACK,
                ARROW_BLUE,
                ARROW_FUCHSIA,
                ARROW_GRAY,
                ARROW_GREEN,
                ARROW_LIME,
                ARROW_MAROON,
                ARROW_NAVY,
                ARROW_OLIVE,
                ARROW_ORANGE,
                ARROW_RED,
                ARROW_SILVER,
                ARROW_TEAL,
                ARROW_YELLOW
        );
    }

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
    }
}
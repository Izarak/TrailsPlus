package me.izarak.trailsPlus.trails.projectile;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IProjectileTrailAction;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.List;

import static me.izarak.trailsPlus.trails.projectile.ProjectileTrails.*;

public class ArrowAquaTrail implements IProjectileTrailAction {
    @Override
    public void onLaunch(TrailsPlus main, ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow))
            return;
        arrow.setColor(Color.AQUA);
    }

    @Override
    public List<ProjectileTrails> getIncompatableTrails() {
        return List.of(
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
                ARROW_PURPLE,
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

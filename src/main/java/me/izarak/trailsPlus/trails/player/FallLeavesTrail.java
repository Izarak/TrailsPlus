package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.ITrailAction;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FallLeavesTrail implements ITrailAction {

    List<Color> colors = List.of(
            Color.ORANGE,
            Color.YELLOW,
            Color.fromRGB(255, 17, 0),
            Color.fromRGB(212, 255, 0)
    );

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        player.getWorld().spawnParticle(Particle.TINTED_LEAVES, player.getLocation().add(0, player.getHeight() + 1, 0), 1, .3, .1, .3, colors.get(ThreadLocalRandom.current().nextInt(colors.size())));
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }
}

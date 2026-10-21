package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.ITrailAction;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpitTrail implements ITrailAction {

    private final Map<UUID, Long> lastSpit = new HashMap<>();

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        if (!player.isSneaking())
            return;
        long currentTime = System.currentTimeMillis();
        if (lastSpit.containsKey(player.getUniqueId())) {
            if (currentTime - lastSpit.get(player.getUniqueId()) < 250)
                return;
            lastSpit.remove(player.getUniqueId());
        }
        Vector dir = player.getLocation().getDirection();
        for (int i = 0; i < 4; i++) {
            player.getWorld().spawnParticle(Particle.SPIT, player.getEyeLocation(), 0, dir.getX(), dir.getY(), dir.getZ(), 1);
        }
        player.getWorld().playSound(player, Sound.ENTITY_LLAMA_SPIT, .1f, 1);
        player.getWorld().playSound(player, Sound.ENTITY_FOX_SPIT, 1, 1);

        lastSpit.put(player.getUniqueId(), currentTime);
    }

    @Override
    public void onAttack(TrailsPlus main, PlayerAnimationEvent e) {
        displayTrail(main, e.getPlayer());
    }

    @Override
    public void onDeactivate(TrailsPlus main, Player player) {
        lastSpit.remove(player.getUniqueId());
    }
}

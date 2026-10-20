package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.ITrailAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class HauntedLanternTrail implements ITrailAction {

    private final Map<Player, HauntedBlock> playerHauntedBlockMap = new HashMap<>();

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
    }

    @Override
    public void onDisable(TrailsPlus main) {
        for (Player player : playerHauntedBlockMap.keySet()) {
            onDeactivate(main, player);
        }
    }

    final double slowRadius = Math.pow(4, 2),
            maxSpeed = 2,
            minSpeed = .1;

    @Override
    public void onTick(TrailsPlus main, Player player) {
        HauntedBlock hauntedBlock = playerHauntedBlockMap.get(player);
        Location loc = player.getEyeLocation().add(-.5, 1.1, -.5);
        hauntedBlock.setTarget(loc);

        double distance = loc.distanceSquared(hauntedBlock.getDisplay().getLocation()),
                speed;

        if (distance < slowRadius) {
            speed = maxSpeed * (distance / slowRadius);
        } else
            speed = maxSpeed;

        speed = Math.min(maxSpeed, Math.max(minSpeed, speed));

        hauntedBlock.moveTowardsTarget(speed, distance < .05);
        hauntedBlock.getDisplay().getWorld().spawnParticle(Particle.SMOKE, hauntedBlock.getDisplay().getLocation(), 2, .05, .05, .05, 0);
    }

    @Override
    public void onTeleport(TrailsPlus main, PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        HauntedBlock hauntedBlock = playerHauntedBlockMap.get(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                hauntedBlock.setTarget(player.getEyeLocation());
                hauntedBlock.moveTowardsTarget(1, true);
            }
        }.runTaskLater(main, 20);
    }

    @Override
    public void onActivate(TrailsPlus main, Player player) {
        BlockData data = Bukkit.createBlockData(Material.LANTERN);
        if (data instanceof Lantern lantern)
            lantern.setHanging(true);
        HauntedBlock hauntedBlock = new HauntedBlock(player, data);
        playerHauntedBlockMap.put(player, hauntedBlock);
    }

    @Override
    public void onDeactivate(TrailsPlus main, Player player) {
        if (playerHauntedBlockMap.containsKey(player))
            playerHauntedBlockMap.remove(player).display.remove();
    }

    public class HauntedBlock {
        final Player player;
        final BlockDisplay display;

        private Location target;
        private Vector offset = new Vector(0, 0, 0);

        public HauntedBlock(Player player, BlockData data) {
            this.player = player;
            target = player.getEyeLocation();
            target.setYaw(0);
            target.setPitch(0);
            display = player.getWorld().spawn(target, BlockDisplay.class, blockDisplay -> {
                blockDisplay.setBlock(data);
                blockDisplay.setTransformation(new Transformation(new Vector3f(-.5f, -.5f, -.5f), new AxisAngle4f(), new Vector3f(1, 1, 1), new AxisAngle4f()));
                blockDisplay.setTeleportDuration(5);
            });
            randomOffset();
        }

        public void moveTowardsTarget(double speed, boolean now) {
            Vector direction = target.toVector().subtract(display.getLocation().toVector()).normalize();
            Location loc = (now ? target : display.getLocation().add(direction.multiply(speed)));
            if (now)
                randomOffset();
            display.teleport(loc);
        }

        final double randomOffsetRange = 1;

        public void randomOffset() {
            offset = new Vector(ThreadLocalRandom.current().nextDouble(-randomOffsetRange, randomOffsetRange),
                    ThreadLocalRandom.current().nextDouble(-randomOffsetRange, randomOffsetRange),
                    ThreadLocalRandom.current().nextDouble(-randomOffsetRange, randomOffsetRange));
        }

        public Vector getOffset() {
            return offset;
        }

        public BlockDisplay getDisplay() {
            return display;
        }

        public Location getTarget() {
            return target;
        }

        public void setTarget(Location target) {
            target.add(offset);
            target.setPitch(0);
            target.setYaw(0);
            this.target = target;
        }
    }

}

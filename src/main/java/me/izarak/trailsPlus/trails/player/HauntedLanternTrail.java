package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class HauntedLanternTrail implements IPlayerTrailAction {

    private final Map<Player, HauntedBlock> playerHauntedBlockMap = new HashMap<>();

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
    }

    final double slowRadius = Math.pow(4, 2),
            maxSpeed = 2,
            minSpeed = .1;

    @Override
    public void onTick(TrailsPlus main, Player player) {
        HauntedBlock hauntedBlock = playerHauntedBlockMap.get(player);
        if (hauntedBlock == null)
            return;
        Location loc = player.getEyeLocation().add(-.5, 1.1, -.5);
        hauntedBlock.setTarget(loc);

        double distance,
                speed;

        try {
            distance = loc.distanceSquared(hauntedBlock.getDisplay().getLocation());
        } catch (IllegalArgumentException exception) {
            return; // If the player is in the void(teleporting)
        }

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
                if (isPlayerReadyInWorld(player)) {
                    hauntedBlock.setTarget(player.getEyeLocation());
                    hauntedBlock.moveTowardsTarget(1, true);
                    cancel();
                }
            }
        }.runTaskTimer(main, 20, 5);
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
        if (playerHauntedBlockMap.containsKey(player)) {
            HauntedBlock hauntedBlock = playerHauntedBlockMap.remove(player);
            hauntedBlock.display.remove();
            if (hauntedBlock.lastLightBlock != null)
                hauntedBlock.lastLightBlock.setBlockData(hauntedBlock.lastLightBlockData);
            playerHauntedBlockMap.remove(player);
        }
    }

    @Override
    public void onQuit(TrailsPlus main, PlayerQuitEvent e) {
        Player player = e.getPlayer();
        onDeactivate(main, player);
    }

    private boolean isPlayerReadyInWorld(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null)
            return false;

        int chunkX = loc.getBlockX() >> 4;
        int chunkZ = loc.getBlockZ() >> 4;

        if (!world.isChunkLoaded(chunkX, chunkZ))
            return false;

        return world.getChunkAt(chunkX, chunkZ).isEntitiesLoaded();
    }

    public class HauntedBlock {
        final Player player;
        final BlockDisplay display;

        private Location target;
        private Vector offset = new Vector(0, 0, 0);

        private float currentRotation = ThreadLocalRandom.current().nextFloat(360),
                rotationSpeed = ThreadLocalRandom.current().nextFloat(5, 10);

        public HauntedBlock(Player player, BlockData data) {
            this.player = player;
            if (ThreadLocalRandom.current().nextBoolean())
                rotationSpeed *= -1;
            target = player.getEyeLocation();
            target.setYaw(currentRotation);
            target.setPitch(0);
            display = player.getWorld().spawn(target, BlockDisplay.class, blockDisplay -> {
                blockDisplay.setBlock(data);
                blockDisplay.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(1, 1, 1), new AxisAngle4f(0, 0, 1, 0)));
                blockDisplay.setTeleportDuration(5);
                blockDisplay.setInterpolationDuration(5);
            });
            randomOffset();
        }

        private Block lastLightBlock;
        private BlockData lastLightBlockData;

        public void moveTowardsTarget(double speed, boolean now) {
            if (lastLightBlock != null && lastLightBlockData != null) {
                lastLightBlock.setBlockData(lastLightBlockData);
                lastLightBlock = null;
                lastLightBlockData = null;
            }

            if (display.getLocation().getBlock().getType().isAir()) {
                lastLightBlock = display.getLocation().getBlock();
                lastLightBlockData = lastLightBlock.getBlockData();
                lastLightBlock.setType(Material.LIGHT);
            }

            rotationSpeed = Math.max(-10, Math.min(10, rotationSpeed + ThreadLocalRandom.current().nextFloat(-4, 4)));

            currentRotation += rotationSpeed;

            float angleInRadians = (float) Math.toRadians(currentRotation);

            Matrix4f matrix = new Matrix4f()
                    .translation(0f, 0f, 0f)                      // 3. Final position offset
                    .rotateY(angleInRadians)                      // 2. Rotate around the Y-axis
                    .translate(-0.5f, -0.5f, -0.5f)               // 1. Move pivot from corner to center
                    .scale(1f, 1f, 1f);                           // 0. Apply size scaling

            display.setTransformationMatrix(matrix);

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

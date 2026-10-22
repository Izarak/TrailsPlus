package me.izarak.trailsPlus.trails.player;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.IPlayerTrailAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class FlowerPowerTrail implements IPlayerTrailAction {

    private final Set<BlockDisplay> displays = new HashSet<>();

    private final List<Material> flowerMaterials = List.of(
            Material.POPPY,
            Material.DANDELION,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.PINK_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE,
            Material.TORCHFLOWER
    );

    final double radius = 2.5;

    @Override
    public void displayTrail(TrailsPlus main, Player player) {
        Location loc = player.getLocation().add(0, player.getHeight(), 0);
        loc.setYaw(ThreadLocalRandom.current().nextFloat(360));
        loc.setPitch(0);
        loc.add(ThreadLocalRandom.current().nextDouble(-radius, radius), 0, ThreadLocalRandom.current().nextDouble(-radius, radius));

        Location testLoc = loc.clone();
        testLoc.setY(Math.min(testLoc.getWorld().getMaxHeight(), player.getEyeLocation().getY() + 1));

        double targetY = player.getLocation().getY() - 5;
        boolean foundFloor = false;
        for (int y = testLoc.getBlockY(); y >= Math.floor(player.getLocation().getY() - 5); y--) {
            Block block = testLoc.getWorld().getBlockAt(testLoc.getBlockX(), y, testLoc.getBlockZ());

            if (!block.getCollisionShape().getBoundingBoxes().isEmpty()) {
                double highestPoint = y + block.getCollisionShape().getBoundingBoxes().stream()
                        .mapToDouble(BoundingBox::getMaxY)
                        .max()
                        .orElse(1.0);

                if (highestPoint <= player.getEyeLocation().getY() + 5) {
                    targetY = highestPoint;
                    foundFloor = true;
                    break;
                }
            }
        }

        loc.setY(targetY);
        if (!foundFloor)
            return;

        new FlowerDisplay(main, loc);
    }


    @Override
    public void onDisable(TrailsPlus main) {
        displays.forEach(BlockDisplay::remove);
    }

    @Override
    public void onTick(TrailsPlus main, Player player) {
        displayTrail(main, player);
    }

    private boolean isPassableOrAir(Material material) {
        if (material.isAir()) return true;
        if (material == Material.LIGHT) return true;
        if (material.name().endsWith("_CARPET")) return true;
        return material.isBlock() && !material.isSolid();
    }


    public class FlowerDisplay {

        final TrailsPlus main;
        final BlockDisplay display;
        final Location startingLocation;

        public FlowerDisplay(TrailsPlus main, Location location) {
            this.main = main;
            startingLocation = location;
            display = location.getWorld().spawn(location, BlockDisplay.class, blockDisplay -> {
                blockDisplay.setBlock(Bukkit.createBlockData(flowerMaterials.get(ThreadLocalRandom.current().nextInt(flowerMaterials.size()))));
                float scale = ThreadLocalRandom.current().nextFloat(.3f, .8f);
                blockDisplay.setTransformation(new Transformation(new Vector3f(-.5f, -.5f, -.5f), new AxisAngle4f(), new Vector3f(scale, scale, scale), new AxisAngle4f()));
                blockDisplay.setTeleportDuration(5);
                blockDisplay.setBrightness(new Display.Brightness(location.clone().add(0, .1, 0).getBlock().getLightFromBlocks(), location.clone().add(0, .1, 0).getBlock().getLightFromSky()));
            });

            displays.add(display);

            new BukkitRunnable() {
                final double maxIncrease = .55, speed = .1;
                double increase = 0;
                public void run() {
                    if (increase < maxIncrease)
                        display.teleport(startingLocation.clone().add(0, Math.min(increase, maxIncrease), 0));
                    else if (increase > maxIncrease * 2) {
                        cancel();
                        displays.remove(display);
                        display.remove();
                    }
                    increase += speed;
                }
            }.runTaskTimer(main, 0, 5);

        }
    }

}

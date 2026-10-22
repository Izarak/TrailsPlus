package me.izarak.trailsPlus.commands;

import me.izarak.trailsPlus.TrailsPlus;
import me.izarak.trailsPlus.trails.projectile.ProjectileTrails;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectilesCommand implements TabExecutor {

    final TrailsPlus main;

    public ProjectilesCommand(TrailsPlus main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ProjectileTrails trail;
        switch (strings.length) {
            case 0:
                // TODO Open the trail gui.
                break;
            case 1:
                if (!(commandSender instanceof Player player)) {
                    commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
                    return true;
                }

                if (strings[0].equalsIgnoreCase("clear")) {
                    List<ProjectileTrails> playerTrails = main.getProjectileTrails(player).stream().toList();
                    for (int size = playerTrails.size(); size > 0; size--) {
                        main.removeProjectileTrail(player, playerTrails.get(size - 1));
                    }
                    return true;
                }

                try {
                    trail = ProjectileTrails.valueOf(strings[0]);
                } catch (IllegalArgumentException exception) {
                    player.sendMessage(ChatColor.RED + "Unknown trail: " + strings[0]);
                    break;
                }

                Collection<ProjectileTrails> playerTrails = main.getProjectileTrails(player);

                boolean hasConflictingTrails = false;
                ProjectileTrails conflictingTrail = null;

                for (ProjectileTrails trails : playerTrails) {
                    if (trail.getTrailAction().getIncompatableTrails().contains(trails)) {
                        hasConflictingTrails = true;
                        conflictingTrail = trails;
                        break;
                    }
                }

                if (hasConflictingTrails) {
                    player.sendMessage(ChatColor.RED + conflictingTrail.toString() + " is conflicting with " + trail);
                    return true;
                }

                int maxTrails = getMaxTrails(player);

                if (maxTrails != -1 && playerTrails.size() >= maxTrails && !playerTrails.contains(trail)) {
                    player.sendMessage(ChatColor.RED + "You have too many trails equipped!");
                    boolean color = true;
                    for (ProjectileTrails playerTrail : playerTrails) {
                        player.sendMessage((color ? ChatColor.GRAY : ChatColor.DARK_GRAY) + playerTrail.toString());
                        color = !color;
                    }
                    return true;
                }

                String message;

                if (main.setProjectileTrail(player, trail))
                    message = ChatColor.GREEN + "Trail has been activated!";
                else
                    message = ChatColor.RED + "Trail has been deactivated!";

                commandSender.sendMessage(message);
                break;
            case 2:
                try {
                    trail = ProjectileTrails.valueOf(strings[0]);
                } catch (IllegalArgumentException exception) {
                    commandSender.sendMessage(ChatColor.RED + "Unknown trail: " + strings[0]);
                    break;
                }
                Player player = Bukkit.getPlayer(strings[1]);
                if (player == null) {
                    commandSender.sendMessage(ChatColor.RED + "Couldn't find player: " + strings[1]);
                    break;
                }
                main.setProjectileTrail(player, trail);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();

        switch (strings.length) {
            case 1:
                list.add("clear");
                for (ProjectileTrails value : ProjectileTrails.values()) {
                    if (value.toString().toLowerCase().contains(strings[0]))
                        list.add(value.toString());
                }
                break;
            case 2:
                if (!(commandSender.hasPermission("trailsplus.trail.projectile.other")))
                    break;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().toLowerCase().contains(strings[1]))
                        list.add(onlinePlayer.getName());
                }
                break;
        }

        return list;
    }

    /**
     * @param player
     * @return Returns -1 for infinite amount.
     */
    private int getMaxTrails(Player player) {
        int highestValue = 0;
        if (player.isOp())
            return -1;
        for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions()) {
            String permissionString = effectivePermission.getPermission().toLowerCase();
            try {
                int value = Integer.parseInt(permissionString.substring("trailsplus.trail.projectile.".length()));
                if (value < 0)
                    return -1;
                if (value > highestValue)
                    highestValue = value;
            } catch (Exception ignored) {
            }
        }
        return highestValue;
    }
}
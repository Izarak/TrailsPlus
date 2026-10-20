package me.izarak.trailsPlus.commands;

import me.izarak.trailsPlus.PlayerTrails;
import me.izarak.trailsPlus.TrailsPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TrailsCommand implements TabExecutor {

    final TrailsPlus main;

    public TrailsCommand(TrailsPlus main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        switch (strings.length) {
            case 0:
                // TODO Open the trail gui.
                break;
            case 1:
                if (!(commandSender instanceof Player player)) {
                    commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
                    return true;
                }
                PlayerTrails trail;
                try {
                    trail = PlayerTrails.valueOf(strings[0]);
                } catch (IllegalArgumentException exception) {
                    player.sendMessage(ChatColor.RED + "Unknown trail: " + strings[0]);
                    break;
                }
                main.setPlayerTrail(player, trail);
                break;
            case 2:
                try {
                    trail = PlayerTrails.valueOf(strings[0]);
                } catch (IllegalArgumentException exception) {
                    commandSender.sendMessage(ChatColor.RED + "Unknown trail: " + strings[0]);
                    break;
                }
                Player player = Bukkit.getPlayer(strings[1]);
                if (player == null) {
                    commandSender.sendMessage(ChatColor.RED + "Couldn't find player: " + strings[1]);
                    break;
                }
                main.setPlayerTrail(player, trail);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();

        switch (strings.length) {
            case 1:
                for (PlayerTrails value : PlayerTrails.values()) {
                    if (value.toString().toLowerCase().contains(strings[0]))
                        list.add(value.toString());
                }
                break;
            case 2:
                if (!(commandSender.hasPermission("trailsplus.trail.player.other")))
                    break;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().toLowerCase().contains(strings[1]))
                        list.add(onlinePlayer.getName());
                }
                break;
        }

        return list;
    }
}

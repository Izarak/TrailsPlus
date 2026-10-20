package me.izarak.trailsPlus;

import me.izarak.trailsPlus.commands.TrailsCommand;
import me.izarak.trailsPlus.events.PlayerTrailEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class TrailsPlus extends JavaPlugin {

    final private File savedTrailsFile = new File(getDataFolder() + "/saved_trails.yml");
    private YamlConfiguration savedTrailsYaml;

    final private Map<UUID, PlayerTrails> selectedPlayerTrails = new HashMap<>();

    @Override
    public void onEnable() {
        if (!savedTrailsFile.exists()) {
            savedTrailsFile.getParentFile().mkdirs();
            try {
                savedTrailsFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        savedTrailsYaml = YamlConfiguration.loadConfiguration(savedTrailsFile);

        registerEvents();
        registerCommands();

        for (PlayerTrails value : PlayerTrails.values()) {
            value.getTrailAction().onEnable(this);
        }
    }

    @Override
    public void onDisable() {
        for (PlayerTrails value : PlayerTrails.values()) {
            value.getTrailAction().onDisable(this);
        }

        saveSelectedTrails();
    }

    public void setPlayerTrail(UUID id, PlayerTrails trail) {
        if (selectedPlayerTrails.containsKey(id))
            selectedPlayerTrails.get(id).getTrailAction().onDeactivate(this, Bukkit.getPlayer(id));
        if (trail == PlayerTrails.NONE || trail == null) {
            selectedPlayerTrails.put(id, PlayerTrails.NONE);
            return;
        }
        selectedPlayerTrails.put(id, trail);
        trail.getTrailAction().onActivate(this, Bukkit.getPlayer(id));
    }

    public void setPlayerTrail(Player player, PlayerTrails trail) {
        setPlayerTrail(player.getUniqueId(), trail);
    }

    @Nullable
    public PlayerTrails getPlayerTrail(UUID id) {
        PlayerTrails trail = selectedPlayerTrails.get(id);
        if (trail == null) {
            if (!savedTrailsYaml.isSet(id.toString() + ".Player Trail"))
                return null;
            try {
                trail = PlayerTrails.valueOf(savedTrailsYaml.getString(id.toString() + ".Player Trail"));
            } catch (IllegalArgumentException exception) {
                return null;
            }
            selectedPlayerTrails.put(id, trail);
        }


        return trail;
    }

    @Nullable
    public PlayerTrails getPlayerTrail(Player player) {
        return getPlayerTrail(player.getUniqueId());
    }

    @Nullable
    public PlayerTrails removePlayerTrail(UUID id) {
        return selectedPlayerTrails.remove(id);
    }

    @Nullable
    public PlayerTrails removePlayerTrail(Player player) {
        return removePlayerTrail(player.getUniqueId());
    }

    public File getSavedTrailsFile() {
        return savedTrailsFile;
    }

    public YamlConfiguration getSavedTrailsYaml() {
        return savedTrailsYaml;
    }

    public void saveSelectedTrails() {
        selectedPlayerTrails.forEach((k, v) -> savedTrailsYaml.set(k.toString() + ".Player Trail", v != null ? v.toString() : null));

        try {
            savedTrailsYaml.save(savedTrailsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerTrailEvents(this), this);
    }

    private void registerCommands() {
        registerCommand("trailsplus", new TrailsCommand(this));
    }

    private void registerCommand(String command, CommandExecutor executor) {
        PluginCommand pc = getCommand(command);
        if (pc == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't find command \"" + command + "\" in plugin.yml");
            return;
        }
        pc.setExecutor(executor);
        if (executor instanceof TabCompleter tab)
            pc.setTabCompleter(tab);
    }

}

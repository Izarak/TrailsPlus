package me.izarak.trailsPlus;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import me.izarak.trailsPlus.commands.ProjectilesCommand;
import me.izarak.trailsPlus.commands.TrailsCommand;
import me.izarak.trailsPlus.events.PlayerTrailEvents;
import me.izarak.trailsPlus.events.ProjectileTrailEvents;
import me.izarak.trailsPlus.events.UniversalEvents;
import me.izarak.trailsPlus.trails.player.PlayerTrails;
import me.izarak.trailsPlus.trails.projectile.ProjectileTrails;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class TrailsPlus extends JavaPlugin {

    final private File savedTrailsFile = new File(getDataFolder() + "/saved_trails.yml");
    private YamlConfiguration savedTrailsYaml;

    final private Multimap<UUID, PlayerTrails> selectedPlayerTrails = MultimapBuilder.hashKeys().arrayListValues().build();
    final private Multimap<UUID, ProjectileTrails> selectedProjectileTrails = MultimapBuilder.hashKeys().arrayListValues().build();

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
        for (UUID uuid : selectedPlayerTrails.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            selectedPlayerTrails.get(uuid).forEach(trail -> trail.getTrailAction().onDeactivate(this, player));
        }
        for (UUID uuid : selectedProjectileTrails.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            selectedProjectileTrails.get(uuid).forEach(trail -> trail.getTrailAction().onDeactivate(this, player));
        }
        for (PlayerTrails value : PlayerTrails.values()) {
            value.getTrailAction().onDisable(this);
        }

        saveSelectedTrails();
    }

    public boolean setProjectileTrail(UUID id, ProjectileTrails trail) {
        if (!selectedProjectileTrails.containsKey(id)) {
            selectedProjectileTrails.put(id, trail);
            return true;
        }

        Collection<ProjectileTrails> trails = selectedProjectileTrails.get(id);

        if (trails.contains(trail)) {
            removeProjectileTrail(id, trail);
            return false;
        } else {
            selectedProjectileTrails.put(id, trail);
            trail.getTrailAction().onActivate(this, Bukkit.getPlayer(id));
            return true;
        }
    }

    public boolean setProjectileTrail(Player player, ProjectileTrails trail) {
        return setProjectileTrail(player.getUniqueId(), trail);
    }

    public void removeProjectileTrail(UUID id, ProjectileTrails trail) {
        selectedProjectileTrails.remove(id, trail);
        trail.getTrailAction().onDeactivate(this, Bukkit.getPlayer(id));
    }

    public void removeProjectileTrail(Player player, ProjectileTrails trail) {
        removeProjectileTrail(player.getUniqueId(), trail);
    }

    public Collection<ProjectileTrails> getProjectileTrails(UUID id) {
        return selectedProjectileTrails.get(id);
    }

    public Collection<ProjectileTrails> getProjectileTrails(Player player) {
        return getProjectileTrails(player.getUniqueId());
    }

    /**
     * @param id
     * @param trail
     * @return True if the trail is added. False if trail is removed.
     */
    public boolean setPlayerTrail(UUID id, PlayerTrails trail) {
        if (!selectedPlayerTrails.containsKey(id)) {
            selectedPlayerTrails.put(id, trail);
            trail.getTrailAction().onActivate(this, Bukkit.getPlayer(id));
            return true;
        }

        Collection<PlayerTrails> playerTrailsList = selectedPlayerTrails.get(id);

        if (playerTrailsList.contains(trail)) {
            removePlayerTrail(id, trail);
            return false;
        } else {
            selectedPlayerTrails.put(id, trail);
            trail.getTrailAction().onActivate(this, Bukkit.getPlayer(id));
            return true;
        }
    }

    public boolean setPlayerTrail(Player player, PlayerTrails trail) {
        return setPlayerTrail(player.getUniqueId(), trail);
    }

    public Collection<PlayerTrails> getPlayerTrails(UUID id) {
        return selectedPlayerTrails.get(id);
    }

    public Collection<PlayerTrails> getPlayerTrails(Player player) {
        return getPlayerTrails(player.getUniqueId());
    }

    public void removePlayerTrail(UUID id, PlayerTrails trail) {
        selectedPlayerTrails.remove(id, trail);
        trail.getTrailAction().onDeactivate(this, Bukkit.getPlayer(id));
    }

    public void removePlayerTrail(Player player, PlayerTrails trail) {
        removePlayerTrail(player.getUniqueId(), trail);
    }

    public File getSavedTrailsFile() {
        return savedTrailsFile;
    }

    public YamlConfiguration getSavedTrailsYaml() {
        return savedTrailsYaml;
    }


    public void loadPlayerData(Player player) {
        String id = player.getUniqueId().toString();
        if (savedTrailsYaml.isSet(id + ".Player Trails")) {
            for (String playerTrail : savedTrailsYaml.getStringList(id + ".Player Trails")) {
                PlayerTrails trail = PlayerTrails.valueOf(playerTrail);
                if (!selectedPlayerTrails.containsEntry(player.getUniqueId(), trail))
                    selectedPlayerTrails.put(player.getUniqueId(), trail);
            }
        }
        if (savedTrailsYaml.isSet(id + ".Projectile Trails")) {
            for (String trail : savedTrailsYaml.getStringList(id + ".Projectile Trails")) {
                ProjectileTrails projectileTrail = ProjectileTrails.valueOf(trail);
                if (!selectedProjectileTrails.containsEntry(player.getUniqueId(), projectileTrail))
                    selectedProjectileTrails.put(player.getUniqueId(), projectileTrail);
            }

        }
    }

    private void saveSelectedTrails() {
        for (UUID id : selectedPlayerTrails.keySet()) {
            List<String> list = new ArrayList<>();
            for (PlayerTrails trail : selectedPlayerTrails.get(id)) {
                list.add(trail.toString());
            }
            savedTrailsYaml.set(id.toString() + ".Player Trails", list);
        }

        for (UUID uuid : selectedProjectileTrails.keySet()) {
            List<String> list = new ArrayList<>();
            for (ProjectileTrails trail : selectedProjectileTrails.get(uuid)) {
                list.add(trail.toString());
            }
            savedTrailsYaml.set(uuid.toString() + ".Projectile Trails", list);
        }

        try {
            savedTrailsYaml.save(savedTrailsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new UniversalEvents(this), this);
        pm.registerEvents(new PlayerTrailEvents(this), this);
        pm.registerEvents(new ProjectileTrailEvents(this), this);
    }

    private void registerCommands() {
        registerCommand("trailsplus", new TrailsCommand(this));
        registerCommand("projectiletrails", new ProjectilesCommand(this));
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

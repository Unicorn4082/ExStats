package com.xboxbedrock;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import java.io.File;
import java.io.IOException;

import net.coreprotect.database.Lookup.*;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;


public final class exstats extends JavaPlugin {
    exstats plugin = new exstats();
    private File customConfigFile;
    private FileConfiguration customConfig;
    @Override
    public void onEnable() {
        getLogger().info("ExStats Logging Stats now");
        createCustomConfig();
        this.getCommand("blocksplaced").setExecutor(new blocksplaced());



    }

    @Override
    public void onDisable() {
        getLogger().info("ExStats Saving Stats now");
    }
    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

// Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

// Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect)plugin).getAPI();
        if (CoreProtect.isEnabled()==false){
            return null;
        }

// Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 4){
            return null;
        }

        return CoreProtect;
    }


    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }
    public void updateStats(int time){
        CoreProtectAPI coreapi = getCoreProtect();

        Set<OfflinePlayer> whitelist = Bukkit.getWhitelistedPlayers();
        for (OfflinePlayer player : whitelist){
            String IGN = player.getName();
            if (plugin.getCustomConfig().contains(IGN+".placed") && plugin.getCustomConfig().contains(IGN+".breaked")){
                List<String[]> lookup = coreapi.performLookup(time, Arrays.asList(IGN), null, null, null, Arrays.asList(0, 1), 0, null);
                for (String[] lookstr : lookup) {
                    CoreProtectAPI.ParseResult lookupOBJ = coreapi.parseResult(lookstr);
                    Date now = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    if (lookupOBJ.getActionId() == 0) {


                        ArrayList<String> dataArray = (ArrayList<String>) plugin.getCustomConfig().getStringList(IGN + ".breaked");
                        dataArray.add(format.format(now));


                    } else {
                        //placed code
                    }
                }


            }else{
                plugin.getCustomConfig().createSection(IGN+".placed");
                plugin.getCustomConfig().createSection(IGN+".breaked");
                this.reloadConfig();
            }
        }

    }

    public void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "data.yml");
        if (!this.customConfigFile.exists()) {
            this.customConfigFile.getParentFile().mkdirs();
            saveResource("data.yml", false);


        }
        customConfig= new YamlConfiguration();
        try {
            this.customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }




    }
    public void saveConfig() {
        try {
            this.customConfig.save(this.customConfigFile);
            Bukkit.getServer().getConsoleSender().sendMessage("ExStats data has been saved");
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("ExStats data could not be saved");
        }
    }
    public void reloadConfig() {
        this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);
    }
}

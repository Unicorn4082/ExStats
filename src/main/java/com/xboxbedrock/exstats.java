package com.xboxbedrock;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.coreprotect.database.Lookup.*;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.scheduler.BukkitScheduler;



public final class Exstats extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;
    private static Exstats instance;
    // main config file


    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        
        getLogger().info("ExStats Logging Stats now");
        createCustomConfig();
        Blocksplaced l = new Blocksplaced(this); //We're passing the instance of our 'MainPluginClass' to the object, look at the constructor in SomePluginListener to see what we do with it
        this.getCommand("blocksplaced").setExecutor(new Blocksplaced(this));
        this.getCommand("blocksbreaked").setExecutor(new Blocksbroken(this));
        this.getCommand("blocksactivity").setExecutor(new Blocksbrokenandplaced(this));

        this.saveDefaultConfig();
        this.reloadConfig();

            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    updateStats(60);


                }

            }, 1L, 1200L);


        }


        // Schedule event





    @Override
    public void onDisable() {
        getLogger().info("ExStats Saving Stats now");
        Bukkit.getScheduler().cancelTasks(this);

    }
    //public CreatePaste createPaste() {
    //    return new CreatePaste(this);
    //}

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
    protected Poster getNewPoster() {
        return new Poster(this);
    }
    public void postpastenow() throws IOException {
        Pastebin.PasteRequest request = new Pastebin.PasteRequest("O-rNCEpqum8MF7ZCbeNa84UelpKbbbPP", "kkkkkkkkkkkkkkk");
        request.setPasteName("Paste Java Wrap (Ligh AF)");//To set title
        request.setPasteFormat("java");//To make it a java format
        //To make unlisted
        request.setPasteExpire("1H");//Make it live 1 hour
        request.postPaste();//Prints the paste url
    }
    protected Paste[] parse(String[] args) throws PastebinException {
        ArrayList<Paste> pastes = new ArrayList();
        ArrayList<String> current = new ArrayList();
        String[] var7 = args;
        int var6 = args.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            String str = var7[var5];
            if (str != null && !str.equals("<paste>")) {
                if (str.equals("</paste>")) {
                    pastes.add(new Paste(this, current));
                    current = new ArrayList();
                } else if (str.startsWith("<paste_")) {
                    current.add(str);
                }
            }
        }

        return (Paste[])pastes.toArray(new Paste[pastes.size()]);
    }


    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }
    public void updateStats(int time){
        CoreProtectAPI coreapi = getCoreProtect();

        Set<OfflinePlayer> whitelist = Bukkit.getWhitelistedPlayers();
        for (OfflinePlayer player : whitelist){
            String IGN = player.getName();
            if (this.getCustomConfig().contains(IGN+".placed") && this.getCustomConfig().contains(IGN+".breaked")){
                List<String[]> lookup = coreapi.performLookup(time, Arrays.asList(IGN), null, null, null, Arrays.asList(0, 1), 0, null);
                for (String[] lookstr : lookup) {
                    CoreProtectAPI.ParseResult lookupOBJ = coreapi.parseResult(lookstr);
                    Date now = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    if (lookupOBJ.getActionId() == 0) {


                        ArrayList<String> dataArray = (ArrayList<String>) this.getCustomConfig().getStringList(IGN + ".breaked");
                        dataArray.add(format.format(now));
                        this.getCustomConfig().set(IGN + ".breaked", dataArray);
                        this.saveConfig();
                        this.reloadConfig();


                    } else {
                        ArrayList<String> dataArray = (ArrayList<String>) this.getCustomConfig().getStringList(IGN + ".placed");
                        dataArray.add(format.format(now));
                        this.getCustomConfig().set(IGN + ".placed", dataArray);
                        this.saveConfig();
                        this.reloadConfig();

                    }
                }


            }else{
                this.getCustomConfig().createSection(IGN+".placed");
                this.getCustomConfig().createSection(IGN+".breaked");
                this.saveConfig();
                this.reloadConfig();

                List<String[]> lookup = coreapi.performLookup(time, Arrays.asList(IGN), null, null, null, Arrays.asList(0, 1), 0, null);
                for (String[] lookstr : lookup) {
                    CoreProtectAPI.ParseResult lookupOBJ = coreapi.parseResult(lookstr);
                    Date now = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    if (lookupOBJ.getActionId() == 0) {


                        ArrayList<String> dataArray = (ArrayList<String>) this.getCustomConfig().getStringList(IGN + ".breaked");
                        dataArray.add(format.format(now));
                        this.getCustomConfig().set(IGN + ".breaked", dataArray);
                        this.saveConfig();


                    } else {
                        ArrayList<String> dataArray = (ArrayList<String>) this.getCustomConfig().getStringList(IGN + ".placed");
                        dataArray.add(format.format(now));
                        this.getCustomConfig().set(IGN + ".placed", dataArray);
                        this.saveConfig();
                        this.reloadConfig();
                    }
                }

            }
        }

    }

    public void createCustomConfig(){
        customConfigFile = new File(getDataFolder(), "data.yml");
        if (!this.customConfigFile.exists()) {
            this.customConfigFile.getParentFile().mkdirs();
            saveResource("data.yml", false);


        }
        customConfig= new YamlConfiguration();
        try {
            this.customConfig.load(customConfigFile);
            customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
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



        // Essential Hook

    // Essential Hooks


















    /**
     * Only DAY, WEEK, MONTH, DISPOSABLE and SESSION type will be returned here
     * LONGTIMENOSEE is checked in playerLoginEvent
     *
     * @param id uuid of the player
     * @return set of rules, not null
     */







}

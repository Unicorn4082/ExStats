package com.xboxbedrock;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class Blocksbrokenandplaced implements CommandExecutor {
    private final Exstats plugin; //This is a reference to the main class

    public Blocksbrokenandplaced(Exstats instanceOfMainClass) { //This is called a constructor; basic java. Won't explain more than the name.
        this.plugin = instanceOfMainClass; //Here, we set the reference to the main class, if this line doesn't exist, the 'plugin' instance is null, which will cause a NullPointerException since we call a method on a null object.
    }
    // function to sort hashmap by values
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Set<OfflinePlayer> whitelist = Bukkit.getWhitelistedPlayers();

        HashMap<String, Integer> MappedValues = new HashMap<String, Integer>();

        ArrayList<String> editline = new ArrayList<String>();

        for (OfflinePlayer player: whitelist) {
            ArrayList<String> dataArray = (ArrayList<String>) plugin.getCustomConfig().getStringList(player.getName() + ".breaked");
            ArrayList<String> dataArray2 = (ArrayList<String>) plugin.getCustomConfig().getStringList(player.getName() + ".placed");
            int placed = dataArray.size();
            int broken = dataArray2.size();
            MappedValues.put(player.getName(), placed+broken);


        }
        Map<String, Integer> MapSorted = sortByValue(MappedValues);
        int loopvar = 1;
        for (Map.Entry<String, Integer> en : MapSorted.entrySet()) {
            editline.add(loopvar + " " + en.getKey() + ": " + en.getValue());
            loopvar ++;


        }
        String[] stringArray = editline.stream().toArray(String[]::new);
        sender.sendMessage(stringArray);


        //try {
        //Pastebin.PasteRequest request = new Pastebin.PasteRequest("O-rNCEpqum8MF7ZCbeNa84UelpKbbbPP", "kkkkkkkkkkkkkkk");
        //request.setPasteName("Paste Java Wrap (Ligh AF)");//To set title
        //request.setPasteFormat("java");//To make it a java format
        //To make unlisted
        //request.setPasteExpire("1H");//Make it live 1 hour
        //request.postPaste();//Prints the paste url

        //sender.sendMessage(request.postPaste().toString());
        //} catch (IOException e) {
        // e.printStackTrace();
        //}

        return true;
    }
}

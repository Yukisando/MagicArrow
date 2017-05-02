package me.yukisando.MagicArrow;

import java.util.ArrayList;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicArrowMain extends JavaPlugin {

    protected ArrayList<String>	enabledPlayers = new ArrayList<>();
    protected MagicArrowLogger log;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

        this.log = new MagicArrowLogger();

        this.getCommand("MA").setExecutor(new MagicArrowListener(this));

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new MagicArrowListener(this), this);
    }
}
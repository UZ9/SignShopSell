package com.yerti.signshopsell;

import com.yerti.signshopsell.config.Config;
import com.yerti.signshopsell.config.Messages;
import com.yerti.signshopsell.events.SignPlacement;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SignShopSell extends JavaPlugin {

    private static Economy econ = null;

    public void onEnable() {

        new SignPlacement(this);
        new Config(this);

        Messages messages = new Messages(this);
        messages.init();

        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void onDisable() {


    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }


}

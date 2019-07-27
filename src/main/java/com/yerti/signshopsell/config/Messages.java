package com.yerti.signshopsell.config;


import org.bukkit.plugin.Plugin;

public class Messages {
    private Plugin pl;

    public Messages(Plugin pl) {
        this.pl = pl;
    }

    public static Config config;

    public void init() {

        config = new Config(pl);

        config.setPrefix("&f[&aSignShopSell&f]");

        pl.saveConfig();


    }
}
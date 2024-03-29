package com.yerti.signshopsell.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Config {

    FileConfiguration config;
    File file;


    public Config(Plugin pl) {
        pl.saveDefaultConfig();
        this.config = pl.getConfig();
    }

    public void setPrefix(String prefix) {
        config.set("prefix", prefix);
    }

    public String prefixMessage(String message) {
        return getTranslation("prefix") + " " + message;
    }

    public Object getValue(String key) {
        return config.get(key);
    }

    public String getTranslation(String key) {
        return ChatColor.translateAlternateColorCodes('&', (String) getValue(key));
    }

    public void setDefault(String key, Object value) {

        if (value instanceof String) {
            config.set(key, value.toString());
        }

        if (value instanceof Long) {
            config.set(key, Long.valueOf(value.toString()));
        }



    }




}

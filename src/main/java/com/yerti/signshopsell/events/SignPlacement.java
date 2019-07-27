package com.yerti.signshopsell.events;

import com.yerti.signshopsell.SignShopSell;
import com.yerti.signshopsell.config.Messages;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;
import org.bukkit.plugin.Plugin;


public class SignPlacement implements Listener {

    Plugin pl;

    public SignPlacement(Plugin pl) {
        this.pl = pl;
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent e) {


        if (e.getPlayer().hasPermission("signshopsell.placesign")) {

            if (e.getLine(0).equals("[SignShopSell]")) {


                Sign sign = (Sign) e.getBlock().getState().getData();
                Block attached = e.getBlock().getRelative(sign.getAttachedFace());

                if (attached.getType().equals(Material.CHEST)) {

                    e.setLine(0, ChatColor.GREEN + "[SignShopSell]");
                    e.getPlayer().sendMessage(Messages.config.prefixMessage(ChatColor.GREEN + "Successfully created a sign shop!"));

                } else {
                    e.getPlayer().sendMessage(Messages.config.prefixMessage(ChatColor.RED + "You must place this next to a chest!"));
                    e.setLine(0, ChatColor.RED + "No chest.");

                }


            }

        } else {
            e.getPlayer().sendMessage(Messages.config.prefixMessage(ChatColor.RED + "You do not have permission to create this sign!"));
            e.setLine(0, ChatColor.RED + "No perms.");
        }


    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null) return;

        if (e.getPlayer().hasPermission("signshopsell.usesign")) {

            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                Block block = e.getClickedBlock();

                if (block.getType().equals(Material.SIGN) || block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {

                    org.bukkit.material.Sign sign = (Sign) block.getState().getData();
                    org.bukkit.block.Sign signBlock = (org.bukkit.block.Sign) block.getState();
                    if (signBlock.getLine(0).contains("[SignShopSell]")) {

                        Block attached = block.getRelative(sign.getAttachedFace());
                        if (attached.getType().equals(Material.CHEST)) {

                            InventoryHolder ih = ((Chest) attached.getState()).getInventory().getHolder();

                            if (ih instanceof DoubleChest) {
                                DoubleChest dc = (DoubleChest) ih;
                                sellItems(dc.getInventory(), e.getPlayer());
                            } else {
                                sellItems(ih.getInventory(), e.getPlayer());
                            }


                        }
                    }

                }
            }
        }


    }

    public void sellItems(Inventory i, Player pl) {

        double moneyEarned = 0;
        int itemAmount = 0;

        if (i.getContents() == null) {
            return;
        }


        for (ItemStack e : i.getContents()) {
            if (e != null) {
                if (ShopGuiPlusApi.getItemStackPriceSell(pl, e) != -1) {
                    itemAmount++;
                    moneyEarned += ShopGuiPlusApi.getItemStackPriceSell(pl, e);
                    i.remove(e);
                }


            }

        }

        if (itemAmount == 0) {
            pl.sendMessage(Messages.config.prefixMessage(ChatColor.RED + "No items able to be sold were found."));
            return;
        }

        SignShopSell.getEconomy().depositPlayer(pl, moneyEarned);
        pl.sendMessage(Messages.config.prefixMessage(ChatColor.WHITE + "Sold " + itemAmount + "items in chest for " + ChatColor.GREEN + "$" + moneyEarned + ChatColor.WHITE + '.'));


    }



}

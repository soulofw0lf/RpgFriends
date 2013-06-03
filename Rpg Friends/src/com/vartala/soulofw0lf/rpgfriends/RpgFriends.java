package com.vartala.soulofw0lf.rpgfriends;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RpgFriends extends JavaPlugin implements Listener {
	RpgFriends plugin;

	@Override
	public void onEnable(){
		plugin = this;
		getCommand("friend").setExecutor(new friendHandler(this));
		Bukkit.getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		getLogger().info("Rpg Friends has been enabled!");
	}

	@Override
	public void onDisable(){
		getLogger().info("Rpg Friends has been Disabled!");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerClick(InventoryClickEvent event){
		Player player = (Player)event.getWhoClicked();
		if (event.getInventory().getTitle().equalsIgnoreCase("Friends List")){
			event.setResult(Result.DENY);
			if (event.getCurrentItem() != null){
			ItemStack item = event.getCurrentItem();
			ItemMeta im = item.getItemMeta();
			String mname = im.getDisplayName();
			SkullMeta sm = (SkullMeta)item.getItemMeta();
			String oname = sm.getOwner();
			if (mname != null){
				if (event.isShiftClick()){
					player.performCommand(getConfig().getString("Commands.Shift Click.Command").replaceAll("@t", mname).replaceAll("@p", player.getName()));
					event.setCancelled(true);
					player.closeInventory();
					return;
				}
			if (event.isLeftClick()){
				player.performCommand(getConfig().getString("Commands.Left Click.Command").replaceAll("@t", mname).replaceAll("@p", player.getName()));
				event.setCancelled(true);
				player.closeInventory();
				return;
			}
			if (event.isRightClick()){
				player.performCommand(getConfig().getString("Commands.Right Click.Command").replaceAll("@t", mname).replaceAll("@p", player.getName()));
				event.setCancelled(true);
				player.closeInventory();
				return;
			}
		
			}
			if (event.isShiftClick()){
				player.performCommand(getConfig().getString("Commands.Shift Click.Command").replaceAll("@t", oname).replaceAll("@p", player.getName()));
				event.setCancelled(true);
				player.closeInventory();
				return;
			}
			if (event.isLeftClick()){
				player.performCommand(getConfig().getString("Commands.Left Click.Command").replaceAll("@t", oname).replaceAll("@p", player.getName()));
				event.setCancelled(true);
				player.closeInventory();
				return;
			}
			if (event.isRightClick()){
				player.performCommand(getConfig().getString("Commands.Right Click.Command").replaceAll("@t", oname).replaceAll("@p", player.getName()));
				event.setCancelled(true);
				player.closeInventory();
				return;
			}
			
			}
		}
	}
	public void friendAccept(Player player, Player p){
		final Player p1 = player;
		final Player p2 = p;
		final Integer timer = getConfig().getInt("Timer");

		new BukkitRunnable(){


			Integer count = timer;
			@Override
			public void run(){
				if (count == 0){
					p1.sendMessage(p2.getName() + " has declined your friend invite");
					getConfig().set("Invited." + p2.getName(), null);
					saveConfig();
					cancel();
					return;
				}
				if (getConfig().getConfigurationSection(p1.getName() + ".Friends") != null){
					if (getConfig().getConfigurationSection(p1.getName() + ".Friends").contains(p2.getName())){
						p1.sendMessage(p2.getName() + " has been added to your friends list!");
						p2.sendMessage(p1.getName() + " has been added to your friends list!");
						getConfig().set("Invited." + p2.getName(), null);
						saveConfig();
						cancel();
						return;
					}
				}
				if (!(getConfig().getConfigurationSection("Invited").contains(p2.getName()))){
					p1.sendMessage(p2.getName() + " has declined your friend invite");
					cancel();
					return;
				}
				count--;

			}
		}.runTaskTimer(plugin, 20, 20);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);			
		if (getConfig().contains(player.getName())){
			for (String key : getConfig().getConfigurationSection(player.getName() + ".Friends").getKeys(false)){
				if (Bukkit.getPlayer(key) != null){
					Player p = Bukkit.getPlayer(key);
					p.sendMessage("§6" + player.getName() + "§3 has come online!");
				}
			}

		}
	}

}


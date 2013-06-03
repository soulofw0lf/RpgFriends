package com.vartala.soulofw0lf.rpgfriends;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class friendHandler implements CommandExecutor {
	RpgFriends Rpgf;
	public friendHandler(RpgFriends rpgf){
		this.Rpgf = rpgf;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = (Player)sender;
		if (!(sender instanceof Player)){
			return true;
		}
		if (args.length >= 3){
			player.sendMessage("Incorrect usage. Please use /friend <add/remove/list/accept/deny/ignore/unignore>");
			return true;
		}
		if (args.length == 0){
			player.sendMessage("Incorrect usage. Please use /friend <add/remove/list/accept/deny/ignore/unignore>");
			return true;
		}
		if (args[0].equalsIgnoreCase("remove")){
			if (args.length != 2){
				player.sendMessage("Incorrect usage. Please use /friend remove playername");
				return true;
			}
			Player p = Bukkit.getPlayer(args[1]);
			if (p == null){
				player.sendMessage("player could not be found!");
				return true;
			}
			if (!(this.Rpgf.getConfig().getConfigurationSection(player.getName() + ".Friends").contains(p.getName()))){
				player.sendMessage("This player is not on your friends list!");
				return true;
			}
			p.sendMessage(player.getName() + " has removed you from his friends list.");
			this.Rpgf.getConfig().set(player.getName() + ".Friends." + p.getName(), null);
			this.Rpgf.getConfig().set(p.getName() + ".Friends." + player.getName(), null);
			player.sendMessage("You are no longer friends with " + p.getName() + ".");
			this.Rpgf.saveConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("add")){
			if (args.length != 2){
				player.sendMessage("Incorrect usage. Please use /friend add playername");
				return true;
			}
			Player p = Bukkit.getPlayer(args[1]);
			if (p == null){
				player.sendMessage("player could not be found!");
				return true;
			}
			if (this.Rpgf.getConfig().getConfigurationSection(player.getName() + ".Friends") != null){
			if (this.Rpgf.getConfig().getConfigurationSection(player.getName() + ".Friends").contains(p.getName())){
				player.sendMessage("This player is already on your friends list!");
				return true;
			}
			}
			if (this.Rpgf.getConfig().getConfigurationSection(player.getName() + ".Ignore") != null){
			if (this.Rpgf.getConfig().getConfigurationSection(player.getName() + ".Ignore").contains(p.getName())){
				player.sendMessage("You cannot add a friend that you have ignored!");
				return true;
			}
			}
			if (this.Rpgf.getConfig().getConfigurationSection("Invited") != null){
			if (this.Rpgf.getConfig().getConfigurationSection("Invited").contains(p.getName())){
				player.sendMessage("this person already has a pending friend invite!");
				return true;
			}
			}
			player.sendMessage("Friend invite sent to " + args[1]);
			p.sendMessage(player.getName() + " has sent you a friend invite! \n Type §2/friend accept §For §4/friend deny");
			this.Rpgf.getConfig().set("Invited." + p.getName(), player.getName());
			this.Rpgf.saveConfig();
			this.Rpgf.friendAccept(player, p);
			return true;
		}
		if (args[0].equalsIgnoreCase("accept")){
			
			if (!(this.Rpgf.getConfig().getConfigurationSection("Invited").contains(player.getName()))){
				player.sendMessage("You do not have any pending friend invites!");
				return true;
			}
			String name = this.Rpgf.getConfig().getString("Invited." + player.getName());
			String name1 = player.getName();
			this.Rpgf.getConfig().set(name + ".Friends." + name1, true);
			this.Rpgf.getConfig().set(name1 + ".Friends." + name, true);
			this.Rpgf.getConfig().set("Invited." + name1, null);
			this.Rpgf.saveConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("deny")){
			if (!(this.Rpgf.getConfig().getConfigurationSection("Invited").contains(player.getName()))){
				player.sendMessage("You do not have any pending friend invites!");
				return true;
			}
			String name1 = player.getName();
			this.Rpgf.getConfig().set("Invited." + name1, null);
			this.Rpgf.saveConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("list")){
			if (this.Rpgf.getConfig().getConfigurationSection(player.getName()) == null){
				player.sendMessage("You do not have anyone on your friends list!");
				return true;
			}
			Integer islot = 0;
			Inventory flist = Bukkit.createInventory(null, 45, "Friends List");
			for (String key : this.Rpgf.getConfig().getConfigurationSection(player.getName() + ".Friends").getKeys(false)){
				if (Bukkit.getPlayer(key) != null){
					Player p = Bukkit.getPlayer(key);
					ItemStack skull = new ItemStack(397, 1, (short) 3);
					SkullMeta meta = (SkullMeta) skull.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					String world = p.getLocation().getWorld().getName();
					lore.add("§f[World]§2" + world);
					lore.add("§f[Left Click]§2" + this.Rpgf.getConfig().getString("Commands.Left Click.Description").replaceAll("@t", p.getName()).replaceAll("@p", player.getName()));
					lore.add("§f[Right Click]§2" + this.Rpgf.getConfig().getString("Commands.Right Click.Description").replaceAll("@t", p.getName()).replaceAll("@p", player.getName()));
					lore.add("§f[shift Click]§2" + this.Rpgf.getConfig().getString("Commands.Shift Click.Description").replaceAll("@t", p.getName()).replaceAll("@p", player.getName()));
					meta.setLore(lore);
					meta.setOwner(p.getName());
					skull.setItemMeta(meta);
					flist.setItem(islot, skull);
					islot++;
				} else {
					ItemStack skull = new ItemStack(397, 1, (short) 0);
					SkullMeta meta = (SkullMeta) skull.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					lore.add("§f[World]§4Offline");
					lore.add("§f[Left Click]§2" + this.Rpgf.getConfig().getString("Commands.Left Click.Description").replaceAll("@t", key).replaceAll("@p", player.getName()));
					lore.add("§f[Right Click]§2" + this.Rpgf.getConfig().getString("Commands.Right Click.Description").replaceAll("@t", key).replaceAll("@p", player.getName()));
					lore.add("§f[Shift Click]§2" + this.Rpgf.getConfig().getString("Commands.Shift Click.Description").replaceAll("@t", key).replaceAll("@p", player.getName()));
					meta.setLore(lore);
					meta.setDisplayName(key);
					skull.setItemMeta(meta);
					flist.setItem(islot, skull);
					islot++;
				}
			}
			player.openInventory(flist);
			return true;
		}
		return false;
	}

}

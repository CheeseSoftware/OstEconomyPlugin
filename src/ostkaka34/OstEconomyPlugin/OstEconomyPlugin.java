package ostkaka34.OstEconomyPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OstEconomyPlugin extends JavaPlugin implements IOstEconomy, Listener {

	protected Map<Player, EPlayer> ePlayers = new HashMap<Player, EPlayer>();
	
	protected Map<Material, Integer> shopItems = new HashMap<Material, Integer>();
	protected Map<Material, Integer> xpShopItems = new HashMap<Material, Integer>();
	
	protected Map<String, Material> shopItemNames = new HashMap<String, Material>();
	protected Map<String, Material> xpShopItemNames = new HashMap<String, Material>();
	
	protected void LoadPlayer(Player player) {
		if (ePlayers.containsKey(player))
			ePlayers.remove(player);
		
		ePlayers.put(player, new EPlayer(player));
	}
	
	@Override
	public void onEnable(){
		//getCommand("testcommand").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		
		Player[] players = getServer().getOnlinePlayers();
		
		for (int i = 0; i < players.length; i++) {
			LoadPlayer(players[i]);
		}
	}
	
	@Override
	public void onDisable(){
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		LoadPlayer(player);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if (ePlayers.containsKey(player))
			ePlayers.remove(player);
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		if (ePlayers.containsKey(event.getPlayer()))
		{
			EPlayer player = ePlayers.get(event.getPlayer());
			
			player.Reset();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (ePlayers.containsKey(sender))
		{
			EPlayer player = ePlayers.get(sender);
			
			if (cmd.getName().equalsIgnoreCase("buy")) {
				if (args.length >= 1) {
					if (!args[0].equalsIgnoreCase("help")) {
						int amount = 1;
						
						if (args.length >= 2)
							Integer.getInteger(args[1], amount);
						
						if (amount < 1)
							amount = 1;
						
						BuyShopItem(player.getPlayer(), args[0], amount);
						return true;
					}
				}
				
				String items = "";
				
				 Iterator<Entry<Material, Integer>> it = shopItems.entrySet().iterator();
				 Iterator<Entry<String, Material>> it2 = shopItemNames.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<Material, Integer> pairs = (Map.Entry<Material, Integer>)it.next();
		            items += "\n" + it2.next().getKey() + " - $" + pairs.getValue().toString();
		        }
				
				sender.sendMessage("Items: " + items);
				
				return true;
			}
			else if (cmd.getName().equalsIgnoreCase("xpbuy")) {
				if (args.length >= 1) {
					if (!args[0].equalsIgnoreCase("help")) {
						int amount = 1;
						
						if (args.length >= 2)
							Integer.getInteger(args[1], amount);
						
						if (amount < 1)
							amount = 1;
						
						BuyXPShopItem(player.getPlayer(), args[0], amount);
						return true;
					}
				}
				
				String items = "";
				
		        Iterator<Entry<Material, Integer>> it = xpShopItems.entrySet().iterator();
		        Iterator<Entry<String, Material>> it2 = xpShopItemNames.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<Material, Integer> pairs = (Map.Entry<Material, Integer>)it.next();
		            items += "\n" + it2.next().getKey() + " - $" + pairs.getValue().toString();
		        }
				
				sender.sendMessage("Items: " + items);
				
				return true;
			}
		}
		
		return false;
	}

	@Override
	public long getMoney(Player player) {
		if (ePlayers.containsKey(player))
			return ePlayers.get(player).getMoney();
		return 0;
	}

	@Override
	public long getXp(Player player) {
		if (ePlayers.containsKey(player))
			return ePlayers.get(player).getXp();
		return 0;
	}

	@Override
	public void GiveMoney(Player player, long money) {
		if (ePlayers.containsKey(player))
			ePlayers.get(player).GiveMoney(money);
	}

	@Override
	public void ResetStats(Player player) {
		if (ePlayers.containsKey(player))
			ePlayers.get(player).Reset();
	}

	@Override
	public boolean BuyShopItem(Player player, Material material, int amount) {
		if (ePlayers.containsKey(player)) {
			EPlayer eplayer = ePlayers.get(player);
			
			if (shopItems.containsKey(material)) {
				int i = 0;
				
		        if (material != null) {
					for (i = 0; i < amount; i++) {
						if (!eplayer.Buy((long)(int)shopItems.get(material), material))
							break;
					}
		        }
		        else {
		        	player.sendMessage("There is no such buyable item. Say '/buy help' to list items.");
		        	return false;
		        }
		        
		        player.sendMessage(i + "/" + amount + " " + material.toString() +
		        		" bought! You have $" + eplayer.getMoney() + " left.");
		        
		        return (i > 0);
			}
		}
		return false;
	}

	@Override
	public boolean BuyXPShopItem(Player player, Material material, int amount) {
		if (ePlayers.containsKey(player)) {
			EPlayer eplayer = ePlayers.get(player);
			
			if (xpShopItems.containsKey(material)) {
				int i = 0;
				
		        if (material != null) {
					for (i = 0; i < amount; i++) {
						if (!eplayer.Buy((long)(int)xpShopItems.get(material), material))
							break;
					}
		        }
		        else {
		        	player.sendMessage("There is no such buyable item. Say '/xpbuy help' to list items.");
		        	return false;
		        }
		        
		        player.sendMessage(i + "/" + amount + " " + material.toString() +
		        		" bought! You have $" + eplayer.getXp() + " left.");
		        
		        return (i > 0);
			}
		}
		return false;
	}
	
	public boolean BuyShopItem(Player player, String material, int amount) {
		Material m;
		
		if (shopItemNames.containsKey(material))
			m = shopItemNames.get(material);
		else
			m = Material.AIR;
		
		return BuyShopItem(player, m, amount);
	}
	
	public boolean BuyXPShopItem(Player player, String material, int amount) {
		Material m;
		
		if (xpShopItemNames.containsKey(material))
			m = xpShopItemNames.get(material);
		else
			m = Material.AIR;
		
		return BuyXPShopItem(player, m, amount);
	}

	@Override
	public void RegisterShopItem(Material material, long price, String name, boolean maxOne) {
		shopItems.put(material, (Integer)(int)price);
		shopItemNames.put(name, material);
	}

	@Override
	public void RegisterXPShopItem(Material material, long price, String name, boolean maxOne) {
		xpShopItems.put(material, (Integer)(int)price);
		xpShopItemNames.put(name, material);
	}
}

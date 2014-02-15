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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OstEconomyPlugin extends JavaPlugin implements Listener, IOstEconomy {

	protected Map<Player, EPlayer> ePlayers = new HashMap<Player, EPlayer>();
	
	protected Map<Material, Integer> shopItems = new HashMap<Material, Integer>();
	protected Map<Material, Integer> xpShopItems = new HashMap<Material, Integer>();
	
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (ePlayers.containsKey(sender))
		{
			EPlayer player = ePlayers.get(sender);
			
			if (label.equalsIgnoreCase("buy")) {
				if (args.length >= 1) {
					if (!args[0].equalsIgnoreCase("help")) {
						int amount = 1;
						int amountBought = 0;
						Material material = null;
						int price = 0;
						
						if (args.length >= 2)
							Integer.getInteger(args[1], amount);
						
						Iterator<Entry<Material, Integer>> it = shopItems.entrySet().iterator();
				        while (it.hasNext()) {
				            Map.Entry<Material, Integer> pairs = (Map.Entry<Material, Integer>)it.next();
				            if (pairs.getKey().toString() == args[0]) {
				            	material = pairs.getKey();
				            	price = pairs.getValue();
				            }
				        }
						
				        int i;
				        
				        if (material != null) {
							for (i = 0; i < amount; i++) {
								if (!player.Buy(price, material))
									break;
							}
				        }
				        else {
				        	sender.sendMessage("There is no such buyable item. Say '/buy help' to list items.");
				        	return true;
				        }
				        
				        sender.sendMessage(i + "/" + amount + " " + material.toString() +
				        		" bought! You have $" + player.getMoney() + " left.");
					}
					
					return true;
				}
				
				String items = "";
				
		        Iterator<Entry<Material, Integer>> it = shopItems.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<Material, Integer> pairs = (Map.Entry<Material, Integer>)it.next();
		            items += pairs.getKey().toString() + " - \t$" + pairs.getValue().toString() + "\n";
		        }
				
				sender.sendMessage("Items: \n" + items);
				
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
	public void BuyShopItem(Player player, Material material) {
		if (ePlayers.containsKey(player)) {
			EPlayer eplayer = ePlayers.get(player);
			
			if (shopItems.containsKey(material)) {
				eplayer.Buy(shopItems.get(material), material);
			}
		}
	}

	@Override
	public void BuyXPShopItem(Player player, Material material) {
		if (ePlayers.containsKey(player)) {
			EPlayer eplayer = ePlayers.get(player);
			
			if (xpShopItems.containsKey(material)) {
				eplayer.XpBuy(xpShopItems.get(material), material);
			}
		}
	}

	@Override
	public void RegisterShopItem(Material material, long price) {
		shopItems.put(material, (Integer)(int)price);
	}

	@Override
	public void RegisterXPShopItem(Material material, long price) {
		xpShopItems.put(material, (Integer)(int)price);
	}
}

package ostkaka34.OstEconomyPlugin;

import java.io.File;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OstEconomyPlugin extends JavaPlugin implements IOstEconomy, Listener {

	protected Map<Player, EPlayer> ePlayers = new HashMap<Player, EPlayer>();
	
	protected Map<Material, Integer> shopItems = new HashMap<Material, Integer>();
	protected Map<Material, Integer> xpShopItems = new HashMap<Material, Integer>();
	
	protected Map<String, Material> shopItemNames = new HashMap<String, Material>();
	protected Map<String, Material> xpShopItemNames = new HashMap<String, Material>();
	
	protected Map<Material, String> shopItemNames2 = new HashMap<Material, String>();
	protected Map<Material, String> xpShopItemNames2 = new HashMap<Material, String>();
	
	protected void LoadPlayer(Player player) {
		if (ePlayers.containsKey(player)) {
			ePlayers.get(player).Destroy(this);
			ePlayers.remove(player);
		}
		
		ePlayers.put(player, new EPlayer(player, this));
	}
	@Override
	public void onEnable(){
		//getCommand("testcommand").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		
		//saveDefaultConfig();
		
		Player[] players = getServer().getOnlinePlayers();
		
		File f = new File(this.getDataFolder() + File.separator + "playerdata" + File.separator);
		if(!f.exists())
		    f.mkdir();
		
		for (int i = 0; i < players.length; i++) {
			LoadPlayer(players[i]);
		}
	}
	
	@Override
	public void onDisable(){
		SavePlayerData();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		LoadPlayer(player);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if (ePlayers.containsKey(player)) {
			ePlayers.get(player).Destroy(this);
			ePlayers.remove(player);
		}
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
		        while (it.hasNext()) {
		            Map.Entry<Material, Integer> pairs = (Map.Entry<Material, Integer>)it.next();
		            items += "\n" + shopItemNames2.get(pairs.getKey()) + " - $" + pairs.getValue().toString();
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
		        while (it.hasNext()) {
		            Map.Entry<Material, Integer> pairs = (Map.Entry<Material, Integer>)it.next();
		            items += "\n" + xpShopItemNames2.get(pairs.getKey()) + " - $" + pairs.getValue().toString();
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
		return -1;
	}

	@Override
	public long getXp(Player player) {
		if (ePlayers.containsKey(player))
			return ePlayers.get(player).getXp();
		return -1;
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
		        
		        if (i > 0) {
		        player.sendMessage(i + "/" + amount + " " + shopItemNames2.get(material) +
		        		" bought for $" + i*shopItems.get(material) + ". You have $" + eplayer.getMoney() + " left.");
		        }
		        else {
		        	player.sendMessage("You don't have enough money to buy " + shopItemNames2.get(material).toString() + ". " + 
		        			shopItemNames2.get(material).toString() + " costs $" + shopItems.get(material).toString() + ".");
		        }
		        
		        return (i > 0);
			}
			else
			{
				player.sendMessage("There is no such buyable item. Say '/buy help' to list items.");
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
						if (!eplayer.XpBuy((long)(int)xpShopItems.get(material), material))
							break;
					}
		        }
		        else {
		        	player.sendMessage("There is no such buyable item. Say '/xpbuy help' to list items.");
		        	return false;
		        }
		        
		        if (i > 0) {
			        player.sendMessage(i + "/" + amount + " " + xpShopItemNames2.get(material).toString() +
			        		" bought for $" + i*xpShopItems.get(material) + ". You have $" + eplayer.getXp() + " left.");
		        }
		        else {
		        	player.sendMessage("You don't have enough money(XP) to buy " + xpShopItemNames2.get(material).toString() + ". " + 
		        			xpShopItemNames2.get(material).toString() + " costs $" + xpShopItems.get(material).toString() + " XP.");
		        }
		        
		        return (i > 0);
			}
			else
			{
				player.sendMessage("There is not such buyable item. Say '/xpbuy help' to list items.");
			}
		}
		return false;
	}
	
	public void SavePlayerData()
	{
		Iterator<Entry<Player, EPlayer>> iterator = ePlayers.entrySet().iterator();
		
		while (iterator.hasNext()) {
			iterator.next().getValue().Destroy(this);
		}
			/*File file = new File(getDataFolder() + File.separator + "playerdata.yml");
			
			if (!file.exists()) {
				try {
					file.createNewFile();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				YamlConfiguration config = new YamlConfiguration();
				
				Iterator<Entry<Player, EPlayer>> iterator = ePlayers.entrySet().iterator();
				
				while (iterator.hasNext()) {
					try {
					EPlayer player = iterator.next().getValue();
					
					List<Integer> inventory = player.getInventoryData();
					
					config.set(player.getPlayer().getName() + ".xp", player.getXp());
					config.set(player.getPlayer().getName() + ".inventory", inventory);
					
					}
					catch (Exception e) {
						 //TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				config.save(file);
			}
			catch (IOException e) {
				 //TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
	}
	
	public boolean BuyShopItem(Player player, String material, int amount) {
		Material m;
		
		if (shopItemNames.containsKey(material.toLowerCase()))
			m = shopItemNames.get(material);
		else
			m = Material.AIR;
		
		return BuyShopItem(player, m, amount);
	}
	
	public boolean BuyXPShopItem(Player player, String material, int amount) {
		Material m;
		
		if (xpShopItemNames.containsKey(material.toLowerCase()))
			m = xpShopItemNames.get(material);
		else
			m = Material.AIR;
		
		return BuyXPShopItem(player, m, amount);
	}

	@Override
	public void RegisterShopItem(Material material, long price, String name, boolean maxOne) {
		shopItems.put(material, (Integer)(int)price);
		shopItemNames.put(name.toLowerCase(), material);
		shopItemNames2.put(material, name.toLowerCase());
	}

	@Override
	public void RegisterXPShopItem(Material material, long price, String name, boolean maxOne) {
		xpShopItems.put(material, (Integer)(int)price);
		xpShopItemNames.put(name.toLowerCase(), material);
		xpShopItemNames2.put(material, name.toLowerCase());
	}
	
}

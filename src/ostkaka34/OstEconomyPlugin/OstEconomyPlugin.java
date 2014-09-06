package ostkaka34.OstEconomyPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class OstEconomyPlugin extends JavaPlugin implements IOstEconomy, Listener
{
	protected class Pair<A, B>
	{
		public A first;
		public B second;
		
		public Pair(A first, B second)
		{
			this.first = first;
			this.second = second;
		}
	}
	
	protected Economy econ = null;
	protected Map<Player, Pair<String, Double>> transfers = new HashMap<Player, Pair<String, Double>>();
	
	protected Map<Player, EPlayer> ePlayers = new HashMap<Player, EPlayer>();
	protected Map<String, ShopItem> shopItems = new HashMap<String, ShopItem>();
	protected ShopInventoryManager shopInventoryManager;
	protected Random random = new Random();

	protected final double xpAmount = 1000;
	
	protected void LoadPlayer(Player player)
	{
		if (ePlayers.containsKey(player))
		{
			ePlayers.get(player).Save();
			ePlayers.remove(player);
		}
		ePlayers.put(player, new EPlayer(player, this));
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

	@Override
	public void onEnable()
	{
		this.shopInventoryManager = new ShopInventoryManager(this.shopItems);
		Player[] players = getServer().getOnlinePlayers();
		File folder = new File(this.getDataFolder() + File.separator + "playerdata");
		if (!folder.exists())
			folder.mkdir();

		for (int i = 0; i < players.length; i++)
			LoadPlayer(players[i]);
		
		setupEconomy();

		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable()
	{
		SavePlayerData();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		LoadPlayer(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		if (ePlayers.containsKey(player))
		{
			ePlayers.get(player).Save();
			ePlayers.remove(player);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
        if(!(sender instanceof Player)) {
            return true;
        }
		
		if (ePlayers.containsKey(sender) && ePlayers.containsKey(sender))
		{
			EPlayer player = ePlayers.get(sender);

			if (cmd.getName().equalsIgnoreCase("buy"))
			{
				shopInventoryManager.CreateShowMoneyShop(player);
				return true;
			}
			else if (cmd.getName().equalsIgnoreCase("xpbuy"))
			{
				sender.sendMessage("Say /buyxp to buy xp with 'real money' or kill zombies to get xp.");
				shopInventoryManager.CreateShowXpShop(player);
				return true;
			}
			else if (cmd.getName().equalsIgnoreCase("buyxp"))
			{
				if (econ == null)
				{
					sender.sendMessage("Could not find an economy plugin. Try again later.");
					return true;
				}
					
				if (args.length >= 1)
				{
					double amountOfReal = 0.0;
					
					amountOfReal = Double.parseDouble(args[0]);
					
					if (amountOfReal <= 0.0)
						return false;
					
					String key = "";
					for (int i = 0; i < 4; i++)
						key += String.valueOf(random.nextInt(10));
					
					if (transfers.containsKey(sender))
						transfers.remove(sender);
					
					transfers.put((Player)sender , new Pair<String, Double>(key, (Double)amountOfReal));
					
					sender.sendMessage("Are you use you want to buy " + String.valueOf(amountOfReal*xpAmount) + " xp for $" + String.valueOf(amountOfReal) + " ?");
					sender.sendMessage("say '/acceptbuyxp " + key + "' to accept transef. Say '/denybuyxp' to deny.");
					
					return true;
				}
				else
				{
					return false;
				}
			}
			else if (cmd.getName().equalsIgnoreCase("acceptbuyxp"))
			{
				if (econ == null)
				{
					sender.sendMessage("Could not find an economy plugin. Try again later.");
					return true;
				}
				
				if (args.length >= 1)
				{
					if (transfers.containsKey(sender))
					{
						Pair<String, Double> pair = transfers.get(sender);
						if (pair.first == args[0])
						{
							double amountOfReal = pair.second;
							
							if (amountOfReal <= 0)
								return false;
							
				            EconomyResponse r = econ.depositPlayer((Player)sender, -amountOfReal);
				            
				            if(r.transactionSuccess())
				            {
				            	player.GiveMoney((long)(r.amount * xpAmount));
				                sender.sendMessage(String.format("You bought %s xp for %s and now have %s and %s xp.",
				                		r.amount*xpAmount, 
				                		econ.format(r.amount), 
				                		econ.format(r.balance), 
				                		player.xp));
				            }
				            else
				            {
				                sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
				            }
							
							
							return true;
						}
						else
						{
							sender.sendMessage("Wrong code! Say /buyxp again if you forgot the code.");
							return true;
						}
					}
				}
			}
			else if (cmd.getName().equalsIgnoreCase("denybuyxp"))
			{
				if (econ == null)
				{
					sender.sendMessage("Could not find an economy plugin. Try again later.");
					return true;
				}
				
				if (transfers.containsKey(sender))
				{
					transfers.remove(sender);
					return true;
				}
				return false;
			}
			
		}

		return false;
	}

	@Override
	public long getMoney(Player player)
	{
		if (ePlayers.containsKey(player))
			return ePlayers.get(player).getMoney();
		return -1;
	}

	@Override
	public long getXp(Player player)
	{
		if (ePlayers.containsKey(player))
			return ePlayers.get(player).getXp();
		return -1;
	}

	@Override
	public void GiveMoney(Player player, long money)
	{
		if (ePlayers.containsKey(player))
			ePlayers.get(player).GiveMoney(money);
	}

	@Override
	public void ResetStats(Player player)
	{
		if (ePlayers.containsKey(player))
			ePlayers.get(player).Reset();
	}

	@Override
	public boolean BuyShopItem(Player player, String name, int amount)
	{
		if (ePlayers.containsKey(player))
		{
			EPlayer eplayer = ePlayers.get(player);

			if (shopItems.containsKey(name))
			{
				ShopItem item = shopItems.get(name);
				if (item.getXpCost() != 0)
				{
					int i = 0;
					for (i = 0; i < amount; i++)
					{
						if (!eplayer.XpBuy(item.getXpCost(), item.getMaterial(), item.maxOne))
							break;
					}
					if (i > 0)
						player.sendMessage(i + "/" + amount + " " + item.getName() + " bought for $" + i * item.getXpCost() + ". You have $" + eplayer.getXp() + " left.");
					else
						player.sendMessage("Not enough money(XP). " + item.getName() + " costs $" + item.getXpCost() + " XP.");
					return (i > 0);
				}
				else
				{
					int i = 0;
					for (i = 0; i < amount; i++)
					{
						if (!eplayer.Buy(item.getMoneyCost(), item.getMaterial()))
							break;
					}
					if (i > 0)
						player.sendMessage(i + "/" + amount + " " + item.getName() + " bought for $" + i * item.getMoneyCost() + ". You have $" + eplayer.getMoney() + " left.");
					else
						player.sendMessage("Not enough money. " + item.getName() + " costs $" + item.getMoneyCost() + ".");
					return (i > 0);
				}
			}
			else
				player.sendMessage("There is not such buyable item. Say '/xpbuy help' to list items.");
		}
		return false;
	}

	public void SavePlayerData()
	{
		Iterator<Entry<Player, EPlayer>> iterator = ePlayers.entrySet().iterator();

		while (iterator.hasNext())
		{
			iterator.next().getValue().Save();
		}
	}

	@Override
	public void RegisterShopItem(String name, Material material, int moneyCost, int xpCost, boolean maxOne, int amount)
	{
		ShopItem item = new ShopItem(name, material, moneyCost, xpCost, maxOne);
		item.setAmount(amount);
		this.shopItems.put(name, item);
		shopInventoryManager.setShopItems(this.shopItems);
	}
	
	public String MaterialToName(Material material)
	{
		for(ShopItem item : shopItems.values())
		{
			if(item.getMaterial() == material)
				return item.getName();
		}
		return null;
	}

	public static OstEconomyPlugin getPlugin()
	{
		return (OstEconomyPlugin) Bukkit.getPluginManager().getPlugin("OstEconomyPlugin");
	}

}

package ostkaka34.OstEconomyPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OstEconomyPlugin extends JavaPlugin implements IOstEconomy, Listener
{

	protected Map<Player, EPlayer> ePlayers = new HashMap<Player, EPlayer>();
	protected Map<String, ShopItem> shopItems = new HashMap<String, ShopItem>();
	protected ShopInventoryManager shopInventoryManager;

	protected void LoadPlayer(Player player)
	{
		if (ePlayers.containsKey(player))
		{
			ePlayers.get(player).Save();
			ePlayers.remove(player);
		}
		ePlayers.put(player, new EPlayer(player, this));
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
				shopInventoryManager.CreateShowXpShop(player);
				return true;
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
						if (!eplayer.XpBuy(item.getXpCost(), item.getMaterial()))
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

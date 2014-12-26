package ostkaka34.OstEconomyPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EPlayer
{
	protected Player player;
	protected long money = 1000;
	protected long xp = 0;
	protected List<IShopItem> xpInventory = new ArrayList<IShopItem>();
	protected File configFile;

	public EPlayer(Player player, OstEconomyPlugin plugin)
	{
		this.player = player;
		configFile = new File("../playerdata/" + player.getName() + ".yml");
		if (!configFile.exists())
		{
			try
			{
				configFile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			this.SaveConfig();
		}
		this.Load(plugin);
	}
	
	protected void SaveConfig()
	{
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			List<String> inventory = getInventoryData();
			
			config.set("xp", getXp());
			config.set("inventory", inventory);
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void LoadConfig(OstEconomyPlugin plugin)
	{
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			List<String> inventory;
			
			config.load(configFile);
			xp = config.getLong("xp");
			
			inventory = config.getStringList("inventory");
			
			xpInventory.clear();
			Iterator<String> iterator = inventory.iterator();
			while (iterator.hasNext())
			{
				IShopItem shopItem = plugin.getShopItem(iterator.next());
				xpInventory.add(shopItem);
			}
		}
		catch (IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	public void Save()
	{
		this.SaveConfig();
	}
	
	public void Load(OstEconomyPlugin plugin)
	{
		this.LoadConfig(plugin);
	}

	protected boolean PutInInventory(Material item, int amount)
	{
		player.getInventory().addItem(new ItemStack(item, amount));
		player.updateInventory();
		return true;
	}

	public void Reset(OstEconomyPlugin plugin)
	{
		this.Load(plugin);
		this.money = 0;
		player.getInventory().clear();
		
		PutInInventory(Material.COOKED_BEEF, 16);
		PutInInventory(Material.TORCH, 16);
		PutInInventory(Material.LADDER, 16);
		PutInInventory(Material.WOOD, 8);
		PutInInventory(Material.WEB, 8);
		PutInInventory(Material.WOOL, 16);

		
		ListIterator<IShopItem> iterator = xpInventory.listIterator(xpInventory.size());
		
		while (iterator.hasPrevious())
			iterator.previous().RecoverBoughtItem(this.getPlayer());
			//player.getInventory().addItem(new ItemStack(iterator.previous()));
		
		PutInInventory(Material.STONE_SPADE, 1);
		PutInInventory(Material.STICK, 16);

		this.Save();
		player.updateInventory();
	}

	public boolean Buy(IShopItem shopItem)
	{
		if (this.money > shopItem.getMoneyCost())
		{
			if (shopItem.OnBuyItem(this.getPlayer()))//(PutInInventory(shopItem, amount))
			{
				this.money -= shopItem.getMoneyCost();
				this.Save();
				return true;
			}
		}
		return false;
	}

	public boolean XpBuy(IShopItem shopItem)
	{
		if (this.xp > shopItem.getXpCost())
		{
			if ((!xpInventory.contains(shopItem) && shopItem.getAmount() == 1) || !shopItem.getMaxOne())
			{
				if (shopItem.OnBuyItem(this.getPlayer()))
				{
					this.xp -= shopItem.getXpCost();
						xpInventory.add(shopItem);
					
					this.Save();
					return true;
				}
			}
		}
		return false;
	}

	public void GiveMoney(long money)
	{
		this.money += money;
		this.xp += money;
		this.Save();
	}

	public List<Material> getXpInventory()
	{
		return null;
	}

	public long getMoney()
	{
		return this.money;
	}

	public long getXp()
	{
		return this.xp;
	}

	public Player getPlayer()
	{
		return player;
	}

	public List<String> getInventoryData()
	{
		List<String> inventory = new ArrayList<String>();

		Iterator<IShopItem> iterator = xpInventory.iterator();
		while (iterator.hasNext())
			inventory.add(iterator.next().getName());

		return inventory;
	}


}

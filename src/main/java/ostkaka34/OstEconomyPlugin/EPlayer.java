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
	protected List<Material> xpInventory = new ArrayList<Material>();
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
		this.Load();
	}
	
	protected void SaveConfig()
	{
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			List<Integer> inventory = getInventoryData();
			config.set("xp", getXp());
			config.set("inventory", inventory);
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	protected void LoadConfig()
	{
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			config.load(configFile);
			xp = config.getLong("xp");
			List<Integer> inventory = config.getIntegerList("inventory");
			xpInventory.clear();
			Iterator<Integer> iterator = inventory.iterator();
			while (iterator.hasNext())
			{
				xpInventory.add(Material.getMaterial(iterator.next()));
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
	
	public void Load()
	{
		this.LoadConfig();
	}

	@SuppressWarnings("deprecation")
	protected boolean PutInInventory(Material item, int amount)
	{
		player.getInventory().addItem(new ItemStack(item, amount));
		player.updateInventory();
		return true;
	}

	@SuppressWarnings("deprecation")
	public void Reset()
	{
		this.Load();
		this.money = 0;
		player.getInventory().clear();
		
		PutInInventory(Material.COOKED_BEEF, 16);
		PutInInventory(Material.TORCH, 16);
		PutInInventory(Material.LADDER, 16);
		PutInInventory(Material.WOOD, 8);
		PutInInventory(Material.WEB, 8);
		PutInInventory(Material.WOOL, 16);

		
		ListIterator<Material> iterator = xpInventory.listIterator(xpInventory.size());
		
		while (iterator.hasPrevious())
			player.getInventory().addItem(new ItemStack(iterator.previous()));
		
		PutInInventory(Material.STONE_SPADE, 1);
		
		PutInInventory(Material.STICK, 16);

		this.Save();
		player.updateInventory();
	}

	public boolean Buy(long money, Material item, int amount)
	{
		if (this.money > money)
		{
			if (PutInInventory(item, amount))
			{
				this.money -= money;
				this.Save();
				return true;
			}
		}
		return false;
	}

	public boolean XpBuy(long xp, Material item, boolean maxOne, int amount)
	{
		if (this.xp > xp)
		{
			if ((!xpInventory.contains(item) && amount == 1) || !maxOne)
			{
				if (PutInInventory(item, amount))
				{
					this.xp -= xp;
					for (int i = 0; i < amount; i++)
						xpInventory.add(item);
					
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

	@SuppressWarnings("deprecation")
	public List<Integer> getInventoryData()
	{
		List<Integer> inventory = new ArrayList<Integer>();

		Iterator<Material> iterator = xpInventory.iterator();
		while (iterator.hasNext())
			inventory.add(iterator.next().getId());

		return inventory;
	}


}

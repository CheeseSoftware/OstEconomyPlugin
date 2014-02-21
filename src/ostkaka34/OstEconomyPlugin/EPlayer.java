package ostkaka34.OstEconomyPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EPlayer {
	protected Player player;
	protected long money = 1000;
	protected long xp = 0;
	protected List<Material> xpInventory = new ArrayList<Material>();
	
	@SuppressWarnings("deprecation")
	public EPlayer(Player player, OstEconomyPlugin plugin) {
		this.player = player;
		
		//File file = plugin.getDataFolder();
		File file = new File(plugin.getDataFolder() + File.separator + "playerdata.yml");
		
		
		if (file.exists()) {
			YamlConfiguration config = new YamlConfiguration();
			
			try {
				config.load(file);
				xp = config.getLong(player.getName() + "xp");
				List<Integer> inventory = config.getIntegerList(player.getName() + "inventory");
				
				//config.s
				
				Iterator<Integer> iterator = inventory.iterator();
				while(iterator.hasNext()) {
					xpInventory.add(Material.getMaterial(iterator.next()));
				}
				
				
			} catch (IOException | InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			xpInventory.add(Material.WOOD_SPADE);
			for (int i = 0; i < 4; i++)
				PutInInventory(Material.STICK);
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void Destroy(OstEconomyPlugin plugin) {
		File file = new File(plugin.getDataFolder() + File.separator + "playerdata.yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		YamlConfiguration config = new YamlConfiguration();
			
		try {
			List<Integer> inventory = new ArrayList<Integer>();
				
			Iterator<Material> iterator = xpInventory.iterator();
			while(iterator.hasNext()) {
				inventory.add(iterator.next().getId());
			}
				
			config.set(player.getName() + "xp", this.xp);
			config.set(player.getName() + "inventory", inventory);
			config.save(file);
				
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	protected boolean PutInInventory(Material item) {
		Inventory inventory = player.getInventory();

		inventory.addItem(new ItemStack(item, 1));
		player.updateInventory();
		return true;
		
		//if (inventory.)) {
		//	int place = inventory.firstEmpty();
		//	inventory.setItem(place, new ItemStack(item, 1));
		//	return true;
		//}
		//return false;
	}
	
	@SuppressWarnings("deprecation")
	public void Reset()
	{
		this.money = 1000;
		
		player.getInventory().clear();
		
		Iterator<Material> iterator = xpInventory.iterator();
		while(iterator.hasNext()) {
			player.getInventory().addItem(new ItemStack(iterator.next()));
		}
		player.updateInventory();
		
	}
	
	public boolean Buy(long money, Material item) {
		if (this.money > money)
		{
			if (!xpInventory.contains(item))
			{
				if (PutInInventory(item))
				{
					this.money -= money;
					xpInventory.add(item);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean XpBuy(long xp, Material item) {
		if (this.xp > xp)
		{
			if (PutInInventory(item))
			{
				this.xp -= xp;
				xpInventory.add(item);
				return true;
			}
		}
		return false;
	}
	
	public void GiveMoney(long money) {
		this.money += money;
		this.xp += money;
	}
	
	public List<Material> getXpInventory() {
		return null;
	}
	
	public long getMoney() {
		return money;
	}
	
	public long getXp() {
		return xp;
	}
	
	public Player getPlayer() {
		return player;
	}
}

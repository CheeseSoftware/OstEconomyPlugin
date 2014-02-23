package ostkaka34.OstEconomyPlugin;

import java.io.File;
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
		File file = new File(plugin.getDataFolder() + File.separator + "playerdata" + File.separator + player.getName() + ".yml");
		
		
		if (file.exists()) {
			YamlConfiguration config = new YamlConfiguration();
			
			try {
				config.load(file);
				xp = config.getLong(player.getName() + ".xp");
				List<Integer> inventory = config.getIntegerList(player.getName() + ".inventory");
				
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
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void Destroy(OstEconomyPlugin plugin) {
		File file = new File(plugin.getDataFolder() + File.separator + "playerdata" + File.separator + player.getName() + ".yml");
		
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
			
			//Iterator<Entry<Player, EPlayer>> iterator = ePlayers.entrySet().iterator();
			
			/*while (iterator.hasNext()) {
				try {
				EPlayer player = iterator.next().getValue();*/
				
				List<Integer> inventory = getInventoryData();
				
				config.set("xp", getXp());
				config.set("inventory", inventory);
				
			//}
			
			config.save(file);
		}
		catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	protected boolean PutInInventory(Material item, int amount) {
		Inventory inventory = player.getInventory();

		inventory.addItem(new ItemStack(item, amount));
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
		
		PutInInventory(Material.COOKED_BEEF, 16);
		PutInInventory(Material.WOOD_SPADE, 1);
		PutInInventory(Material.STICK, 16);
		PutInInventory(Material.TORCH, 16);
		PutInInventory(Material.WOOL, 16);
		PutInInventory(Material.WEB, 4);
		
		player.updateInventory();
		
	}
	
	public boolean Buy(long money, Material item) {
		if (this.money > money)
		{
			if (PutInInventory(item,1))
			{
				this.money -= money;
				return true;
			}
		}
		return false;
	}
	
	public boolean XpBuy(long xp, Material item) {
		if (this.xp > xp)
		{
			if (!xpInventory.contains(item))
			{
				if (PutInInventory(item,1))
				{
					this.xp -= xp;
					xpInventory.add(item);
					return true;
				}
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
		return this.money;
	}
	
	public long getXp() {
		return this.xp;
	}
	
	public Player getPlayer() {
		return player;
	}

	@SuppressWarnings("deprecation")
	public List<Integer> getInventoryData() {
		List<Integer> inventory = new ArrayList<Integer>();
		
		Iterator<Material> iterator = xpInventory.iterator();	
		while(iterator.hasNext())
			inventory.add(iterator.next().getId());

		return inventory;
	}
}

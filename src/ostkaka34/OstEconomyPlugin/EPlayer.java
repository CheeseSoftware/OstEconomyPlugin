package ostkaka34.OstEconomyPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EPlayer {
	protected Player player;
	protected long money = 0;
	protected long xp = 0;
	protected List<Material> xpInventory = new ArrayList<Material>();
	
	public EPlayer(Player player) {
		this.player = player;
		
		xpInventory.add(Material.WOOD_SPADE);
		xpInventory.add(Material.STICK);
		
		Reset();
	}
	
	protected boolean PutInInventory(Material item) {
		Inventory inventory = player.getInventory();
		
		inventory.addItem(new ItemStack(item, 1));
		
		return true;
		//if (inventory.)) {
		//	int place = inventory.firstEmpty();
		//	inventory.setItem(place, new ItemStack(item, 1));
		//	return true;
		//}
		//return false;
	}
	
	public void Reset()
	{
		this.money = 0;
		
		player.getInventory().clear();
		
		Iterator<Material> iterator = xpInventory.iterator();
		while(iterator.hasNext()) {
			PutInInventory(iterator.next());
		}
	}
	
	public boolean Buy(long money, Material item) {
		if (this.money > money)
		{
			if (PutInInventory(item))
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

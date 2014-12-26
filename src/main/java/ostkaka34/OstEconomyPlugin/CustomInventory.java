package ostkaka34.OstEconomyPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CustomInventory
{
	protected Inventory inventory = null;
	protected int size;
	protected String title;
	
	public CustomInventory(Player player, int size, String title)
	{
		this.size = size;
		this.title = title;
		if(this.size < 9)
			this.size = 9;
		else
			this.size = size - size%9 + 9;//(int)(Math.ceil(this.size/9)*9);
		this.inventory = Bukkit.createInventory(player, this.size, title);
		this.inventory.clear();
		player.updateInventory();
	}
	
	public void Show()
	{
		((Player)inventory.getHolder()).openInventory(this.inventory);
	}
	
	public void Hide()
	{
		((Player)inventory.getHolder()).closeInventory();
	}
}

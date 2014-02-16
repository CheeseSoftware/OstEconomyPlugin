package ostkaka34.OstEconomyPlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IOstEconomy extends Plugin {
	public long getMoney(Player player);
	public long getXp(Player player);
	
	public void GiveMoney(Player player, long money);
	public void ResetStats(Player player);
	
	public boolean BuyShopItem(Player player, Material material, int amount);
	public boolean BuyXPShopItem(Player player, Material material, int amount);
	
	public boolean BuyShopItem(Player player, String material, int amount);
	public boolean BuyXPShopItem(Player player, String material, int amount);
	
	public void RegisterShopItem(Material material, long price, String name, boolean maxOne);
	public void RegisterXPShopItem(Material material, long price, String name, boolean maxOne);
}

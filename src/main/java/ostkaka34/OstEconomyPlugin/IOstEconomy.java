package ostkaka34.OstEconomyPlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IOstEconomy extends Plugin {

	public long getMoney(Player player);
	public long getXp(Player player);
	
	public void GiveMoney(Player player, long money);
	public void ResetStats(Player player);
	
	public boolean BuyShopItem(Player player, IShopItem shopItem, int amount);
	public boolean BuyShopItem(Player player, String name, int amount);
	
	public void RegisterShopItem(IShopItem shopItem);
	
	

	@Deprecated
	public void RegisterShopItem(String name, Material material, int moneyCost, int xpCost, boolean maxOne, int amount);
	
	@Deprecated
	public String MaterialToName(Material material);
}

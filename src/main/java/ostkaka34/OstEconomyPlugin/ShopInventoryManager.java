package ostkaka34.OstEconomyPlugin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopInventoryManager implements Listener
{
	Map<Player, ShopInventory> shopInventories = new HashMap<Player, ShopInventory>();
	Map<String, ShopItem> shopItems = new HashMap<String, ShopItem>();
	Map<String, ShopItem> moneyShopItems = new HashMap<String, ShopItem>();
	Map<String, ShopItem> xpShopItems = new HashMap<String, ShopItem>();

	public ShopInventoryManager(Map<String, ShopItem> shopItems)
	{
		this.setShopItems(shopItems);
		Bukkit.getPluginManager().registerEvents(this, OstEconomyPlugin.getPlugin());
	}

	public void setShopItems(Map<String, ShopItem> shopItems)
	{
		this.shopItems = shopItems;
		for (ShopItem item : shopItems.values())
		{
			if (item.getMoneyCost() != 0)
				this.moneyShopItems.put(item.getName(), item);
			else if (item.getXpCost() != 0)
				this.xpShopItems.put(item.getName(), item);
		}
	}

	public void CreateShowMoneyShop(EPlayer player)
	{
		ShopInventory shop = new ShopInventory(player.getPlayer(), "Money Shop - You have $" + player.getMoney() + ".", moneyShopItems);
		shopInventories.put(player.getPlayer(), shop);
		shop.Show();
	}

	public void CreateShowXpShop(EPlayer player)
	{
		ShopInventory shop = new ShopInventory(player.getPlayer(), "XP Shop - You have $" + player.getXp() + " XP.", xpShopItems);
		// CustomInventory shop = new CustomInventory(player, 27, "LOL");
		shopInventories.put(player.getPlayer(), shop);
		shop.Show();
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if (event.getPlayer() instanceof Player)
		{
			Player player = (Player) event.getPlayer();
			if (shopInventories.containsKey(player))
			{
				shopInventories.remove(player);
				player.sendMessage("Shop closed.");
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getWhoClicked() instanceof Player)
		{
			Player player = (Player) event.getWhoClicked();
			if (shopInventories.containsKey(player))
			{
				ShopInventory shop = shopInventories.get(player);
				ItemStack itemStack = event.getCurrentItem();
				if (itemStack != null)
				{
					ItemMeta meta = itemStack.getItemMeta();
					if (meta != null)
					{
						String name = meta.getDisplayName();
						ShopItem shopItem = shop.getItems().get(name);
						if (shopItem != null)
						{
							OstEconomyPlugin.getPlugin().BuyShopItem(player, name, 1);
						}
					}
					event.setCancelled(true);
				}
			}
		}
	}
}

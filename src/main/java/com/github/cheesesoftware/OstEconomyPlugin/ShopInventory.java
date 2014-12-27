package com.github.cheesesoftware.OstEconomyPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopInventory extends CustomInventory {
    Map<String, IShopItem> shopItems;

    public ShopInventory(Player player, String title, Map<String, IShopItem> shopItems) {
	super(player, shopItems.size(), title);
	this.shopItems = shopItems;
	for (IShopItem item : shopItems.values()) {
	    ItemStack itemStack = new ItemStack(item.getVisibleShopMaterial());
	    itemStack.setAmount(item.getAmount());

	    ItemMeta itemMeta = itemStack.getItemMeta();

	    List<String> lore = itemMeta.getLore();
	    if (lore == null)
		lore = new ArrayList<String>();
	    lore.clear();
	    if (item.getXpCost() != 0)
		lore.add("XP Cost: $" + item.getXpCost());
	    else
		lore.add("Cost: $" + item.getMoneyCost());
	    itemMeta.setLore(lore);

	    itemMeta.setDisplayName(item.getName());
	    itemStack.setItemMeta(itemMeta);

	    this.inventory.addItem(itemStack);
	}
    }

    public Map<String, IShopItem> getItems() {
	return this.shopItems;
    }

}

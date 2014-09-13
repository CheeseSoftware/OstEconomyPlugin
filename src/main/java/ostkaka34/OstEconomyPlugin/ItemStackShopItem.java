package ostkaka34.OstEconomyPlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackShopItem implements IShopItem
{
	protected ItemStack itemStack;
	protected String name;
	protected int moneyCost;
	protected int xpCost;
	protected boolean maxOne;
	
	public ItemStackShopItem(String name, ItemStack itemStack, int moneyCost, int xpCost, boolean maxOne, int amount)
	{
		this.name = name;
		this.itemStack = itemStack;
		this.moneyCost = moneyCost;
		this.xpCost = xpCost;
		this.maxOne = maxOne;
		
		// Set name of item stack.
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		
		itemMeta.setDisplayName(name);
		this.itemStack.setItemMeta(itemMeta);
	}
	
	@Override
	public boolean OnBuyItem(Player player) {
		return PutInInventory(player);
	}

	@Override
	public boolean RecoverBoughtItem(Player player)
	{
		return PutInInventory(player);
	}

	@Override
	public Material getVisibleShopMaterial() {
		return itemStack.getType();
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public int getMoneyCost()
	{
		return this.moneyCost;
	}

	@Override
	public int getXpCost()
	{
		return this.xpCost;
	}

	@Override
	public void setAmount(int amount)
	{
		itemStack.setAmount(amount);
	}

	@Override
	public int getAmount()
	{
		return itemStack.getAmount();
	}
	
	@Override
	public boolean getMaxOne()
	{
		return this.maxOne;
	}
	
	@SuppressWarnings("deprecation")
	protected boolean PutInInventory(Player player)
	{
		boolean canPut = player.getInventory().contains(Material.AIR);
		
		if (!canPut)
		{
			for(ItemStack item : player.getInventory().getContents()){
	            if(item == null) {
	                canPut = true;
	                break;
	            } else if (item.getType() == this.getVisibleShopMaterial()
	            		&& item.getAmount() <= (64 - this.getAmount()) 
	            		&& item.getData() == this.itemStack.getData())
	            {
	            	canPut = true;
	            	break;
	            }
	        }
		}
		
		if (canPut)
		{
			player.getInventory().addItem(this.itemStack);
			player.updateInventory();
			return true;
		}
		return false;
	}
	
}

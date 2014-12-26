package ostkaka34.OstEconomyPlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopItem implements IShopItem
{
	protected Material material;
	protected String name;
	protected int moneyCost;
	protected int xpCost;
	protected boolean maxOne;
	protected int amount;
	
	public ShopItem(String name, Material material, int moneyCost, int xpCost, boolean maxOne, int amount)
	{
		this.name = name;
		this.material = material;
		this.moneyCost = moneyCost;
		this.xpCost = xpCost;
		this.maxOne = maxOne;
		this.amount = amount;
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
		return this.material;
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
		this.amount = amount;
	}

	@Override
	public int getAmount()
	{
		return this.amount;
	}
	
	@Override
	public boolean getMaxOne()
	{
		return this.maxOne;
	}
	
	protected boolean PutInInventory(Player player)
	{
		boolean canPut = player.getInventory().contains(Material.AIR);
		
		if (!canPut)
		{
			for(ItemStack item : player.getInventory().getContents()){
	            if(item == null) {
	                canPut = true;
	                break;
	            } else if (item.getType() == this.material && item.getAmount() <= (64 - this.getAmount())) { 
	            	canPut = true;
	            	break;
	            }
	        }
		}
		
		if (canPut)
		{
			player.getInventory().addItem(new ItemStack(this.material, this.amount));
			player.updateInventory();
			return true;
		}
		return false;
	}
	
}

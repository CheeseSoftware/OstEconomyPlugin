package ostkaka34.OstEconomyPlugin;

import org.bukkit.Material;

public class ShopItem
{
	protected Material material;
	protected String name;
	protected int moneyCost;
	protected int xpCost;
	protected boolean maxOne;
	protected int amount = 1;
	
	public ShopItem(String name, Material material, int moneyCost, int xpCost, boolean maxOne)
	{
		this.name = name;
		this.material = material;
		this.moneyCost = moneyCost;
		this.xpCost = xpCost;
		this.maxOne = maxOne;
	}
	
	public Material getMaterial()
	{
		return this.material;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getMoneyCost()
	{
		return this.moneyCost;
	}
	
	public int getXpCost()
	{
		return this.xpCost;
	}
	
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	public int getAmount()
	{
		return this.amount;
	}
	
	public boolean getMaxOne()
	{
		return this.maxOne;
	}
	
}

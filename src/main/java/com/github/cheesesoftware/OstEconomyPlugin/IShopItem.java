package com.github.cheesesoftware.OstEconomyPlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface IShopItem {

    public boolean OnBuyItem(Player player);

    public boolean RecoverBoughtItem(Player Player);

    public Material getVisibleShopMaterial();

    public String getName();

    public int getMoneyCost();

    public int getXpCost();

    public void setAmount(int amount);

    public int getAmount();

    public boolean getMaxOne();
}

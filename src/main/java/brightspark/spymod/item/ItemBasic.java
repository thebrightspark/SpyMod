package brightspark.spymod.item;

import brightspark.spymod.SpyMod;
import net.minecraft.item.Item;

public class ItemBasic extends Item
{
    public ItemBasic(String itemName)
    {
        setCreativeTab(SpyMod.TAB);
        setUnlocalizedName(itemName);
        setRegistryName(itemName);
    }
}

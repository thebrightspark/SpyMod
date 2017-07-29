package brightspark.spymod.item.gun;

import brightspark.spymod.item.ItemBasic;

public class ItemBullet extends ItemBasic implements IShootable
{
    public ItemBullet(String itemName)
    {
        this(itemName, 16);
    }

    public ItemBullet(String itemName, int maxStackSize)
    {
        super(itemName);
        setMaxStackSize(maxStackSize);
    }

    @Override
    public boolean isClip()
    {
        return false;
    }

    @Override
    public int getMaxAmmo()
    {
        return maxStackSize;
    }
}

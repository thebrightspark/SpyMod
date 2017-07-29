package brightspark.spymod.init;

import brightspark.spymod.item.EItemBasic;
import brightspark.spymod.item.ItemBasicMeta;
import brightspark.spymod.item.gun.AbstractGun;
import brightspark.spymod.item.gun.ItemBulletClip;
import brightspark.spymod.item.gun.ItemPistol;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SMItems
{
    public static List<Item> ITEMS = new ArrayList<>();

    public static final Item itemBasic;
    public static ItemBulletClip itemPistolClip;
    public static final AbstractGun itemPistol;

    static
    {
        addItem(itemBasic = new ItemBasicMeta("basic", EItemBasic.allNames));
        addItem(itemPistolClip = new ItemBulletClip("pistolClip", 10));
        addItem(itemPistol = new ItemPistol());
    }

    private static void addItem(Item item)
    {
        ITEMS.add(item);
    }

    public static Item[] getItems()
    {
        return ITEMS.toArray(new Item[ITEMS.size()]);
    }

    public static ItemStack getBasicItem(EItemBasic basicName)
    {
        return getBasicItem(basicName, 1);
    }

    public static ItemStack getBasicItem(EItemBasic basicName, int stackSize)
    {
        return new ItemStack(itemBasic, stackSize, basicName.ordinal());
    }
}

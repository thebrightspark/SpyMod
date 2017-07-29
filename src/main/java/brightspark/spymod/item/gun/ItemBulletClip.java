package brightspark.spymod.item.gun;

import brightspark.spymod.item.ItemBasic;
import brightspark.spymod.util.NBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBulletClip extends ItemBasic implements IShootable
{
    //TODO: Make texture change depending on how full it is?
    public int clipSize;

    public ItemBulletClip(String itemName, int clipSize)
    {
        super(itemName);
        setMaxStackSize(1);
        this.clipSize = clipSize;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(isInCreativeTab(tab))
        {
            //Add empty and full clips
            ItemStack clip = new ItemStack(this);
            setBulletsAmount(clip, 0);
            items.add(clip.copy());
            setBulletsAmount(clip, clipSize);
            items.add(clip);
        }
    }

    public static void setBulletsAmount(ItemStack stack, int amount)
    {
        NBTHelper.setInteger(stack, "ammo", Math.max(Math.min(amount, ((ItemBulletClip) stack.getItem()).clipSize), 0));
    }

    public static int getBulletsAmount(ItemStack stack)
    {
        return NBTHelper.getInt(stack, "ammo");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add("Bullets: " + getBulletsAmount(stack) + "/" + clipSize);
    }

    @Override
    public boolean isClip()
    {
        return true;
    }

    @Override
    public int getMaxAmmo()
    {
        return clipSize;
    }
}

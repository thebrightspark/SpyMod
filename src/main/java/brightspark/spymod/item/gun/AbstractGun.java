package brightspark.spymod.item.gun;

import brightspark.spymod.SpyMod;
import brightspark.spymod.item.ItemCooldown;
import brightspark.spymod.util.NBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractGun extends ItemCooldown implements IGun
{
    public AbstractGun(String itemName, int maxCooldown)
    {
        super(itemName, maxCooldown);
    }

    /**
     * Called to spawn the bullet(s) in the world
     * Ammo reduction is handled after this
     */
    protected abstract void spawnBullet(World world, EntityPlayer player);

    private int getMaxAmmo()
    {
        return getAmmoItem().getMaxAmmo();
    }

    /**
     * This will get called by onItemRightClick when the cooldown is 0
     * @return True if the cooldown should be started.
     */
    @Override
    protected boolean doRightClickAction(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if(world.isRemote) return true;

        int ammo = getAmmoAmount(stack);

        if(player.isSneaking())
        {
            //Reload
            if(ammo < getMaxAmmo())
            {
                reloadAll(player, stack);
                return true;
            }
            return false;
        }
        else
        {
            if(!player.capabilities.isCreativeMode && ammo == 0)
            {
                //TODO: Play gun out of ammo sound
                return false;
            }
            //Shoot
            spawnBullet(world, player);
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1f, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.5F);
            if(!player.capabilities.isCreativeMode)
            setAmmoAmount(stack, --ammo);
            return true;
        }
    }

    private static boolean isAmmoForGun(ItemStack gun, ItemStack ammo)
    {
        if(gun == null || ammo == null || !(gun.getItem() instanceof IGun) || !(ammo.getItem() instanceof IShootable))
            return false;
        return ((IGun) gun.getItem()).getAmmoItem() == ammo.getItem();
    }

    /**
     * Reloads the gun as much as possible using any ammo clips in the player's inventory
     */
    public static void reloadAll(EntityPlayer player, ItemStack gunStack)
    {
        if(gunStack == null || !(gunStack.getItem() instanceof IGun))
        {
            SpyMod.logger.error("Trying to reload an ItemStack that is null or it's Item isn't an instance of IUseAmmo!");
            return;
        }

        //Check held items first
        for(EnumHand hand : EnumHand.values())
        {
            ItemStack heldStack = player.getHeldItem(hand);
            int remaining = reloadWithStack(gunStack, heldStack);
            if(heldStack.isEmpty() || heldStack.getCount() == 0) player.setHeldItem(hand, ItemStack.EMPTY);
            if(remaining == 0) return;
        }

        //Check any ammo belts before any loose ones in inventory
        /*
        for(int i = 0; i < player.inventory.mainInventory.size(); i++)
        {
            //Find ammo belts
            ItemStack stack = player.inventory.mainInventory.get(i);
            if(!stack.isEmpty() && stack.getItem() == BABItems.itemAmmoBelt)
            {
                //Find ammo clips within the ammo belt
                ItemStackHandler ammoHandler = ItemAmmoBelt.getInventoryHandler(stack);
                for(int j = 0; j < ammoHandler.getSlots(); j++)
                {
                    ItemStack ammoClip = ammoHandler.getStackInSlot(j);
                    int remaining = reloadWithStack(gunStack, ammoClip);
                    if(ammoClip.isEmpty() || ammoClip.getCount() == 0) ammoHandler.setStackInSlot(j, ItemStack.EMPTY);
                    if(remaining == 0) return;
                }
            }
        }
        */

        //Check main inventory
        for(int i = 0; i < player.inventory.mainInventory.size(); i++)
        {
            ItemStack stack = player.inventory.mainInventory.get(i);
            int remaining = reloadWithStack(gunStack, stack);
            if(stack.isEmpty() || stack.getCount() == 0) player.inventory.mainInventory.set(i, ItemStack.EMPTY);
            if(remaining == 0) return;
        }
    }

    /**
     * Reloads the gun with the given stack
     * Returns how many bullets weren't used of the amount given
     */
    public static int reloadWithStack(ItemStack gunStack, ItemStack clipStack)
    {
        if(!isAmmoForGun(gunStack, clipStack) || !(clipStack.getItem() instanceof IShootable)) return -1;
        boolean isClip = ((IShootable) clipStack.getItem()).isClip();
        int ammo = isClip ? ItemBulletClip.getBulletsAmount(clipStack) : clipStack.getCount();
        int remaining = reload(gunStack, ammo);
        if(isClip)
            ItemBulletClip.setBulletsAmount(clipStack, remaining);
        else
            clipStack.setCount(remaining);
        return ((IGun) gunStack.getItem()).getAmmoSpace(gunStack);
    }

    /**
     * Reloads the gun with the given amount of bullets
     * Returns how many bullets weren't used of the amount given
     */
    public static int reload(ItemStack gunStack, int bullets)
    {
        int spaceInGun = ((IGun) gunStack.getItem()).getAmmoSpace(gunStack);
        if(spaceInGun == 0 || bullets == 0)
            return bullets;
        int toRefill = Math.min(spaceInGun, bullets);
        ((IGun) gunStack.getItem()).setAmmoAmount(gunStack, ((IGun) gunStack.getItem()).getAmmoAmount(gunStack) + toRefill);
        return bullets - toRefill;
    }

    @Override
    public void setAmmoAmount(ItemStack stack, int amount)
    {
        NBTHelper.setInteger(stack, "ammo", Math.max(amount, 0));
    }

    @Override
    public int getAmmoAmount(ItemStack stack)
    {
        return NBTHelper.getInt(stack, "ammo");
    }

    @Override
    public int getAmmoSpace(ItemStack stack)
    {
        return getMaxAmmo() - getAmmoAmount(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1 - ((float) getAmmoAmount(stack) / (float) getMaxAmmo());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add("Ammo: " + getAmmoAmount(stack) + "/" + getMaxAmmo());
    }
}

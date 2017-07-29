package brightspark.spymod.item.gun;

import brightspark.spymod.SpyMod;
import brightspark.spymod.init.SMItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemPistol extends AbstractGun
{
    public ItemPistol()
    {
        super("pistol", 10);
    }

    @Override
    public IShootable getAmmoItem()
    {
        return SMItems.itemPistolClip;
    }

    @Override
    protected void spawnBullet(World world, EntityPlayer player)
    {
        SpyMod.logger.info("Pew!");
        //TODO: Create bullet entity
        //world.spawnEntity(new EntityBullet(world, player).setDamage(6f));
    }
}

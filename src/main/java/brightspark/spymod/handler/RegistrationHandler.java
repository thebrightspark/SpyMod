package brightspark.spymod.handler;

import brightspark.spymod.init.SMItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class RegistrationHandler
{
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(SMItems.getItems());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void regModels(ModelRegistryEvent event)
    {
        SMItems.ITEMS.forEach(RegistrationHandler::regModel);
    }

    @SideOnly(Side.CLIENT)
    private static void regModel(Item item)
    {
        regModel(item, 0);
    }

    @SideOnly(Side.CLIENT)
    private static void regModel(Item item, int meta)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}

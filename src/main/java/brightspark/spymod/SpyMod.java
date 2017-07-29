package brightspark.spymod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = SpyMod.MOD_ID, name = SpyMod.MOD_NAME, version = SpyMod.VERSION)
public class SpyMod
{
    public static final String MOD_ID = "spymod";
    public static final String MOD_NAME = "Spy Mod";
    public static final String VERSION = "@VERSION@";
    public static final String GUI_TEXTURE_DIR = "textures/gui/";

    @Mod.Instance(MOD_ID)
    public static SpyMod instance;

    public static Logger logger;

    public static final CreativeTabs TAB = new CreativeTabs(MOD_ID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Blocks.SKULL);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}

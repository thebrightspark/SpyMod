package brightspark.spymod.init;

import brightspark.spymod.SpyMod;
import brightspark.spymod.item.EItemBasic;
import brightspark.spymod.item.gun.ItemBulletClip;
import brightspark.spymod.recipe.ShapelessNBTRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;

public class SMRecipes
{
    private static final ResourceLocation SPY_GROUP = new ResourceLocation(SpyMod.MOD_ID);

    public static void regRecipes()
    {
        //Register Recipe
        RecipeSorter.register("ShapelessNBTRecipe", ShapelessNBTRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        //Bullet
        addShapelessNBTRecipe(new ItemStack(SMItems.itemBasic, 4, EItemBasic.BULLET.ordinal()), "ingotIron", "gunpowder");
        //Empty Bullet Clip
        ItemStack emptyClip = new ItemStack(SMItems.itemPistolClip);
        ItemBulletClip.setBulletsAmount(emptyClip, 0);
        addShapedOreRecipe(emptyClip, "i i", "i i", " i ", 'i', "ingotIron");

        //Bullet Clips
        int max = SMItems.itemPistolClip.clipSize;
        for(int clipSize = 0; clipSize < max; clipSize++)
        {
            for(int numBullets = 1; numBullets <= Math.min(Math.min(max, 8), max - clipSize); numBullets++)
            {
                //Add recipes to fill a bullet clip for every possible combination
                ItemStack[] bullets = new ItemStack[numBullets + 1];
                Arrays.fill(bullets, SMItems.getBasicItem(EItemBasic.BULLET));

                ItemStack inputClip = new ItemStack(SMItems.itemPistolClip);
                ItemBulletClip.setBulletsAmount(inputClip, clipSize);
                bullets[0] = inputClip;

                ItemStack outputClip = new ItemStack(SMItems.itemPistolClip);
                ItemBulletClip.setBulletsAmount(outputClip, clipSize + numBullets);

                addShapelessNBTRecipe(outputClip, (Object[]) bullets);
            }
        }

        //Pistol
        addShapedOreRecipe(SMItems.getBasicItem(EItemBasic.GUN_BARREL), "iii", "iii", 'i', "ingotIron");
        addShapedOreRecipe(SMItems.getBasicItem(EItemBasic.PISTOL_ASSEMBLY), "di", "ii", "i ", 'i', "ingotIron", 'd', "gemDiamond");
        addShapedOreRecipe(new ItemStack(SMItems.itemPistol), "ab", 'b', SMItems.getBasicItem(EItemBasic.GUN_BARREL), 'a', SMItems.getBasicItem(EItemBasic.PISTOL_ASSEMBLY));
    }

    private static void addShapelessOreRecipe(ItemStack output, Object... inputs)
    {
        addRecipe(new ShapelessOreRecipe(SPY_GROUP, output, inputs));
    }

    private static void addShapedOreRecipe(ItemStack output, Object... inputs)
    {
        addRecipe(new ShapedOreRecipe(SPY_GROUP, output, inputs));
    }

    private static void addShapelessNBTRecipe(ItemStack output, Object...inputs)
    {
        addRecipe(new ShapelessNBTRecipe(SPY_GROUP, output, inputs));
    }

    private static void addRecipe(IRecipe recipe)
    {
        ForgeRegistries.RECIPES.register(recipe);
    }
}

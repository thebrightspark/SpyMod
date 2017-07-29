package brightspark.spymod.recipe;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class ShapelessNBTRecipe extends ShapelessRecipes
{
    public ShapelessNBTRecipe(ResourceLocation group, @Nonnull ItemStack output, Object... inputs)
    {
        super(group.toString(), output, parseInputList(inputs));
    }

    private static NonNullList<Ingredient> parseInputList(Object[] inputList)
    {
        NonNullList<Ingredient> inputs = NonNullList.withSize(inputList.length, Ingredient.EMPTY);

        for(Object object : inputList)
        {
            Ingredient ingredient = CraftingHelper.getIngredient(object);
            if(ingredient != null)
                inputs.add(ingredient);
            else
            {
                String ret = "Invalid shapeless nbt recipe: ";
                for (Object tmp :  inputList)
                    ret += tmp + ", ";
                ret += inputList;
                throw new RuntimeException(ret);
            }
        }

        return inputs;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        List<Ingredient> list = Lists.newArrayList(recipeItems);

        for(int i = 0; i < inv.getHeight(); ++i)
            for(int j = 0; j < inv.getWidth(); ++j)
            {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if(!itemstack.isEmpty())
                {
                    boolean flag = false;

                    for(Ingredient ingredient : list)
                        if(ingredient.test(itemstack))
                        {
                            flag = true;
                            list.remove(ingredient);
                            break;
                        }

                    if(!flag)
                        return false;
                }
            }

        return list.isEmpty();
    }
}

package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireOrderedSpecificRecipe extends AbstractCookingFireRecipe{

    protected CookingFireOrderedSpecificRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient... input) {
        super(type, id, group, output, cookTime, input);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        for(int i=0; i < inv.size(); i++) {
            Ingredient ingredient = (i < input.length)? input[i] : null;
            ItemStack itemStack = inv.getStack(i);

            // if the recipe doesn't have 5 ingredients
            if (ingredient == null) {
                if (itemStack.isEmpty() || itemStack == null) {
                    continue;
                }else{
                    return false;
                }
            }

            if (!ingredient.test(itemStack)) {
                return false;
            }
        }

        return true;
    }
}

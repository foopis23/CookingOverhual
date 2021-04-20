package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import java.util.Arrays;
import java.util.List;

public class CookingFireSpecificRecipe extends AbstractCookingFireRecipe {
    protected CookingFireSpecificRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient... input) {
        super(type, id, group, output, cookTime, input);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        List<Ingredient> ingredientList = Arrays.asList(input);

        // Foreach campfire inventory slot
        for (int i=0; i < inv.size(); i++) {
            ItemStack itemStack = inv.getStack(i);

            // if too many inputs for this recipe
            if (ingredientList.size() == 0 && !itemStack.isEmpty()) {
                return false;
            }


            // foreach ingredient in the recipe
            boolean foundItem = false;
            for (int j=0; j < ingredientList.size(); j++) {
                Ingredient ingredient = ingredientList.get(j);

                // found match in one ingredient
                if (ingredient.test(itemStack)) {
                    ingredientList.remove(i);
                    foundItem = true;
                    break;
                }
            }

            // if the current slot doesn't match any of the ingredients left in the recipe
            if (!foundItem)
                return false;
        }

        return true;
    }
}

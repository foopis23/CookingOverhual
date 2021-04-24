package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CookingFireLooseRecipe extends AbstractCookingFireRecipe {
    protected CookingFireLooseRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient... input) {
        super(type, id, group, output, cookTime, input);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        List<Ingredient> unused = Arrays.asList(input);
        List<Ingredient> used = new LinkedList<>();

        for(int i=0; i < inv.size(); i++) {
            ItemStack itemStack = inv.getStack(i);

            boolean ingredientFound = false;
            for (Ingredient ingredient : unused) {
                if (ingredient.test(itemStack)) {
                    unused.remove(ingredient);
                    used.add(ingredient);
                    ingredientFound = true;
                    break;
                }
            }

            if (!ingredientFound) {
                for(Ingredient ingredient : used) {
                    if (ingredient.test(itemStack)) {
                        ingredientFound = true;
                        break;
                    }
                }

                if (!ingredientFound) {
                    return false;
                }
            }
        }

        return used.size() == 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}

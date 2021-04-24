package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireLooseRatioRecipe extends AbstractCookingFireRecipe{
    protected float[] ratiosMin;
    protected float[] ratiosMax;

    protected CookingFireLooseRatioRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient[] input, float[] ratiosMin, float[] ratiosMax) {
        super(type, id, group, output, cookTime, input);
        this.ratiosMin = ratiosMin;
        this.ratiosMax = ratiosMax;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        int inputTotal = 0;
        int[] ingredientCounts = new int[input.length];
        int nonIngredientCount = 0;

        for(int i=0; i <inv.size(); i++) {
            ItemStack item = inv.getStack(i);

            boolean foundIngredient = false;
            for (int j=0; j < input.length; j++) {
                Ingredient ingredient = input[j];

                if (ingredient.test(item)) {
                    ingredientCounts[j]++;
                    foundIngredient = true;
                    break;
                }
            }

            if (!foundIngredient) {
                nonIngredientCount++;
            }

            inputTotal++;
        }

        // if the non ingredient count is the majority of the recipe, its not the recipe
        if (nonIngredientCount >= ((float)inputTotal / 2)) return false;

        for (int j=0; j<input.length; j++) {
            // check if ratio is in between two values
            float ratio = ((float) ingredientCounts[j] / (float) inputTotal);
            if (ratio < ratiosMin[j] || ratio > ratiosMax[j]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}

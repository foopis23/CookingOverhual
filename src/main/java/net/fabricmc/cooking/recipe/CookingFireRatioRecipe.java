package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireRatioRecipe extends AbstractCookingFireRecipe {
    private final float[] inputRatios;

    public CookingFireRatioRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient[] input, float[] ratios) {
        super(type, id, group, output, cookTime, input);
        inputRatios = ratios;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        int inputTotal = 0;
        int[] ingredientCounts = new int[input.length];

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
                return false;
            }

            inputTotal++;
        }

        for (int j=0; j<input.length; j++) {
            // check if ratio is correct (within floating point error)
            if (!(((float) ingredientCounts[j] / (float) inputTotal) - inputRatios[j] < 0.001)) {
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

package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireRatioRecipe extends AbstractCookingFireRecipe {
    public final float[] InputRatio;

    public CookingFireRatioRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD, Ingredient inputE, ItemStack output, int cookTime, Identifier id, float[] inputRatio) {
        super(inputA, inputB, inputC, inputD, inputE, output, cookTime, id);
        InputRatio = inputRatio;
    }

    public CookingFireRatioRecipe(Ingredient[] inputs, ItemStack output, int cookTime, Identifier id, float[] inputRatio) {
        super(inputs, output, cookTime, id);
        InputRatio = inputRatio;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        if (inv.isEmpty()) return  false;
        int[] inputCount = new int[inputs.length];
        int totalCount = 0;

        for (int i=0; i <inv.size(); i++) {
            ItemStack item = inv.getStack(i);
            if (item.isEmpty()) continue;

            totalCount++;

            boolean ingredientFound = false;
            for (int k=0; k < inputs.length; k++) {
                if (inputs[k].test(item)) {
                    inputCount[k]++;
                    ingredientFound = true;
                    break;
                }
            }

            if (!ingredientFound) {
                return false;
            }
        }

        for (int k=0; k<inputCount.length; k++) {
            float ratio = (float)inputCount[k] / (float)totalCount;

            // check if ratio is correct within floating point error
            if (Math.abs(ratio - InputRatio[k]) > 0.01) {
                return false;
            }
        }

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}

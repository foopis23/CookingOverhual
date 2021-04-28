package net.fabricmc.cooking.recipe;

import net.fabricmc.cooking.recipe.serializer.CookingFireRatioSerializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireRatioRecipe extends AbstractCookingFireRecipe {

    public static class Type implements RecipeType<AbstractCookingFireRecipe> {
        // Define ExampleRecipe.Type as a singleton by making its constructor private and exposing an instance.
        private Type() {}
        public static final CookingFireRatioRecipe.Type INSTANCE = new CookingFireRatioRecipe.Type();

        // This will be needed in step 4
        public static final String ID = "cooking_fire_ratio";
    }

    protected final float[] InputRatio;

    public CookingFireRatioRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD, Ingredient inputE, ItemStack output, int cookTime, Identifier id, float[] inputRatio) {
        super(inputA, inputB, inputC, inputD, inputE, output, cookTime, id);
        InputRatio = inputRatio;
    }

    public CookingFireRatioRecipe(Ingredient[] inputs, ItemStack output, int cookTime, Identifier id, float[] inputRatio) {
        super(inputs, output, cookTime, id);
        InputRatio = inputRatio;
    }

    public float getInputRatio(int i) {
        if (i < 0 || i > InputRatio.length) return -1;

        return InputRatio[i];
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
        return CookingFireRatioSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
    return Type.INSTANCE;
    }
}

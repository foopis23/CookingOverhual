package net.fabricmc.cooking.recipe;

import net.fabricmc.cooking.recipe.serializer.CookingFireLooseRatioSerializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireLooseRatioRecipe extends AbstractCookingFireRecipe {

    public static class Type implements RecipeType<AbstractCookingFireRecipe> {
        private Type() {}
        public static final CookingFireLooseRatioRecipe.Type INSTANCE = new CookingFireLooseRatioRecipe.Type();
        public static final String ID = "cooking_fire_loose_ratio";
    }

    protected final float MinInputRatio[];
    protected final float MaxInputRatio[];
    protected final float MaxErrorRatio;

    public CookingFireLooseRatioRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD,
                                       Ingredient inputE, ItemStack output, int cookTime, Identifier id,
                                       float[] MinInputRatio, float[] MaxInputRatio, float MaxErrorRatio) {
        super(inputA, inputB, inputC, inputD, inputE, output, cookTime, id);

        this.MinInputRatio = MinInputRatio;
        this.MaxInputRatio = MaxInputRatio;
        this.MaxErrorRatio = MaxErrorRatio;
    }

    public CookingFireLooseRatioRecipe(Ingredient[] inputs, ItemStack output, int cookTime, Identifier id,
                                       float[] MinInputRatio, float[] MaxInputRatio, float MaxErrorRatio) {
        super(inputs, output, cookTime, id);

        this.MinInputRatio = MinInputRatio;
        this.MaxInputRatio = MaxInputRatio;
        this.MaxErrorRatio = MaxErrorRatio;
    }

    public float getMinRatio(int i) {
        return MinInputRatio[i];
    }

    public float getMaxRatio(int i) {
        return MaxInputRatio[i];
    }

    public float getMaxErrorRatio() {
        return MaxErrorRatio;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        if (inv.isEmpty()) return  false;
        int[] inputCount = new int[inputs.length];
        int nonIngredientCount = 0, totalCount = 0;

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
                nonIngredientCount++;
            }
        }

        for (int k=0; k<inputCount.length; k++) {
            float ratio = (float)inputCount[k] / (float)totalCount;
            if (ratio < MinInputRatio[k] || ratio > MaxInputRatio[k]) {
                return false;
            }
        }

        return !(((float) nonIngredientCount / (float) totalCount) > MaxErrorRatio);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookingFireLooseRatioSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}

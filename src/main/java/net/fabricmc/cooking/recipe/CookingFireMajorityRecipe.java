package net.fabricmc.cooking.recipe;

import net.fabricmc.cooking.recipe.serializer.CookingFireMajoritySerializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireMajorityRecipe extends AbstractCookingFireRecipe {

    public static class Type implements RecipeType<AbstractCookingFireRecipe> {
        // Define ExampleRecipe.Type as a singleton by making its constructor private and exposing an instance.
        private Type() {}
        public static final Type INSTANCE = new Type();

        // This will be needed in step 4
        public static final String ID = "cooking_fire_majority";
    }

    public CookingFireMajorityRecipe(Ingredient inputA, ItemStack output, int cookTime, Identifier id) {
        super(inputA, output, cookTime, id);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        int ingredientCount = 0;
        int nonIngredientCount = 0;

        for(int i=0; i <inv.size(); i++) {
            ItemStack item = inv.getStack(i);
            Ingredient ingredient = inputA;

            if (ingredient.test(item)) {
                ingredientCount++;
            }else {
                if (!item.isEmpty())
                    nonIngredientCount++;
            }
        }

        return ingredientCount > nonIngredientCount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookingFireMajoritySerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}

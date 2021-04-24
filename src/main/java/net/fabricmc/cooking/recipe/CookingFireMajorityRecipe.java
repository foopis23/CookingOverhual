package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireMajorityRecipe extends AbstractCookingFireRecipe {
    protected CookingFireMajorityRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient input) {
        super(type, id, group, output, cookTime, new Ingredient[]{input});
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        int ingredientCount = 0;
        int nonIngredientCount = 0;

        for(int i=0; i <inv.size(); i++) {
            ItemStack item = inv.getStack(i);
            Ingredient ingredient = input[0];

            if (ingredient.test(item)) {
                ingredientCount++;
            }else {
                nonIngredientCount++;
            }
        }

        return ingredientCount > nonIngredientCount;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}

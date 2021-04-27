package net.fabricmc.cooking.recipe.manager;

import net.fabricmc.cooking.recipe.AbstractCookingFireRecipe;
import net.fabricmc.cooking.recipe.CookingFireMajorityRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.world.World;

import java.util.Optional;

public class CookingFireRecipeManager {
    public static AbstractCookingFireRecipe getFirstMatch(Inventory inventory, World world) {

        // Cooking Fire Majority Recipe
        Optional<CookingFireMajorityRecipe> cookingFireMajorityRecipeMatch;
        cookingFireMajorityRecipeMatch = world.getRecipeManager().getFirstMatch(CookingFireMajorityRecipe.Type.INSTANCE, inventory, world);
        if (cookingFireMajorityRecipeMatch.isPresent()) {
            return cookingFireMajorityRecipeMatch.get();
        }

        return null;
    }
}

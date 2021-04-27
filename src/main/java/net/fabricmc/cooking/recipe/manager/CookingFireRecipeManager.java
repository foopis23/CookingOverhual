package net.fabricmc.cooking.recipe.manager;

import net.fabricmc.cooking.recipe.AbstractCookingFireRecipe;
import net.fabricmc.cooking.recipe.CookingFireLooseRecipe;
import net.fabricmc.cooking.recipe.CookingFireMajorityRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;

import java.util.Optional;

public class CookingFireRecipeManager {
    public static AbstractCookingFireRecipe getFirstMatch(Inventory inventory, World world) {
        Optional<AbstractCookingFireRecipe> match;

        // Loose Recipe
        match = world.getRecipeManager().getFirstMatch(CookingFireLooseRecipe.Type.INSTANCE, inventory, world);
        if (match.isPresent()) {
            return match.get();
        }

        // Cooking Fire Majority Recipe
        match = world.getRecipeManager().getFirstMatch(CookingFireMajorityRecipe.Type.INSTANCE, inventory, world);
        if (match.isPresent()) {
            return match.get();
        }

        return null;
    }
}

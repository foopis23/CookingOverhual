package net.fabricmc.cooking.recipe.manager;

import net.fabricmc.cooking.recipe.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;

import java.util.Optional;

public class CookingFireRecipeManager {
    public static AbstractCookingFireRecipe getFirstMatch(Inventory inventory, World world) {
        Optional<AbstractCookingFireRecipe> match;

        // Ratio Recipe
        match = world.getRecipeManager().getFirstMatch(CookingFireRatioRecipe.Type.INSTANCE, inventory, world);
        if (match.isPresent()) {
            return match.get();
        }

        // Loose Ratio Recipe
        match = world.getRecipeManager().getFirstMatch(CookingFireLooseRatioRecipe.Type.INSTANCE, inventory, world);
        if (match.isPresent()) {
            return match.get();
        }

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

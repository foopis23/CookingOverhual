package net.fabricmc.cooking.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireLooseRatioRecipe extends AbstractCookingFireRecipe{

    protected CookingFireLooseRatioRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient... input) {
        super(type, id, group, output, cookTime, input);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return false;
    }
}

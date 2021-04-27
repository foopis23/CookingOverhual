package net.fabricmc.cooking.recipe;

import net.fabricmc.cooking.recipe.serializer.CookingFireLooseSerializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CookingFireLooseRecipe extends AbstractCookingFireRecipe {

    public static class Type implements RecipeType<AbstractCookingFireRecipe> {
        // Define ExampleRecipe.Type as a singleton by making its constructor private and exposing an instance.
        private Type() {}
        public static final CookingFireLooseRecipe.Type INSTANCE = new CookingFireLooseRecipe.Type();

        // This will be needed in step 4
        public static final String ID = "cooking_fire_loose";
    }

    public CookingFireLooseRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD, Ingredient inputE, ItemStack output, int cookTime, Identifier id) {
        super(inputA, inputB, inputC, inputD, inputE, output, cookTime, id);
    }

    public CookingFireLooseRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD, ItemStack output, int cookTime, Identifier id) {
        super(inputA, inputB, inputC, inputD, output, cookTime, id);
    }

    public CookingFireLooseRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, ItemStack output, int cookTime, Identifier id) {
        super(inputA, inputB, inputC, output, cookTime, id);
    }

    public CookingFireLooseRecipe(Ingredient inputA, Ingredient inputB, ItemStack output, int cookTime, Identifier id) {
        super(inputA, inputB, output, cookTime, id);
    }

    public CookingFireLooseRecipe(Ingredient inputA, ItemStack output, int cookTime, Identifier id) {
        super(inputA, output, cookTime, id);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        int ingredientCount = 0;

        // Loop through inventory
        for (int i = 0; i < inv.size(); i++) {
            if (inv.getStack(i).isEmpty())
                break;

            ingredientCount++;

            if (inputA.test(inv.getStack(i)))
                continue;

            if (inputB != null && inputB.test(inv.getStack(i)))
                continue;

            if (inputC != null && inputC.test(inv.getStack(i)))
                continue;

            if (inputD != null && inputD.test(inv.getStack(i)))
                continue;

            if (inputE != null && inputE.test(inv.getStack(i)))
                continue;

            return false;
        }

        return ingredientCount > 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CookingFireLooseSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}

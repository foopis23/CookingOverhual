package net.fabricmc.cooking.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class AbstractCookingFireRecipe implements Recipe<Inventory> {
    protected final Ingredient[] input;
    protected final RecipeType<?> type;
    protected final Identifier id;
    protected final String group;
    protected final ItemStack output;
    protected final int cookTime;

    protected AbstractCookingFireRecipe(RecipeType<?> type, Identifier id, String group, ItemStack output, int cookTime, Ingredient... input) {
        this.input = input;
        this.type = type;
        this.id = id;
        this.group = group;
        this.output = output;
        this.cookTime = cookTime;
    }

    @Override
    public abstract boolean matches(Inventory inv, World world);

    @Override
    public ItemStack craft(Inventory inv) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) { return true; }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Environment(EnvType.CLIENT)
    public String getGroup() {
        return this.group;
    }
}

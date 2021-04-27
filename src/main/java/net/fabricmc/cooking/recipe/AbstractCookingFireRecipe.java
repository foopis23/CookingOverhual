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
    protected final Ingredient inputA;
    protected final Ingredient inputB;
    protected final Ingredient inputC;
    protected final Ingredient inputD;
    protected final Ingredient inputE;
    protected final Ingredient[] inputs;
    protected final ItemStack output;
    protected final int cookTime;
    protected final Identifier id;

    protected AbstractCookingFireRecipe(Ingredient inputA, ItemStack output, int cookTime, Identifier id) {
        this.inputA = inputA;
        this.inputB = null;
        this.inputC = null;
        this.inputD = null;
        this.inputE = null;
        inputs = new Ingredient[]{inputA};
        this.output = output;
        this.cookTime = cookTime;
        this.id = id;
    }

    protected AbstractCookingFireRecipe(Ingredient inputA, Ingredient inputB, ItemStack output, int cookTime, Identifier id) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.inputC = null;
        this.inputD = null;
        this.inputE = null;
        inputs = new Ingredient[]{inputA, inputB};
        this.output = output;
        this.cookTime = cookTime;
        this.id = id;
    }

    protected AbstractCookingFireRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, ItemStack output, int cookTime, Identifier id) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.inputC = inputC;
        this.inputD = null;
        this.inputE = null;
        inputs = new Ingredient[]{inputA, inputB, inputC};
        this.output = output;
        this.cookTime = cookTime;
        this.id = id;
    }

    protected AbstractCookingFireRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD, ItemStack output, int cookTime, Identifier id) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.inputC = inputC;
        this.inputD = inputD;
        this.inputE = null;
        inputs = new Ingredient[]{inputA, inputB, inputC, inputD};
        this.output = output;
        this.cookTime = cookTime;
        this.id = id;
    }

    protected AbstractCookingFireRecipe(Ingredient inputA, Ingredient inputB, Ingredient inputC, Ingredient inputD, Ingredient inputE, ItemStack output, int cookTime, Identifier id) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.inputC = inputC;
        this.inputD = inputD;
        this.inputE = inputE;
        inputs = new Ingredient[]{inputA, inputB, inputC, inputD, inputE};
        this.output = output;
        this.cookTime = cookTime;
        this.id = id;
    }

    protected AbstractCookingFireRecipe(Ingredient[] inputs, ItemStack output, int cookTime, Identifier id) {
        inputA = (inputs.length > 0)? inputs[0] : null;
        inputB = (inputs.length > 1)? inputs[1] : null;
        inputC = (inputs.length > 2)? inputs[2] : null;
        inputD = (inputs.length > 3)? inputs[3] : null;
        inputE = (inputs.length > 4)? inputs[4] : null;

        this.inputs = inputs;
        this.output = output;
        this.cookTime = cookTime;
        this.id = id;
    }

    public Ingredient getInputA() {
        return inputA;
    }

    public Ingredient getInputB() {
        return inputB;
    }

    public Ingredient getInputC() {
        return inputC;
    }

    public Ingredient getInputD() {
        return inputD;
    }

    public Ingredient getInputE() {
        return inputE;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public abstract boolean matches(Inventory inv, World world);

    @Override
    public ItemStack craft(Inventory inv) {
        inv.clear();
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
    public abstract RecipeType<?> getType();

    @Environment(EnvType.CLIENT)
    public String getGroup() {
        return null;
    }
}

package net.fabricmc.cooking.recipe.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.cooking.recipe.CookingFireMajorityRecipe;
import net.fabricmc.cooking.recipe.json.CookingFireMajorityJsonFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CookingFireMajoritySerializer implements RecipeSerializer<CookingFireMajorityRecipe> {
    public static final CookingFireMajoritySerializer INSTANCE = new CookingFireMajoritySerializer();
    public static final Identifier ID = new Identifier("cooking-overhaul:recipe_majority");

    @Override
    public CookingFireMajorityRecipe read(Identifier id, JsonObject json) {
        // Convert Json To Java Class Instance
        CookingFireMajorityJsonFormat recipeJson = new Gson().fromJson(json, CookingFireMajorityJsonFormat.class);

        // if json is stupid
        if (recipeJson.input == null || recipeJson.output == null)
            throw new JsonSyntaxException("A require attribute is missing!");

        // Default value most be greater than zero
        if (recipeJson.cookingTime < 1)
            recipeJson.cookingTime = 1;

        // Get output item from id, if no item exists throw error
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.output)).orElseThrow(() ->
                new JsonSyntaxException("No such item " + recipeJson.output));

        // create input and output from json
        Ingredient input = Ingredient.fromJson((recipeJson.input));
        ItemStack output = new ItemStack(outputItem, 1);

        // Create Recipe
        return new CookingFireMajorityRecipe(input, output, recipeJson.cookingTime, id);
    }

    @Override
    public CookingFireMajorityRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient input = Ingredient.fromPacket(buf);
        ItemStack output = buf.readItemStack();
        int cookingTime = buf.readInt();

        return new CookingFireMajorityRecipe(input, output, cookingTime, id);
    }

    @Override
    public void write(PacketByteBuf buf, CookingFireMajorityRecipe recipe) {
        recipe.getInputA().write(buf);
        buf.writeItemStack(recipe.getOutput());
        buf.writeInt(recipe.getCookTime());
    }
}

package net.fabricmc.cooking.recipe.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.cooking.recipe.CookingFireRatioRecipe;
import net.fabricmc.cooking.recipe.json.CookingFireLooseRatioJsonFormat;
import net.fabricmc.cooking.recipe.json.CookingFireRatioJsonFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class CookingFireRatioSerializer implements RecipeSerializer<CookingFireRatioRecipe> {

    @Override
    public CookingFireRatioRecipe read(Identifier id, JsonObject json) {
        CookingFireRatioJsonFormat recipeJson = new Gson().fromJson(json, CookingFireRatioJsonFormat.class);

        int inputSize = recipeJson.input.size();
        int ratioSize = recipeJson.ratio.size();
        if (inputSize <= 0 || inputSize > 5) throw new JsonSyntaxException("Input Array Needs To Be 1-5 Elements in Length");
        if (ratioSize <= 0 || ratioSize > 5) throw new JsonSyntaxException("Ratio Array Needs To Be 1-5 Elements in Length");
        if (ratioSize != inputSize) throw  new JsonSyntaxException("Input Array and Ratio Array New To Be Same Size");

        Ingredient[] inputs = new Ingredient[inputSize];
        float[] ratios = new float[ratioSize];

        for (int i=0; i < inputSize; i++) {
            inputs[i] = Ingredient.fromJson(recipeJson.input.get(i));
            ratios[i] = recipeJson.ratio.get(i).getAsFloat();
        }

        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.output)).orElseThrow(() ->
                new JsonSyntaxException("No such item " + recipeJson.output));
        ItemStack outputItemStack = new ItemStack(outputItem, 1);
        int cookTime = (recipeJson.cookTime > 0)? recipeJson.cookTime : 1;

        return new CookingFireRatioRecipe(inputs, outputItemStack, cookTime, id, ratios);
    }

    @Override
    public CookingFireRatioRecipe read(Identifier id, PacketByteBuf buf) {
        return null;
    }

    @Override
    public void write(PacketByteBuf buf, CookingFireRatioRecipe recipe) {

    }
}

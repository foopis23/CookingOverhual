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
    public static final CookingFireRatioSerializer INSTANCE = new CookingFireRatioSerializer();
    public static final Identifier ID = new Identifier("cooking-overhaul:recipe_ratio");

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
        int cookTime = buf.readInt();
        ItemStack output = buf.readItemStack();

        float[] ratio = new float[5];

        ratio[0] = buf.readFloat();
        ratio[1] = buf.readFloat();
        ratio[2] = buf.readFloat();
        ratio[3] = buf.readFloat();
        ratio[4] = buf.readFloat();

        Ingredient a = Ingredient.fromPacket(buf);
        Ingredient b = Ingredient.fromPacket(buf);
        Ingredient c = Ingredient.fromPacket(buf);
        Ingredient d = Ingredient.fromPacket(buf);
        Ingredient e = Ingredient.fromPacket(buf);

        return new CookingFireRatioRecipe(a, b, c, d, e, output, cookTime, id, ratio);
    }

    @Override
    public void write(PacketByteBuf buf, CookingFireRatioRecipe recipe) {
        buf.writeInt(recipe.getCookTime());
        buf.writeItemStack(recipe.getOutput());

        buf.writeFloat(recipe.getInputRatio(0));
        buf.writeFloat(recipe.getInputRatio(1));
        buf.writeFloat(recipe.getInputRatio(2));
        buf.writeFloat(recipe.getInputRatio(3));
        buf.writeFloat(recipe.getInputRatio(4));

        if (recipe.getInputA() != null)
            recipe.getInputA().write(buf);

        if (recipe.getInputB() != null)
            recipe.getInputB().write(buf);

        if (recipe.getInputC() != null)
            recipe.getInputC().write(buf);

        if (recipe.getInputD() != null)
            recipe.getInputD().write(buf);

        if (recipe.getInputE() != null)
            recipe.getInputE().write(buf);
    }
}

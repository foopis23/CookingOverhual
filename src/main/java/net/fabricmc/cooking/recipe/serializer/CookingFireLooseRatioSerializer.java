package net.fabricmc.cooking.recipe.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.cooking.recipe.CookingFireLooseRatioRecipe;
import net.fabricmc.cooking.recipe.json.CookingFireLooseRatioJsonFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class CookingFireLooseRatioSerializer implements RecipeSerializer<CookingFireLooseRatioRecipe> {
    public static final CookingFireLooseRatioSerializer INSTANCE = new CookingFireLooseRatioSerializer();
    public static final Identifier ID = new Identifier("cooking-overhaul:recipe_loose_ratio");

    @Override
    public CookingFireLooseRatioRecipe read(Identifier id, JsonObject json) {
        CookingFireLooseRatioJsonFormat recipeJson = new Gson().fromJson(json, CookingFireLooseRatioJsonFormat.class);

        int inputSize = recipeJson.input.size();
        int ratioSize = recipeJson.ratio.size();
        if (inputSize <= 0 || inputSize > 5) throw new JsonSyntaxException("Input Array Needs To Be 1-5 Elements in Length");
        if (ratioSize <= 0 || ratioSize > 5) throw new JsonSyntaxException("Ratio Array Needs To Be 1-5 Elements in Length");
        if (ratioSize != inputSize) throw  new JsonSyntaxException("Input Array and Ratio Array New To Be Same Size");

        Ingredient[] inputs = new Ingredient[inputSize];
        float[] minRatios = new float[ratioSize];
        float[] maxRatios = new float[ratioSize];

        for (int i=0; i < inputSize; i++) {
            inputs[i] = Ingredient.fromJson(recipeJson.input.get(i));
            minRatios[i] = JsonHelper.getFloat(recipeJson.ratio.get(i).getAsJsonObject(), "min");
            maxRatios[i] = JsonHelper.getFloat(recipeJson.ratio.get(i).getAsJsonObject(), "max");
        }

        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.output)).orElseThrow(() ->
                new JsonSyntaxException("No such item " + recipeJson.output));
        ItemStack outputItemStack = new ItemStack(outputItem, 1);
        int cookTime = (recipeJson.cookTime > 0)? recipeJson.cookTime : 1;
        float maxErrorRatio = recipeJson.maxErrorRatio;

        return new CookingFireLooseRatioRecipe(inputs, outputItemStack, cookTime, id, minRatios, maxRatios, maxErrorRatio);
    }

    @Override
    public CookingFireLooseRatioRecipe read(Identifier id, PacketByteBuf buf) {
        int cookTime = buf.readInt();
        ItemStack output = buf.readItemStack();
        float maxErrorRatio = buf.readFloat();

        float[] minRatio = new float[5];
        float[] maxRatio = new float[5];

        minRatio[0] = buf.readFloat();
        minRatio[1] = buf.readFloat();
        minRatio[2] = buf.readFloat();
        minRatio[3] = buf.readFloat();
        minRatio[4] = buf.readFloat();

        maxRatio[0] = buf.readFloat();
        maxRatio[1] = buf.readFloat();
        maxRatio[2] = buf.readFloat();
        maxRatio[3] = buf.readFloat();
        maxRatio[4] = buf.readFloat();

        Ingredient a = Ingredient.fromPacket(buf);
        Ingredient b = Ingredient.fromPacket(buf);
        Ingredient c = Ingredient.fromPacket(buf);
        Ingredient d = Ingredient.fromPacket(buf);
        Ingredient e = Ingredient.fromPacket(buf);

        return new CookingFireLooseRatioRecipe(a, b, c, d, e, output, cookTime, id, minRatio, maxRatio, maxErrorRatio);
    }

    @Override
    public void write(PacketByteBuf buf, CookingFireLooseRatioRecipe recipe) {
        buf.writeInt(recipe.getCookTime());
        buf.writeItemStack(recipe.getOutput());
        buf.writeFloat(recipe.getMaxErrorRatio());

        buf.writeFloat(recipe.getMinRatio(0));
        buf.writeFloat(recipe.getMinRatio(1));
        buf.writeFloat(recipe.getMinRatio(2));
        buf.writeFloat(recipe.getMinRatio(3));
        buf.writeFloat(recipe.getMinRatio(4));

        buf.writeFloat(recipe.getMaxRatio(0));
        buf.writeFloat(recipe.getMaxRatio(1));
        buf.writeFloat(recipe.getMaxRatio(2));
        buf.writeFloat(recipe.getMaxRatio(3));
        buf.writeFloat(recipe.getMaxRatio(4));

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

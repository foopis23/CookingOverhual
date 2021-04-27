package net.fabricmc.cooking.recipe.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.cooking.recipe.CookingFireLooseRecipe;
import net.fabricmc.cooking.recipe.json.CookingFireLooseJsonFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CookingFireLooseSerializer implements RecipeSerializer<CookingFireLooseRecipe> {
    public static final CookingFireLooseSerializer INSTANCE = new CookingFireLooseSerializer();
    public static final Identifier ID = new Identifier("cooking-overhaul:recipe_loose");

    @Override
    public CookingFireLooseRecipe read(Identifier id, JsonObject json) {
        CookingFireLooseJsonFormat recipe = new Gson().fromJson(json, CookingFireLooseJsonFormat.class);
        if (recipe.input.size() <= 0) throw new JsonSyntaxException("Input Array Needs At Least One Element");

        Ingredient inputA = null;
        Ingredient inputB = null;
        Ingredient inputC = null;
        Ingredient inputD = null;
        Ingredient inputE = null;

        for(int i=0; i<recipe.input.size(); i++) {
            switch (i) {
                case 0:
                    inputA = Ingredient.fromJson(recipe.input.get(i));
                    break;
                case 1:
                    inputB = Ingredient.fromJson(recipe.input.get(i));
                    break;
                case 2:
                    inputC = Ingredient.fromJson(recipe.input.get(i));
                    break;
                case 3:
                    inputD = Ingredient.fromJson(recipe.input.get(i));
                    break;
                case 4:
                    inputE = Ingredient.fromJson(recipe.input.get(i));
                    break;
                default:
                    throw new JsonSyntaxException("Input Array Can Only Be 5 Elements Long");
            }
        }

        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipe.output)).orElseThrow(() ->
                new JsonSyntaxException("No such item " + recipe.output));
        ItemStack outputItemStack = new ItemStack(outputItem, 1);

        int cookTime = (recipe.cookTime > 0)? recipe.cookTime : 1;

        return new CookingFireLooseRecipe(inputA, inputB, inputC, inputD, inputE, outputItemStack, cookTime, id);
    }

    @Override
    public CookingFireLooseRecipe read(Identifier id, PacketByteBuf buf) {
        ItemStack output = buf.readItemStack();
        int cookingTime = buf.readInt();

        Ingredient inputA = Ingredient.fromPacket(buf);
        Ingredient inputB = Ingredient.fromPacket(buf);
        Ingredient inputC = Ingredient.fromPacket(buf);
        Ingredient inputD = Ingredient.fromPacket(buf);
        Ingredient inputE = Ingredient.fromPacket(buf);

        return new CookingFireLooseRecipe(inputA, inputB, inputC, inputD, inputE, output, cookingTime, id);
    }

    @Override
    public void write(PacketByteBuf buf, CookingFireLooseRecipe recipe) {
        buf.writeItemStack(recipe.getOutput());
        buf.writeInt(recipe.getCookTime());
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

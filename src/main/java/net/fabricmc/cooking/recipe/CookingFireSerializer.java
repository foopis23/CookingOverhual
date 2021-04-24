package net.fabricmc.cooking.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CookingFireSerializer<T extends AbstractCookingFireRecipe> implements RecipeSerializer<T> {
    @Override
    public T read(Identifier id, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        String type = JsonHelper.getString(json, "type", "");
        JsonObject ingredientObjects = JsonHelper.getObject(json, "ingredients");
        JsonObject ratiosObject = JsonHelper.getObject(json, "ratios", null);

        List<Ingredient> ingredients = new LinkedList<>();
        List<float[]> ratios = new LinkedList<>();

        for (Map.Entry<String, JsonElement> entry : ingredientObjects.entrySet()) {
            String key = entry.getKey();
            JsonElement object = entry.getValue();
            ingredients.add(Ingredient.fromJson(object));

            if (ratiosObject != null && JsonHelper.hasElement(ratiosObject, key)) {
                JsonObject ratioObject = JsonHelper.getObject(ratiosObject, key);
                switch (type) {
                    case "cooking_fire_ratio":
                        ratios.add(new float[]{JsonHelper.getFloat(ratioObject, "ratio")});
                        break;
                    case "cooking_fire_loose_ratio":
                        ratios.add(new float[]{
                                JsonHelper.getFloat(ratioObject, "minRatio"),
                                JsonHelper.getFloat(ratioObject, "maxRatio")
                        });
                        break;
                }
            }
        }

        return null;
    }

    @Override
    public T read(Identifier id, PacketByteBuf buf) {
        return null;
    }

    @Override
    public void write(PacketByteBuf buf, T recipe) {

    }
}

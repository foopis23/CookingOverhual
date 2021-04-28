package net.fabricmc.cooking.recipe.json;

import com.google.gson.JsonArray;

public class CookingFireLooseRatioJsonFormat extends CookingFireAbstractJsonFormat{
    public JsonArray input;
    public JsonArray ratio;
    public float maxErrorRatio;
}

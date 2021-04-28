package net.fabricmc.cooking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.cooking.block.CookingFire;
import net.fabricmc.cooking.inventory.CookingFireInventory;
import net.fabricmc.cooking.recipe.CookingFireLooseRatioRecipe;
import net.fabricmc.cooking.recipe.CookingFireLooseRecipe;
import net.fabricmc.cooking.recipe.CookingFireMajorityRecipe;
import net.fabricmc.cooking.recipe.CookingFireRatioRecipe;
import net.fabricmc.cooking.recipe.serializer.CookingFireLooseRatioSerializer;
import net.fabricmc.cooking.recipe.serializer.CookingFireLooseSerializer;
import net.fabricmc.cooking.recipe.serializer.CookingFireMajoritySerializer;
import net.fabricmc.cooking.recipe.serializer.CookingFireRatioSerializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CookingOverhaul implements ModInitializer {
	// Mod Resources ///////////////////////////////////////////////////////////////////////////////////////////////////
	public static final String MOD_ID = "cooking-overhaul";
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Blocks //////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final FabricBlockSettings COOKING_FIRE_SETTINGS = FabricBlockSettings.of(Material.WOOD).nonOpaque();
	public static final Block COOKING_FIRE = new CookingFire(COOKING_FIRE_SETTINGS);
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Block Entity ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static BlockEntityType<CookingFireInventory> COOKING_FIRE_ENTITY;
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onInitialize() {
		COOKING_FIRE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":cooking_fire",
				BlockEntityType.Builder.create(CookingFireInventory::new, COOKING_FIRE).build(null));

		// Ratio Type
		Registry.register(Registry.RECIPE_SERIALIZER, CookingFireRatioSerializer.ID,
				CookingFireRatioSerializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, CookingFireRatioRecipe.Type.ID),
				CookingFireRatioRecipe.Type.INSTANCE);

		// Loose Ratio Type
		Registry.register(Registry.RECIPE_SERIALIZER, CookingFireLooseRatioSerializer.ID,
				CookingFireLooseRatioSerializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, CookingFireLooseRatioRecipe.Type.ID),
				CookingFireLooseRatioRecipe.Type.INSTANCE);

		// Loose Recipe Type
		Registry.register(Registry.RECIPE_SERIALIZER, CookingFireLooseSerializer.ID,
				CookingFireLooseSerializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, CookingFireLooseRecipe.Type.ID),
				CookingFireLooseRecipe.Type.INSTANCE);

		// Majority Recipe Type
		Registry.register(Registry.RECIPE_SERIALIZER, CookingFireMajoritySerializer.ID,
				CookingFireMajoritySerializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, CookingFireMajorityRecipe.Type.ID),
				CookingFireMajorityRecipe.Type.INSTANCE);

		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "cooking_fire"), COOKING_FIRE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cooking_fire"),
				new BlockItem(COOKING_FIRE, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
	}
}

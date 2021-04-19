package net.fabricmc.cooking;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.cooking.block.CookingFire;
import net.fabricmc.cooking.inventory.CookingFireInventory;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.CampfireBlock;

public class CookingOverhaul implements ModInitializer {
	public static final String MOD_ID = "cooking-overhaul";

	public static final FabricBlockSettings COOKING_FIRE_SETTINGS = FabricBlockSettings.of(Material.WOOD).nonOpaque();
	public static final Block COOKING_FIRE = new CookingFire(COOKING_FIRE_SETTINGS);

	public static BlockEntityType<CookingFireInventory> COOKING_FIRE_ENTITY;

	@Override
	public void onInitialize() {
		COOKING_FIRE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":cooking_fire",
				BlockEntityType.Builder.create(CookingFireInventory::new, COOKING_FIRE).build(null));

		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "cooking_fire"), COOKING_FIRE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cooking_fire"),
				new BlockItem(COOKING_FIRE, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

		System.out.println("Hello Fabric world!");
	}
}

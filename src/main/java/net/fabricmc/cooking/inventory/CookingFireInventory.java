package net.fabricmc.cooking.inventory;

import net.fabricmc.cooking.CookingOverhaul;
import net.fabricmc.cooking.recipe.AbstractCookingFireRecipe;
import net.fabricmc.cooking.recipe.CookingFireMajorityRecipe;
import net.fabricmc.cooking.recipe.manager.CookingFireRecipeManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CookingFireInventory extends BlockEntity implements ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private int cookTime = 0;
    private int currentCookTime = 0;
    private boolean lit = false;

    private AbstractCookingFireRecipe foundRecipe;

    public CookingFireInventory() {
        super(CookingOverhaul.COOKING_FIRE_ENTITY);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, items);
        lit = tag.getBoolean("lit");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, items);
        tag.putBoolean("lit", lit);
        return super.toTag(tag);
    }

    @Override
    public void tick() {
        if (this.world.isClient) return;
        if (!lit) return;
        if (foundRecipe == null) return;

        if (currentCookTime >= cookTime) {
            ItemStack itemStack = foundRecipe.craft(this);
            BlockPos blockPos = this.getPos();
            ItemScatterer.spawn(this.world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
            setLit(false);
        } else if (!isEmpty()) {
            currentCookTime++;
        }
    }

    public void setLit(boolean lit) {
        this.lit = lit;

        if (this.lit) {
            foundRecipe = CookingFireRecipeManager.getFirstMatch(this, this.world);

            if (foundRecipe != null) {
                cookTime = foundRecipe.getCookTime();
                currentCookTime = 0;
            } else {
                BlockPos blockPos = this.getPos();

                for (int i = 0; i < size(); i++) {
                    ItemStack itemStack = getStack(i);

                    if (!itemStack.isEmpty())
                        ItemScatterer.spawn(this.world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
                }
            }
        } else {
            foundRecipe = null;
            cookTime = 0;
            currentCookTime = 0;
        }

        markDirty();
    }
}

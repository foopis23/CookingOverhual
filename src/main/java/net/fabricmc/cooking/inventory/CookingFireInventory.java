package net.fabricmc.cooking.inventory;

import net.fabricmc.cooking.CookingOverhaul;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class CookingFireInventory extends BlockEntity implements ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private final int[] cookTimes = new int[5];
    private final int totalCookTime = 100;
    private boolean lit = false;

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

        cookTimes[slot] = 0;
    }

    @Override
    public ItemStack removeStack(int slot) {
        cookTimes[slot] = 0;
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag,items);
        lit = tag.getBoolean("lit");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag,items);
        tag.putBoolean("lit", lit);
        return super.toTag(tag);
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            if (lit) {
                for (int i=0; i<getItems().size(); i++) {
                    ItemStack stack = getStack(i);
                    Inventory inventory = new SimpleInventory(new ItemStack[]{stack});
                    if (cookTimes[i] >= totalCookTime) {

                        // TODO: Replace this with cooking fire recipe manager
                        ItemStack itemStack2 = (ItemStack)this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, inventory, this.world).map((campfireCookingRecipe) -> {
                            return campfireCookingRecipe.craft(inventory);
                        }).orElse(stack);

                        BlockPos blockPos = this.getPos();
                        ItemScatterer.spawn(this.world, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), itemStack2);
                        setStack(i, ItemStack.EMPTY);
                        cookTimes[i] = 0;
                    }else if (getStack(i) != ItemStack.EMPTY) {
                        cookTimes[i]++;
                    }
                }

                if (isEmpty()) {
                    lit = false;
                    markDirty();
                }
            }
        }
    }

    public void setLit(boolean lit) {
        this.lit = lit;
        markDirty();
    }
}

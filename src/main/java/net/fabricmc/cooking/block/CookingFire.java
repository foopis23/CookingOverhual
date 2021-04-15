package net.fabricmc.cooking.block;

import net.fabricmc.cooking.inventory.CookingFireInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CookingFire extends Block implements BlockEntityProvider {
    public CookingFire(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) return ActionResult.PASS;
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;

        Inventory blockEntity = (Inventory) world.getBlockEntity(pos);
        assert blockEntity != null;

        if (!player.getStackInHand(hand).isEmpty()) {

            for (int i = 0; i < blockEntity.size(); i++) {
                if (blockEntity.getStack(i).isEmpty()) {
                    blockEntity.setStack(i, player.getStackInHand(hand).copy());
                    player.getStackInHand(hand).setCount(0);

                    return ActionResult.SUCCESS;
                }
            }

        }else{

            for (int i = blockEntity.size(); i>0; i--) {
                if (!blockEntity.getStack(i-1).isEmpty()) {
                    player.inventory.offerOrDrop(world, blockEntity.getStack(i-1));
                    blockEntity.removeStack(i-1);
                    return ActionResult.SUCCESS;
                }
            }

        }

        return ActionResult.PASS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory) {
                ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new CookingFireInventory();
    }
}

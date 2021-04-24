package net.fabricmc.cooking.block;

import net.fabricmc.cooking.inventory.CookingFireInventory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CookingFire extends Block implements BlockEntityProvider {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

    public CookingFire(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;

        CookingFireInventory blockEntity = (CookingFireInventory) world.getBlockEntity(pos);
        assert blockEntity != null;

        ItemStack itemStack = player.getStackInHand(hand);

        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() instanceof FlintAndSteelItem) {
                if (!world.isClient) {
                    System.out.println("LIT FIRE");
                    blockEntity.setLit(true);
                }
                return ActionResult.SUCCESS;
            }

            for (int i = 0; i < blockEntity.size(); i++) {
                if (blockEntity.getStack(i).isEmpty()) {
                    if (!world.isClient) {
                        blockEntity.setStack(i, itemStack.copy());
                        itemStack.setCount(0);
                    }
                    return ActionResult.CONSUME;
                }
            }

        }else{
            for (int i = blockEntity.size(); i>0; i--) {
                if (!blockEntity.getStack(i-1).isEmpty()) {
                    if (!world.isClient) {
                        player.inventory.offerOrDrop(world, blockEntity.getStack(i-1));
                        blockEntity.removeStack(i-1);
                    }

                    return ActionResult.SUCCESS;
                }
            }

        }

        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
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

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}

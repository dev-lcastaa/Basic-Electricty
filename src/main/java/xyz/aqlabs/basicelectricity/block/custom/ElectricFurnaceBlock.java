package xyz.aqlabs.basicelectricity.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import xyz.aqlabs.basicelectricity.energy.EnergyStorage;

import java.util.Random;

public class ElectricFurnaceBlock extends Block {

    // Energy storage capacity
    private final EnergyStorage energyStorage;

    // Block state properties
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public ElectricFurnaceBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)
                .strength(3.5f, 6.0f)
                .requiresCorrectToolForDrops());

        this.energyStorage = new EnergyStorage(5000); // Example capacity

        // Set the default block state (furnace faces north and is not lit)
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Define the block's state properties
        builder.add(FACING, LIT);
    }

    // Determines the block state when placed
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Set the furnace to face the opposite direction of the player
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    // Handles block rotation
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    // Handles block mirroring
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }



    // Method to update the block's state each tick
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        boolean wasLit = state.getValue(LIT);
        boolean shouldBeLit = true;

        if (energyStorage.getStoredEnergy() >= 50) {
            energyStorage.extractEnergy(50, false);
            // Perform smelting action here
            shouldBeLit = false;
        }

        if (wasLit != shouldBeLit) {
            // Update the block state only if there's a change
            world.setBlock(pos, state.setValue(LIT, shouldBeLit), 3);
        }
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }
}

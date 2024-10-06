package xyz.aqlabs.basicelectricity.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.Level;
import xyz.aqlabs.basicelectricity.block.entity.CoalGeneratorBlockEntity;

import javax.annotation.Nullable;

public class CoalGeneratorBlock extends HorizontalDirectionalBlock {

    // Block state for whether the generator is lit (burning fuel) or not
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CoalGeneratorBlock() {
        super(Properties.copy(Blocks.BLAST_FURNACE).strength(3.5F).lightLevel(state -> state.getValue(LIT) ? 13 : 0)); // Light when active
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    // Create the block entity when the block is placed
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoalGeneratorBlockEntity(pos, state);
    }

    // Ticking behavior for the block (to update energy generation and fuel consumption)
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, st, entity) -> {
            if (entity instanceof CoalGeneratorBlockEntity coalGenerator) {
                coalGenerator.tick(); // Call the tick method in the BlockEntity
            }
        };
    }

    // Called when the block is placed, setting its facing direction
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    // Rotate the block when right-clicked or by redstone logic
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    // Mirror the block's state (used for world mirroring or redstone)
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // Create block state properties (lit and facing)
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT); // Add FACING and LIT to block state
    }

    // Drop the contents of the block entity's inventory when broken
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoalGeneratorBlockEntity) {
                Containers.dropContents(world, pos, (Container) blockEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    // Change the block's lit state based on fuel consumption (called by BlockEntityCoalGenerator)
    public static void setLit(Level world, BlockPos pos, boolean lit) {
        BlockState state = world.getBlockState(pos);
        world.setBlock(pos, state.setValue(LIT, lit), 3);
    }

}

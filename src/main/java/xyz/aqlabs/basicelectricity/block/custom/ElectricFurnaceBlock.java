package xyz.aqlabs.basicelectricity.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import xyz.aqlabs.basicelectricity.block.ModBlockEntities;
import xyz.aqlabs.basicelectricity.block.entity.ElectricFurnaceBlockEntity;

public class ElectricFurnaceBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ElectricFurnaceBlock() {
        super(Properties.copy(Blocks.BLAST_FURNACE).strength(3.5F).lightLevel(state -> state.getValue(LIT) ? 13 : 0)); // Light when active
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, true));
    }

    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState)pState.setValue(FACING, pRot.rotate((Direction)pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContex) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState>  pBuilder){
        pBuilder.add(FACING, LIT);
    }


    // Block Entity

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level level, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pPos);
            if(blockEntity instanceof ElectricFurnaceBlockEntity) {
                ((ElectricFurnaceBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, level, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof ElectricFurnaceBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer),(ElectricFurnaceBlockEntity) blockEntity, pPos);
            } else {
                throw new IllegalStateException("Our Container is missing");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ElectricFurnaceBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity>BlockEntityTicker<T> getTicker (Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()){
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ELECTRIC_FURNACE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

}

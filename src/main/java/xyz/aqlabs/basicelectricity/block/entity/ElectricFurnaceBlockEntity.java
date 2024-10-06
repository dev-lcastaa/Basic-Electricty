package xyz.aqlabs.basicelectricity.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.aqlabs.basicelectricity.block.ModBlockEntities;
import xyz.aqlabs.basicelectricity.screen.ElectricFurnaceMenu;


public class ElectricFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack){
            return switch (slot) {
                case 0 -> stack.getItem() == Items.RAW_IRON;
                case 1 -> false;
                default -> super.isItemValid(slot,stack);
            };
        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;


    public ElectricFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ELECTRIC_FURNACE.get(),pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ElectricFurnaceBlockEntity.this.progress;
                    case 1 -> ElectricFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                 switch (i) {
                    case 0 -> ElectricFurnaceBlockEntity.this.progress = i1;
                    case 1 -> ElectricFurnaceBlockEntity.this.maxProgress = i1;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }




    @Override
    public Component getDisplayName() {
        return Component.literal("Electric Furnace");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ElectricFurnaceMenu(i, inventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemStackHandler.serializeNBT());
        pTag.putInt("electric_furnace.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("electric_furnace.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++){
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick(Level level, BlockPos pPos, BlockState pState) {
        if (isOutputSlotEmptyOrReceivable() && hasRecipe()) {
            increaseCraftingProcess();
            setChanged(level, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        this.itemStackHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemStackHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(Items.IRON_INGOT,
                this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + 1));
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProcess() {
        this.progress++;
    }

    private boolean hasRecipe() {
        return canInsertAmountIntoOutputSlot(1) && canInsertItemIntoOutputSlot(Items.IRON_INGOT)
                && hasRecipeItemInInputSlot();
    }

    private boolean hasRecipeItemInInputSlot() {
        return this.itemStackHandler.getStackInSlot(INPUT_SLOT).getItem() == Items.RAW_IRON;
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() >=
                this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count;
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }


}

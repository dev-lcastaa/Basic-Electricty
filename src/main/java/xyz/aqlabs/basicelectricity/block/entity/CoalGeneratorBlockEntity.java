package xyz.aqlabs.basicelectricity.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import xyz.aqlabs.basicelectricity.block.ModBlockEntities;
import xyz.aqlabs.basicelectricity.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CoalGeneratorBlockEntity extends BlockEntity {


    private static final int ENERGY_PER_TICK = 20; // Amount of energy generated per tick

    // Inventory to hold the fuel (1 slot)
    private final ItemStackHandler fuelHandler = new ItemStackHandler(1);

    // Energy storage for the generator
    private final EnergyStorage energyStorage = new EnergyStorage(10000); // Example capacity

    // Cache LazyOptionals for capability handling
    private final LazyOptional<ItemStackHandler> fuelHandlerOptional = LazyOptional.of(() -> fuelHandler);
    private final LazyOptional<EnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);

    // Burn time management
    private int burnTime = 0;          // Remaining ticks the current fuel will last
    private int totalBurnTime = 0;     // Total burn time of the current fuel

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COAL_GENERATOR.get(), pos, state);
    }

    public void tick() {
        boolean isBurning = burnTime > 0;

        if (isBurning) {
            // Decrease burn time and generate energy
            burnTime--;
            energyStorage.receiveEnergy(ENERGY_PER_TICK, false); // Generate energy while burning
        }

        // If we aren't burning, try to consume fuel
        if (!isBurning && !fuelHandler.getStackInSlot(0).isEmpty()) {
            ItemStack fuelStack = fuelHandler.getStackInSlot(0);
            totalBurnTime = burnTime = getBurnTime(fuelStack);

            if (burnTime > 0) {
                fuelHandler.extractItem(0, 1, false); // Consume 1 item of fuel
            }
        }

        // Mark block entity as changed to sync with the client (e.g., for animation)
        setChanged();
    }

    // Get the burn time for a given fuel item
    private int getBurnTime(ItemStack fuel) {
        Item item = fuel.getItem();
        return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
    }

    // NBT (Data Persistence)

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        fuelHandler.deserializeNBT(tag.getCompound("FuelInventory"));
        energyStorage.deserializeNBT(tag.getCompound("Energy"));
        burnTime = tag.getInt("BurnTime");
        totalBurnTime = tag.getInt("TotalBurnTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("FuelInventory", fuelHandler.serializeNBT());
        tag.put("Energy", energyStorage.serializeNBT());
        tag.putInt("BurnTime", burnTime);
        tag.putInt("TotalBurnTime", totalBurnTime);
    }

    // Capability handling for energy and fuel
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return fuelHandlerOptional.cast();
        }
        if (cap == ForgeCapabilities.ENERGY) {
            return energyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fuelHandlerOptional.invalidate();
        energyOptional.invalidate();
    }

}

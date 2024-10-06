package xyz.aqlabs.basicelectricity.energy;

public interface IEnergyStorage {
    int receiveEnergy(int amount, boolean simulate); // Add energy
    int extractEnergy(int amount, boolean simulate); // Remove energy
    int getStoredEnergy(); // Current stored energy
    int getMaxEnergyStored(); // Maximum capacity
    boolean canReceive(); // Can receive energy
    boolean canExtract(); // Can extract energy
}

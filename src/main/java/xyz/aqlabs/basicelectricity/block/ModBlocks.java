package xyz.aqlabs.basicelectricity.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.aqlabs.basicelectricity.BasicElectricity;
import xyz.aqlabs.basicelectricity.block.custom.ElectricFurnaceBlock;
import xyz.aqlabs.basicelectricity.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BasicElectricity.MOD_ID);

    public static final RegistryObject<Block> ELECTRIC_FURNACE = registerBlock("electric_furnace",
            () -> new ElectricFurnaceBlock());

//    public static final RegistryObject<Block> ELECTRIC_BLAST_FURNACE = registerBlock("electric_blast_furnace",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)));
//
//    public static final RegistryObject<Block> ELECTRIC_SMOKER = registerBlock("electric_smoker",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)));
//
//    public static final RegistryObject<Block> ELECTRIC_BREWERY = registerBlock("electric_brewery",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)));
//
//    public static final RegistryObject<Block> COAL_GENERATOR = registerBlock("coal_generator",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE)));
//
//    public static final RegistryObject<Block> SILICON_ORE = registerBlock("silicon_ore",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)));
//
//    public static final RegistryObject<Block> BLACK_SAND = registerBlock("black_sand",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND)));
//
//    public static final RegistryObject<Block> SOLAR_PANEL = registerBlock("solar_panel",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DAYLIGHT_DETECTOR)));







    public static void register(IEventBus eventbus) {
        BLOCKS.register(eventbus);
    }

    // Registers the Block
    private static <T extends Block> RegistryObject<Block> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> blockToRegister = BLOCKS.register(name, block);
        registerBlockItem(name, blockToRegister);
        return (RegistryObject<Block>) blockToRegister;
    }

    // Registers the Blocks Item
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

}

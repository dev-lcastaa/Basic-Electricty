package xyz.aqlabs.basicelectricity.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.aqlabs.basicelectricity.BasicElectricity;
import xyz.aqlabs.basicelectricity.block.entity.CoalGeneratorBlockEntity;
import xyz.aqlabs.basicelectricity.block.entity.ElectricFurnaceBlockEntity;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BasicElectricity.MOD_ID);

    public static final RegistryObject<BlockEntityType<ElectricFurnaceBlockEntity>> ELECTRIC_FURNACE =
            BLOCK_ENTITIES.register("electric_furnace", () ->
                    BlockEntityType.Builder.of(ElectricFurnaceBlockEntity::new,
                            ModBlocks.ELECTRIC_FURNACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR =
            BLOCK_ENTITIES.register("coal_generator", () ->
                    BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new,
                            ModBlocks.COAL_GENERATOR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

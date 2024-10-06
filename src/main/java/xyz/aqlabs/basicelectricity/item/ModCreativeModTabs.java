package xyz.aqlabs.basicelectricity.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import xyz.aqlabs.basicelectricity.BasicElectricity;
import xyz.aqlabs.basicelectricity.block.ModBlocks;

public class ModCreativeModTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BasicElectricity.MOD_ID);


    public static RegistryObject<CreativeModeTab> BASIC_ELECTRICITY_TAB = CREATIVE_MODE_TAB.register("basicelectricity_tab",
            () -> CreativeModeTab.builder()
                    //.icon(() -> new ItemStack(ModItems.WAFER.get()))
                    .title(Component.translatable("creative.basicelectricity_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // Modded Items
//                        output.accept(ModItems.CABLE.get());
//                        output.accept(ModItems.MAGNET.get());
//                        output.accept(ModItems.REFINED_SILICON.get());
//                        output.accept(ModItems.RAW_SILICON.get());
//                        output.accept(ModItems.ELECTRIC_MOTOR.get());
//                        output.accept(ModItems.WAFER.get());

                        // Modded Blocks
                        output.accept(ModBlocks.ELECTRIC_FURNACE.get());
//                        output.accept(ModBlocks.ELECTRIC_BLAST_FURNACE.get());
//                        output.accept(ModBlocks.ELECTRIC_SMOKER.get());
//                        output.accept(ModBlocks.ELECTRIC_BREWERY.get());
                        output.accept(ModBlocks.COAL_GENERATOR.get());
//                        output.accept(ModBlocks.SILICON_ORE.get());
//                        output.accept(ModBlocks.BLACK_SAND.get());
//                        output.accept(ModBlocks.SOLAR_PANEL.get());
                    })
                    .build());




    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}

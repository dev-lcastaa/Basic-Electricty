package xyz.aqlabs.basicelectricity.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.aqlabs.basicelectricity.BasicElectricity;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BasicElectricity.MOD_ID);

//    public static final RegistryObject<Item> CABLE = ITEMS.register("cable",
//            () -> new Item(new Item.Properties()));
//
//    public static final RegistryObject<Item> MAGNET = ITEMS.register("magnet",
//            () -> new Item(new Item.Properties()));
//
//    public static final RegistryObject<Item> REFINED_SILICON = ITEMS.register("refined_silicon",
//            () -> new Item(new Item.Properties()));
//
//    public static final RegistryObject<Item> RAW_SILICON = ITEMS.register("raw_silicon",
//            () -> new Item(new Item.Properties()));
//
//    public static final RegistryObject<Item> ELECTRIC_MOTOR = ITEMS.register("electric_motor",
//            () -> new Item(new Item.Properties()));
//
//    public static final RegistryObject<Item> WAFER = ITEMS.register("wafer",
//            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

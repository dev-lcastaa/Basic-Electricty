package xyz.aqlabs.basicelectricity.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.aqlabs.basicelectricity.BasicElectricity;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BasicElectricity.MOD_ID);

    public static final RegistryObject<MenuType<ElectricFurnaceMenu>> ELECTRIC_FURNACE_MENU =
            registryMenuType( ElectricFurnaceMenu::new, "electric_furnace_menu");







    public static void register (IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registryMenuType(IContainerFactory<T> factory, String name){
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

}

package akki697222.retrocomputers.common.registers;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.client.gui.FrameContainerScreen;
import akki697222.retrocomputers.common.menu.FrameContainerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, RetroComputers.MODID);

    public static final Supplier<MenuType<FrameContainerMenu>> FRAME_BLOCK_CONTAINER = MENU_TYPES.register("frame_block", () -> new MenuType<>(FrameContainerMenu::new, FeatureFlags.DEFAULT_FLAGS));
}

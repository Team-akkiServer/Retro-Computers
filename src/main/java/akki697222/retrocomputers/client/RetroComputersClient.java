package akki697222.retrocomputers.client;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.client.gui.FrameContainerScreen;
import akki697222.retrocomputers.common.registers.MenuTypes;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.player.Input;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static akki697222.retrocomputers.RetroComputers.computers;

@EventBusSubscriber(modid = RetroComputers.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RetroComputersClient {
    public static Logger clientLogger = LoggerFactory.getLogger("Retro Computers Client");
    @SubscribeEvent
    public static void onRegisterMenuScreen(RegisterMenuScreensEvent event) {
        clientLogger.debug("Registering Menu Screens...");
        event.register(MenuTypes.FRAME_BLOCK_CONTAINER.get(), FrameContainerScreen::new);
    }

    public static void onKeyInput(InputEvent.Key event) {
        int keyCode = event.getKey();
        int scanCode = event.getScanCode();
        boolean isPressed = event.getAction() != InputConstants.RELEASE;

        computers.forEach((uuid, computer) -> {
            computer.onKeyInput(keyCode, scanCode, isPressed);
        });
    }
    public static void registerListener(IEventBus modEventBus) {
        //@SubscribeEventはmodEventBus.addListenerを勝手に呼び出してくれるため、@SubscribeEventがついたメソッドをaddListenerで登録すると2回呼び出されてしまう
        NeoForge.EVENT_BUS.addListener(RetroComputersClient::onKeyInput);
    }
}

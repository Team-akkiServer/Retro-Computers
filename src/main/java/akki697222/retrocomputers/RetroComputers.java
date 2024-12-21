package akki697222.retrocomputers;

import akki697222.retrocomputers.api.computer.Computer;
import akki697222.retrocomputers.client.RetroComputersClient;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import akki697222.retrocomputers.common.components.expansions.TestExpansion;
import akki697222.retrocomputers.common.registers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(RetroComputers.MODID)
public class RetroComputers {
    public static final String MODID = "retro_computers";
    public static final Logger logger = LoggerFactory.getLogger("Retro Computers");
    public static final File rc_root = new File(".", MODID);
    public static final File computer_data = new File(rc_root, "computers");
    public static final Map<UUID, Computer> computers = new HashMap<>();
    public RetroComputers(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        registerRegisters(modEventBus);
        registerListeners(modEventBus);

        computer_data.mkdirs();

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            executor.execute(() -> {
                computers.forEach((uuid, computer) -> {
                    if (computer.getPowerState()) {
                        computer.update();
                    }
                });
            });
        }
    }

    private void registerRegisters(IEventBus modEventBus) {
        Items.ITEMS.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        BlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MenuTypes.MENU_TYPES.register(modEventBus);
        CreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        logger.info("Registered ALL mod contents.");
    }

    private void registerListeners(IEventBus modEventBus) {
        RetroComputersClient.registerListener(modEventBus);
        logger.info("Registered ALL event listeners.");
    }
}

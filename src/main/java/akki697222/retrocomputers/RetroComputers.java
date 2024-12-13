package akki697222.retrocomputers;

import akki697222.retrocomputers.client.RetroComputersClient;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import akki697222.retrocomputers.common.components.expansions.TestExpansion;
import akki697222.retrocomputers.common.registers.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(RetroComputers.MODID)
public class RetroComputers {
    public static final String MODID = "retro_computers";
    public static final Logger logger = LoggerFactory.getLogger("Retro Computers");

    public RetroComputers(IEventBus modEventBus, ModContainer modContainer) {
        registerRegisters(modEventBus);
        registerListeners(modEventBus);

        BasicLogicBoardComponent logicBoard = new BasicLogicBoardComponent();

        logicBoard.runProgram("print(graphics)print(graphics.drawText)");
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

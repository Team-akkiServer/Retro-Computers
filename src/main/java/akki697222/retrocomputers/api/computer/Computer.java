package akki697222.retrocomputers.api.computer;

import akki697222.retrocomputers.api.computer.renderer.ScreenRenderQueues;
import akki697222.retrocomputers.api.globals.GraphicsLib;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import org.apache.commons.io.file.PathUtils;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Executors;

import static akki697222.retrocomputers.RetroComputers.*;

public class Computer implements IComputer {
    protected boolean powerState;
    protected final BasicLogicBoardComponent logicBoard;
    protected final UUID computerUuid;
    protected ScreenRenderQueues renderQueues;
    protected ComputerScreenScreen computerScreen;
    protected LuaEventProcessor processor = null;
    protected Queue<LuaEvent> eventQueue = new LinkedList<>();
    private LuaThread currentLuaThread = null;

    public Computer(@NotNull UUID computerUuid, BasicLogicBoardComponent logicBoard, ComputerScreenScreen computerScreen, ScreenRenderQueues renderQueues) {
        this.computerUuid = computerUuid;
        this.logicBoard = logicBoard;
        this.powerState = false;
        this.computerScreen = computerScreen;
        this.renderQueues = renderQueues;
        computers.put(computerUuid.toString(), this);
    }

    public void onChanged(ComputerScreenScreen computerScreen) {
        this.computerScreen = computerScreen;
    }

    @Override
    public void turnOn() {
        logger.info("Computer '{}' has turned on", computerUuid);
        powerState = true;
        logicBoard.init();
        Path rom = computer_data.toPath().resolve(computerUuid.toString() + "_ROM/init.lua");
        try {
            if (!runProgram(Files.readString(rom, StandardCharsets.UTF_8))) {
                logger.warn("Failed to initialize lua file");
            }
        } catch (FileNotFoundException | NoSuchFileException e) {
            drawErrorScreen("'init.lua' not found");
        } catch (IOException e) {
            logger.error("Failed to read file", e);
        }
    }

    @Override
    public void turnOff() {
        logger.info("Computer '{}' has turned off", computerUuid);
        powerState = false;
        renderQueues.clear();
    }

    @Override
    public void update() {
        logicBoard.update();
        if (currentLuaThread != null && powerState) {
            try {
                if (processor == null) {
                    processor = new LuaEventProcessor(eventQueue, currentLuaThread);
                } else {
                    processor.processNextEvent();
                }
                if (currentLuaThread.getStatus().equals("dead")) {
                    currentLuaThread = null;
                    logger.info("LuaThread was dead");
                }
            } catch (LuaError e) {
                logger.error("Lua Execution Failed", e);
                drawErrorScreen(e.getMessage());
                currentLuaThread = null;
            }
        }
    }

    @Override
    public boolean getPowerState() {
        return powerState;
    }

    @Override
    public boolean runProgram(String program) {
        try {
            Globals globals = new Globals();

            globals.load(new JseBaseLib());
            globals.load(new PackageLib());
            globals.load(new Bit32Lib());
            globals.load(new TableLib());
            globals.load(new StringLib());
            globals.load(new CoroutineLib());
            globals.load(new JseMathLib());
            globals.load(new JseIoLib());
            globals.load(new JseOsLib());
            globals.load(new GraphicsLib(computerScreen));

            LoadState.install(globals);
            LuaC.install(globals);

            LuaValue chunk = globals.load(program);
            currentLuaThread = new LuaThread(globals, chunk);
            processor = new LuaEventProcessor(eventQueue, currentLuaThread);
        } catch (LuaError e) {
            drawErrorScreen(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.info("Uncaught exception: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Successfully executed lua code:\n {}", program);
        }
        return true;
    }

    protected void drawErrorScreen(String error) {
        computerScreen.drawPixelRectangle(0, 0, ComputerScreenScreen.NATIVE_WIDTH, ComputerScreenScreen.NATIVE_HEIGHT, 0xFF0000FF);
        computerScreen.drawText("Critical Error!", 0, 0, 0xFFFFFFFF);
        computerScreen.drawText(error, 0, 8, 0xFFFFFFFF);
    }

    public ScreenRenderQueues getRenderQueues() {
        return renderQueues;
    }

    public void onKeyInput(int keyCode, int scanCode, boolean isPressed) {
        eventQueue.add(new LuaEvent("key_input", LuaInteger.valueOf(keyCode), LuaInteger.valueOf(scanCode), LuaBoolean.valueOf(isPressed)));
    }
}

package akki697222.retrocomputers.api.computer;

import akki697222.retrocomputers.api.computer.renderer.ScreenRenderQueues;
import akki697222.retrocomputers.api.globals.GraphicsLib;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import org.apache.commons.io.file.PathUtils;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static akki697222.retrocomputers.RetroComputers.*;

public class Computer implements IComputer {
    protected boolean powerState;
    protected final BasicLogicBoardComponent logicBoard;
    protected final UUID computerUuid;
    protected ScreenRenderQueues renderQueues;
    protected final ComputerScreenScreen computerScreen;
    public Computer(@NotNull UUID computerUuid, BasicLogicBoardComponent logicBoard, ComputerScreenScreen computerScreen, ScreenRenderQueues renderQueues) {
        this.computerUuid = computerUuid;
        this.logicBoard = logicBoard;
        this.powerState = false;
        this.computerScreen = computerScreen;
        this.renderQueues = renderQueues;
        computers.put(computerUuid, this);
    }
    @Override
    public void turnOn() {
        powerState = true;
        logicBoard.init();
        Path rom = computer_data.toPath().resolve(computerUuid.toString() + "_ROM/init.lua");
        try {
            this.runProgram(Files.readString(rom, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            drawErrorScreen("'init.lua' not found");
        } catch (IOException e) {
            logger.error("Failed to read file", e);
        }
    }

    @Override
    public void turnOff() {
        powerState = false;
    }

    @Override
    public void update() {
        logicBoard.update();
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
            chunk.call();
        } catch (LuaError e) {
            drawErrorScreen(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.info("Uncaught exception: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Successfully executed lua code: {}", program);
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

    }
}

package akki697222.retrocomputers.common.components;

import akki697222.retrocomputers.api.component.ComponentProperty;
import akki697222.retrocomputers.api.component.ComponentTier;
import akki697222.retrocomputers.api.component.IBasicComponent;
import akki697222.retrocomputers.api.component.IExpansionComponent;
import akki697222.retrocomputers.api.globals.*;
import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import static akki697222.retrocomputers.RetroComputers.logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BasicLogicBoardComponent implements IBasicComponent {
    private final Map<Integer, IExpansionComponent> expansions;

    public BasicLogicBoardComponent(@NotNull Map<Integer, IExpansionComponent> expansions) {
        this.expansions = expansions;
    }

    public BasicLogicBoardComponent() {
        this.expansions = new HashMap<>();
    }

    public void runProgram(String code) {
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
            globals.load(new GraphicsLib());

            LoadState.install(globals);
            LuaC.install(globals);

            LuaValue chunk = globals.load(code);
            chunk.call();
        } catch (LuaError e) {
            ComputerScreenScreen.instance().drawPixelRectangle(0, 0, ComputerScreenScreen.NATIVE_WIDTH, ComputerScreenScreen.NATIVE_HEIGHT, 0xFF0000FF);
            ComputerScreenScreen.instance().drawText(e.getMessage(), 0, 0, 0xFFFFFFFF);
        } catch (StackOverflowError soe) {
            soe.printStackTrace();
        } catch (Exception e) {
            logger.info("Throwing Uncaught exception: " + e.getMessage());
            throw e;
        } finally {
            logger.info("Successfully executed lua code: " + code);
        }
    }

    public void init() {
        logger.info("Initializing LogicBoard with Components:");
        expansions.forEach((id, expansion) -> {
            logger.info("ID " + id + " : " + expansion.getProperty().getName());
            expansion.setUsing(true);
            expansion.init();
        });
    }

    public void tick() {
        expansions.forEach((id, expansion) -> {
            expansion.update();
        });
    }

    public void addExpansion(IExpansionComponent expansionComponent) {
        expansions.put(expansions.size() + 1, expansionComponent);
    }

    public void removeExpansion(Integer id) {
        expansions.remove(id);
    }

    @Override
    public ComponentProperty getProperty() {
        return new ComponentProperty(
                "Logic Board",
                ComponentTier.IRON,
                20,
                50
        );
    }
}

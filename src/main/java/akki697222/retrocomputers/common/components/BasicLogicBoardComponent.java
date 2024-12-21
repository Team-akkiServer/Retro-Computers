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

    public void init() {
        logger.info("Initializing LogicBoard with Components:");
        expansions.forEach((id, expansion) -> {
            logger.info("ID " + id + " : " + expansion.getProperty().getName());
            expansion.setUsing(true);
            expansion.init();
        });
    }

    public void update() {
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

package akki697222.retrocomputers.common.components.expansions;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.api.component.ComponentProperty;
import akki697222.retrocomputers.api.component.ComponentTier;
import akki697222.retrocomputers.api.component.IExpansionComponent;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import static akki697222.retrocomputers.RetroComputers.logger;

public class TestExpansion implements IExpansionComponent {
    private boolean using = false;
    @Override
    public ComponentProperty getProperty() {
        return new ComponentProperty(
                "Test",
                ComponentTier.IRON,
                0,
                0
        );
    }

    @Override
    public void setUsing(boolean state) {
        using = state;
    }

    @Override
    public boolean getUsing() {
        return using;
    }

    @Override
    public void update() {

    }

    @Override
    public void init() {

    }
}

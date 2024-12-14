package akki697222.retrocomputers.common.components.expansions;

import akki697222.retrocomputers.api.component.ComponentProperty;
import akki697222.retrocomputers.api.component.ComponentTier;
import akki697222.retrocomputers.api.component.IExpansionComponent;

public class InterpreterExpansion implements IExpansionComponent {
    protected boolean using = false;
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

    @Override
    public ComponentProperty getProperty() {
        return new ComponentProperty(
                "Interpreter Expansion",
                ComponentTier.IRON,
                1,
                5
        );
    }
}

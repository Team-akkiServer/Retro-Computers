package akki697222.retrocomputers.common.components.expansions;

import akki697222.retrocomputers.api.component.ComponentProperty;
import akki697222.retrocomputers.api.component.ComponentTier;
import akki697222.retrocomputers.api.component.IBasicComponent;
import akki697222.retrocomputers.api.component.IExpansionComponent;

public class CRTExpansion implements IExpansionComponent {
    private boolean using = false;
    @Override
    public ComponentProperty getProperty() {
        return new ComponentProperty(
                "CRT Monitor",
                ComponentTier.IRON,
                20,
                50
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

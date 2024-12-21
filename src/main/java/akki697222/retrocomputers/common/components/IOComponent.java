package akki697222.retrocomputers.common.components;

import akki697222.retrocomputers.api.component.ComponentProperty;
import akki697222.retrocomputers.api.component.ComponentTier;
import akki697222.retrocomputers.api.component.IBasicComponent;

public class IOComponent implements IBasicComponent {
    @Override
    public ComponentProperty getProperty() {
        return new ComponentProperty(
                "I/O Port",
                ComponentTier.IRON,
                4,
                8
        );
    }
}

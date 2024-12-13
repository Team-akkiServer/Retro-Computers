package akki697222.retrocomputers.common.items;

import akki697222.retrocomputers.api.component.ComponentTier;
import akki697222.retrocomputers.api.component.IBasicComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractComponentItem extends Item {
    private final IBasicComponent component;
    public AbstractComponentItem(IBasicComponent component) {
        super(new Properties().rarity(component.getProperty().getTier() == ComponentTier.GOLD ? Rarity.UNCOMMON : component.getProperty().getTier() == ComponentTier.DIAMOND ? Rarity.RARE : Rarity.COMMON));

        this.component = component;
    }

    public IBasicComponent getComponent() {
        return component;
    }
}

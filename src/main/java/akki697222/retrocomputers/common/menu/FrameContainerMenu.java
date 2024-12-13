package akki697222.retrocomputers.common.menu;

import akki697222.retrocomputers.common.registers.MenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameContainerMenu extends AbstractMachineMenu {
    public FrameContainerMenu(int containerId, Inventory playerInventory) {
        super(containerId, playerInventory, new SimpleContainer(15), new SimpleContainerData(15));
    }

    public FrameContainerMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(containerId, playerInventory, container, containerData);
    }
}

package akki697222.retrocomputers.common.blocks.entity;

import akki697222.retrocomputers.common.registers.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BasicFrameBlockEntity extends AbstractFrameBlockEntity {
    private final SimpleContainer inventory = new SimpleContainer(15);
    public BasicFrameBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntities.BASIC_FRAME.get(), pos, blockState);
    }

    public Container getInventory() {
        return inventory;
    }
}

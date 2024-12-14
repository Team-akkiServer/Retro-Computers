package akki697222.retrocomputers.common.menu;

import akki697222.retrocomputers.common.items.AbstractComponentItem;
import akki697222.retrocomputers.common.items.BasicLogicBoardComponentItem;
import akki697222.retrocomputers.common.items.ComponentItem;
import akki697222.retrocomputers.common.items.ExpansionComponentItem;
import akki697222.retrocomputers.common.registers.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbstractMachineMenu extends AbstractContainerMenu {
    public AbstractMachineMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MenuTypes.FRAME_BLOCK_CONTAINER.get(), containerId);

        for (int col = 0; col < 5; col++) {
            this.addSlot(new FilteredSlot(container, col, 8 + col * 18, 20, ExpansionComponentItem.class));
        }

        this.addSlot(new FilteredSlot(container, 5, 151, 20, BasicLogicBoardComponentItem.class));

        for (int col = 0; col < 9; col++) {
            this.addSlot(new FilteredSlot(container, col + 6, 8 + col * 18, 53, ComponentItem.class));
        }

        for (int row = 0; row < 3; row++) {
            for(int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.addDataSlots(containerData);
    }


    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();

            // ブロックのインベントリから（0-8）プレイヤーインベントリへ（9-35）
            if (quickMovedSlotIndex < 15) {
                if (!this.moveItemStackTo(rawStack, 15, 42, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // プレイヤーインベントリからブロックのインベントリへ
            else if (!this.moveItemStackTo(rawStack, 0, 15, false)) {
                // プレイヤーインベントリ内での移動
                if (quickMovedSlotIndex < 24) { // ホットバーからメインインベントリへ
                    if (!this.moveItemStackTo(rawStack, 24, 42, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(rawStack, 15, 24, false)) { // メインインベントリからホットバーへ
                    return ItemStack.EMPTY;
                }
            }

            if (rawStack.isEmpty()) {
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                quickMovedSlot.setChanged();
            }

            if (rawStack.getCount() == quickMovedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}

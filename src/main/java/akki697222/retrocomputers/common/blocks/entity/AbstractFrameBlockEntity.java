package akki697222.retrocomputers.common.blocks.entity;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.api.component.IBasicComponent;
import akki697222.retrocomputers.api.component.IExpansionComponent;
import akki697222.retrocomputers.api.computer.Computer;
import akki697222.retrocomputers.common.items.AbstractComponentItem;
import akki697222.retrocomputers.common.menu.FrameContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Iterator;
import java.util.UUID;

import static akki697222.retrocomputers.RetroComputers.computers;
import static akki697222.retrocomputers.RetroComputers.logger;

public abstract class AbstractFrameBlockEntity extends BlockEntity implements Container, MenuProvider {
    protected final IEnergyStorage energyStorage;
    protected final ContainerData dataAccess;
    protected File computerRoot;
    protected UUID computerUuid = UUID.randomUUID();
    protected NonNullList<ItemStack> items = NonNullList.withSize(
            getContainerSize(),
            ItemStack.EMPTY
    );

    public AbstractFrameBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.computerRoot = null;
        this.dataAccess = new SimpleContainerData(15);
        this.energyStorage = new EnergyStorage(16000, 1000, 500);
    }

    /**
     * frame inventories have:
     * 9 expansion card slot (controllers, internet cards...),
     * 1 Logic board slot,
     * 5 frame extension slot (speakers, monitors...)
     * @return i
     */
    @Override
    public int getContainerSize() {
        return 15;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.items, slot, amount);
        this.setChanged();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = ContainerHelper.takeItem(this.items, slot);
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        stack.limitSize(this.getMaxStackSize(stack));
        this.items.set(slot, stack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
        this.setChanged();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.retro_computers.frame.title");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FrameContainerMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (computerRoot == null && computerUuid != null) {
            computerRoot = new File(RetroComputers.computer_data, computerUuid + "_ROM");
        }

        if (computerRoot != null && !computerRoot.exists()) {
            computerRoot.mkdir();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractFrameBlockEntity blockEntity) {
        IEnergyStorage storage = blockEntity.energyStorage;
        blockEntity.getItems().forEach(i -> {
            Item item = i.getItem();
            if (item instanceof AbstractComponentItem componentItem) {
                if (componentItem.getComponent() instanceof IExpansionComponent component) {
                    if (storage.getEnergyStored() > component.getProperty().getPowerConsumption()) {
                        component.update();
                        storage.extractEnergy(component.getProperty().getPowerConsumption(), false);
                    }
                } else if (componentItem.getComponent() instanceof IBasicComponent component) {
                    if (storage.getEnergyStored() > component.getProperty().getPowerConsumption()) {
                        storage.extractEnergy(component.getProperty().getPowerConsumption(), false);
                    }
                }
            }
        });
        Computer computer = computers.get(blockEntity.getUuid().toString());
        if (computer != null) {
            computer.update();
        }
    }

    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.computerUuid = tag.getUUID("Uuid");
        ContainerHelper.loadAllItems(tag, this.items, registries);

        logger.info("Loading BlockEntity data: {}", tag);

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                logger.info("Loaded item in slot {}: {}", i, stack.getItem());
            }
        }

        computerRoot = new File(RetroComputers.computer_data, computerUuid.toString() + "_ROM");
        if (!computerRoot.exists()) {
            computerRoot.mkdir();
        }
    }

    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putUUID("Uuid", this.computerUuid);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    public UUID getUuid() {
        return computerUuid;
    }
}

package akki697222.retrocomputers.common.blocks;

import akki697222.retrocomputers.client.gui.ComputerScreenScreen;
import akki697222.retrocomputers.common.blocks.entity.AbstractFrameBlockEntity;
import akki697222.retrocomputers.common.blocks.entity.BasicFrameBlockEntity;
import akki697222.retrocomputers.common.components.BasicLogicBoardComponent;
import akki697222.retrocomputers.common.items.BasicLogicBoardComponentItem;
import akki697222.retrocomputers.common.menu.FrameContainerMenu;
import akki697222.retrocomputers.common.registers.BlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public abstract class AbstractFrameBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public AbstractFrameBlock() {
        super(Properties.of().sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof AbstractFrameBlockEntity frameBlockEntity) {
            if (player.isShiftKeyDown()) {
                player.openMenu((MenuProvider) blockentity);
            } else {
                Minecraft minecraft = Minecraft.getInstance();

                minecraft.execute(() -> {
                    minecraft.setScreen(new ComputerScreenScreen(frameBlockEntity));
                });
            }
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return (BlockEntityTicker<T>) (BlockEntityTicker<AbstractFrameBlockEntity>) AbstractFrameBlockEntity::tick;
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }
}

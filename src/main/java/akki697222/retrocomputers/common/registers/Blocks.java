package akki697222.retrocomputers.common.registers;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.common.blocks.BasicFrameBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, RetroComputers.MODID);

    public static final DeferredHolder<Block, BasicFrameBlock> BASIC_FRAME = registerBlock("basic_frame", BasicFrameBlock::new);

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> blockHolder = BLOCKS.register(name, block);
        Items.registerBlockItem(blockHolder);
        return blockHolder;
    }
}

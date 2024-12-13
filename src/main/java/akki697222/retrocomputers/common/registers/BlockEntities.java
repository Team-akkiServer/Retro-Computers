package akki697222.retrocomputers.common.registers;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.common.blocks.entity.BasicFrameBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RetroComputers.MODID);

    public static final Supplier<BlockEntityType<BasicFrameBlockEntity>> BASIC_FRAME = BLOCK_ENTITIES.register(
            "basic_frame_entity",
            () -> BlockEntityType.Builder.of(
                            BasicFrameBlockEntity::new,
                            Blocks.BASIC_FRAME.get()
                    )
                    .build(null)
    );
}

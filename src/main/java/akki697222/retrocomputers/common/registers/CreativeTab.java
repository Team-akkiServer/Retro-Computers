package akki697222.retrocomputers.common.registers;

import akki697222.retrocomputers.RetroComputers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static akki697222.retrocomputers.common.registers.Items.itemList;

public class CreativeTab {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, RetroComputers.MODID);

    public static final Supplier<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("tab_main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + RetroComputers.MODID + ".main"))
            .icon(() -> new ItemStack(Blocks.BASIC_FRAME.get()))
            .displayItems((p, o) -> {
                itemList.forEach((i) -> {
                    o.accept(i.get());
                });
            })
            .build()
    );
}

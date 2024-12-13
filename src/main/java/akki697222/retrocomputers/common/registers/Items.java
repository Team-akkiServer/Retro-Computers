package akki697222.retrocomputers.common.registers;

import akki697222.retrocomputers.RetroComputers;
import akki697222.retrocomputers.common.items.BasicLogicBoardComponentItem;
import akki697222.retrocomputers.common.items.CRTExpansionItem;
import akki697222.retrocomputers.common.items.TestExpansionItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Items {
    public static final List<DeferredHolder<Item, ? extends Item>> itemList = new ArrayList<>();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RetroComputers.MODID);

    public static final DeferredHolder<Item, TestExpansionItem> TEST_EXPANSION = registerItem("test_expansion", TestExpansionItem::new);
    public static final DeferredHolder<Item, CRTExpansionItem> CRT_EXPANSION = registerItem("crt_expansion", CRTExpansionItem::new);
    public static final DeferredHolder<Item, BasicLogicBoardComponentItem> BASIC_LOGIC_BOARD_COMPONENT = registerItem("basic_logic_board", BasicLogicBoardComponentItem::new);
    public static <T extends Item> DeferredHolder<Item, T> registerItem(String name, Supplier<T> item) {
        DeferredHolder<Item, T> i = ITEMS.register(name, item);
        itemList.add(i);
        return i;
    }

    public static DeferredHolder<Item, BlockItem> registerBlockItem(DeferredHolder<Block, ? extends Block> blockHolder) {
        DeferredHolder<Item, BlockItem> i = ITEMS.registerSimpleBlockItem(blockHolder);
        itemList.add(i);
        return i;
    }
}

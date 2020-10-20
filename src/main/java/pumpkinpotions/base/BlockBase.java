package pumpkinpotions.base;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import pumpkinpotions.PumpkinPotions;

import java.util.Set;

public class BlockBase extends Block implements Registerable {

    private final Item item;

    public BlockBase(Properties properties) {
        this(properties, new Item.Properties());
    }

    public BlockBase(Properties properties, Item.Properties itemProperties) {
        super(properties);
        item = new BlockItem(this, itemProperties.group(PumpkinPotions.TAB));
    }

    @Override
    public Set<Object> getAdditionalRegisters() {
        return ImmutableSet.of(item);
    }
}

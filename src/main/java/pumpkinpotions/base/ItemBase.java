package pumpkinpotions.base;

import pumpkinpotions.PumpkinPotions;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(Properties properties) {
        super(properties.group(PumpkinPotions.TAB));
    }
}

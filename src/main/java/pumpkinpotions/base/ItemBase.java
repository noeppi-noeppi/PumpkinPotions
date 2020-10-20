package pumpkinpotions.base;

import net.minecraft.item.Item;
import pumpkinpotions.PumpkinPotions;

public class ItemBase extends Item {

    public ItemBase(Properties properties) {
        super(properties.group(PumpkinPotions.TAB));
    }
}

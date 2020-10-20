package pumpkinpotions;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import pumpkinpotions.cauldron.BlockCauldron;

public class ModBlocks {

    public static final Block cauldron = new BlockCauldron(AbstractBlock.Properties.create(Material.IRON));

    public static void register() {
        PumpkinPotions.register("cauldron", cauldron);
    }
}

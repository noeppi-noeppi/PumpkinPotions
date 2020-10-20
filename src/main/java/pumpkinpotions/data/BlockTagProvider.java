/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package pumpkinpotions.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

import javax.annotation.Nonnull;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generator) {
        //noinspection deprecation
        super(generator);
    }

    @Nonnull
    @Override
    public String getName() {
        return "PumpkinPotions block tags";
    }

    @Override
    protected void registerTags() {
        //getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
    }
}

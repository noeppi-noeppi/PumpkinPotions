/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package pumpkinpotions.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import pumpkinpotions.PumpkinPotions;

import javax.annotation.Nonnull;

public class ItemTagProvider extends ItemTagsProvider {

    public ItemTagProvider(DataGenerator generatorIn, BlockTagProvider blockTagProvider) {
        //noinspection deprecation
        super(generatorIn, blockTagProvider);
    }

    @Nonnull
    @Override
    public String getName() {
        return "PumpkinPotions item tags";
    }

    @Override
    protected void registerTags() {

        //noinspection deprecation
        Registry.ITEM.stream()
                .filter(i -> PumpkinPotions.MODID.equals(Registry.ITEM.getKey(i).getNamespace()))
                .filter(i -> !(i instanceof BlockItem))
                .forEach(this::addDefaultItemTag);

        //noinspection deprecation
        Registry.ITEM.stream()
                .filter(i -> PumpkinPotions.MODID.equals(Registry.ITEM.getKey(i).getNamespace()))
                .filter(i -> i instanceof BlockItem)
                .map(i -> ((BlockItem) i).getBlock())
                .forEach(this::addDefaultBlockItemTag);

        //this.func_240522_a_(ModTags.Items.SHEARS).func_240534_a_(ModItems.elementiumShears, ModItems.manasteelShears);
    }

    public void addDefaultItemTag(Item item) {
        //this.getOrCreateBuilder(ModTags.Items.RUNES).add(item);
    }

    public void addDefaultBlockItemTag(Block block) {
        //this.getOrCreateBuilder(ModTags.Items.SPECIAL_FLOWERS).add(block.asItem());
    }
}

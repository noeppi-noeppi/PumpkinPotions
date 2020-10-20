/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package pumpkinpotions.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import pumpkinpotions.PumpkinPotions;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    private final Set<Item> handheld = new HashSet<>();

    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, PumpkinPotions.MODID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "PumpkinPotions item models";
    }

    @Override
    protected void registerModels() {
        @SuppressWarnings("deprecation")
        Set<Item> items = Registry.ITEM.stream().filter(i -> PumpkinPotions.MODID.equals(Registry.ITEM.getKey(i).getNamespace()))
                .collect(Collectors.toSet());
        registerItemBlocks(items.stream().filter(i -> i instanceof BlockItem).map(i -> (BlockItem) i).collect(Collectors.toSet()));
        registerItems(items.stream().filter(i -> !(i instanceof BlockItem)).collect(Collectors.toSet()));
    }

    private static String name(Item i) {
        //noinspection deprecation
        return Registry.ITEM.getKey(i).getPath();
    }

    private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");
    private static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");

    private ItemModelBuilder handheldItem(String name) {
        return withExistingParent(name, HANDHELD)
                .texture("layer0", new ResourceLocation(PumpkinPotions.MODID, "item/" + name));
    }

    @SuppressWarnings("UnusedReturnValue")
    private ItemModelBuilder handheldItem(Item i) {
        handheld.add(i);
        return handheldItem(name(i));
    }

    private ItemModelBuilder generatedItem(String name) {
        return withExistingParent(name, GENERATED)
                .texture("layer0", new ResourceLocation(PumpkinPotions.MODID, "item/" + name));
    }

    @SuppressWarnings("UnusedReturnValue")
    private ItemModelBuilder generatedItem(Item i) {
        return generatedItem(name(i));
    }

    private void registerItems(Set<Item> items) {

        //handheldItem(ModItems.alfsteelSword);
        //items.remove(ModItems.alfsteelPick);

        items.forEach(item -> {
            if (!(item instanceof BlockItem) && !handheld.contains(item)) {
                generatedItem(item);
            }
        });
    }

    private void registerItemBlocks(Set<BlockItem> itemBlocks) {
        //itemBlocks.remove(ModBlocks.alfsteelPylon.asItem());

        itemBlocks.forEach(i -> {
            @SuppressWarnings("deprecation")
            String name = Registry.ITEM.getKey(i).getPath();
            getBuilder(name).parent(new AlwaysExistentModelFile(new ResourceLocation(PumpkinPotions.MODID, "block/" + name)));
        });
    }
}

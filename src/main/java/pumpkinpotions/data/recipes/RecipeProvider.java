package pumpkinpotions.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import pumpkinpotions.ModBlocks;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {

    public RecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Nonnull
    @Override
    public String getName() {
        return "PumpkinPotions crafting recipes";
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.cauldron)
                .key('s', Tags.Items.NUGGETS_IRON)
                .key('p', Items.NETHER_WART)
                .key('o', Tags.Items.NUGGETS_IRON)
                .key('k', Items.CARVED_PUMPKIN)
                .key('y', Tags.Items.NUGGETS_IRON)
                .key('j', ItemTags.LOGS)
                .key('a', Items.BLAZE_POWDER)
                .key('m', ItemTags.LOGS)
                .patternLine("spo")
                .patternLine("oky")
                .patternLine("jam")
                .setGroup(String.valueOf(ModBlocks.cauldron.asItem().getRegistryName()))
                .addCriterion("has_item", hasItem(Items.CARVED_PUMPKIN))
                .build(consumer);
    }
}

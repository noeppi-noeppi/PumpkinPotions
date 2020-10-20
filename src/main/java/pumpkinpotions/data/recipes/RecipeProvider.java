package pumpkinpotions.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import pumpkinpotions.ModBlocks;
import pumpkinpotions.PumpkinPotions;

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
                .key('j', Ingredient.fromItems(Items.OAK_LOG, Items.ACACIA_LOG, Items.BIRCH_LOG, Items.DARK_OAK_LOG, Items.JUNGLE_LOG))
                .key('a', Items.BLAZE_POWDER)
                .key('m', Ingredient.fromItems(Items.OAK_LOG, Items.ACACIA_LOG, Items.BIRCH_LOG, Items.DARK_OAK_LOG, Items.JUNGLE_LOG))
                .patternLine("spo")
                .patternLine("oky")
                .patternLine("jam")
                .setGroup(String.valueOf(ModBlocks.cauldron.asItem().getRegistryName()))
                .addCriterion("has_item", hasItem(Items.CARVED_PUMPKIN))
                .build(consumer);
    }

    @SuppressWarnings({"SameParameterValue", "ConstantConditions"})
    private void makeBlockItemNugget(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider ingot, IItemProvider nugget) {

        makeBlockItem(consumer, block, ingot);

        ShapedRecipeBuilder.shapedRecipe(ingot)
                .key('a', nugget)
                .patternLine("aaa")
                .patternLine("aaa")
                .patternLine("aaa")
                .setGroup(ingot.asItem().getRegistryName() + "_from_nuggets")
                .addCriterion("has_item", hasItem(nugget))
                .build(consumer, new ResourceLocation(PumpkinPotions.MODID, ingot.asItem().getRegistryName().getPath() + "_from_nuggets"));

        ShapelessRecipeBuilder.shapelessRecipe(nugget, 9)
                .addIngredient(ingot)
                .setGroup(nugget.asItem().getRegistryName() + "_from_ingot")
                .addCriterion("has_item", hasItem(ingot))
                .build(consumer, new ResourceLocation(PumpkinPotions.MODID, nugget.asItem().getRegistryName().getPath() + "_from_ingot"));
    }

    @SuppressWarnings("ConstantConditions")
    private void makeBlockItem(Consumer<IFinishedRecipe> consumer, IItemProvider block, IItemProvider ingot) {

        ShapedRecipeBuilder.shapedRecipe(block)
                .key('a', ingot)
                .patternLine("aaa")
                .patternLine("aaa")
                .patternLine("aaa")
                .setGroup(block.asItem().getRegistryName() + "_from_ingots")
                .addCriterion("has_item", hasItem(ingot))
                .build(consumer, new ResourceLocation(PumpkinPotions.MODID, block.asItem().getRegistryName().getPath() + "_from_ingots"));

        ShapelessRecipeBuilder.shapelessRecipe(ingot, 9)
                .addIngredient(block)
                .setGroup(ingot.asItem().getRegistryName() + "_from_block")
                .addCriterion("has_item", hasItem(block))
                .build(consumer, new ResourceLocation(PumpkinPotions.MODID, ingot.asItem().getRegistryName().getPath() + "_from_block"));
    }
}

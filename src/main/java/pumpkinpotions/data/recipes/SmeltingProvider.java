package pumpkinpotions.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SmeltingProvider extends RecipeProvider {

	public SmeltingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	@Nonnull
	public String getName() {
		return "PumpkinPotions smelting recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		/*CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneForest), ModFluffBlocks.biomeStoneForest, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneForest))
				.build(consumer, "botania:smelting/metamorphic_forest_stone");*/
	}
}

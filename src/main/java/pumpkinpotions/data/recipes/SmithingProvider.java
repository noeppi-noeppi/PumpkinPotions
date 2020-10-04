package pumpkinpotions.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SmithingProvider extends RecipeProvider {

    public SmithingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	@Nonnull
	public String getName() {
		return "PumpkinPotions smithing recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        //SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terraSword), alfsteel, ModItems.alfsteelSword).func_240503_a_("alfsteel_sword_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_sword"))).func_240505_a_(consumer, new ResourceLocation(PumpkinPotions.MODID, "alfsteel_sword_smithing"));
    }
}

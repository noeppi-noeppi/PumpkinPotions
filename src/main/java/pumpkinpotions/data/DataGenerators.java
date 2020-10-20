package pumpkinpotions.data;

import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import pumpkinpotions.data.recipes.RecipeProvider;
import pumpkinpotions.data.recipes.SmeltingProvider;
import pumpkinpotions.data.recipes.SmithingProvider;

public class DataGenerators {

    public static void gatherData(GatherDataEvent evt) {
        if (evt.includeServer()) {
            evt.getGenerator().addProvider(new BlockLootProvider(evt.getGenerator()));
            BlockTagProvider blockTagProvider = new BlockTagProvider(evt.getGenerator());
            evt.getGenerator().addProvider(blockTagProvider);
            evt.getGenerator().addProvider(new ItemTagProvider(evt.getGenerator(), blockTagProvider));
            evt.getGenerator().addProvider(new RecipeProvider(evt.getGenerator()));
            evt.getGenerator().addProvider(new SmeltingProvider(evt.getGenerator()));
            evt.getGenerator().addProvider(new BlockStateProvider(evt.getGenerator(), evt.getExistingFileHelper()));
            evt.getGenerator().addProvider(new ItemModelProvider(evt.getGenerator(), evt.getExistingFileHelper()));
            evt.getGenerator().addProvider(new SmithingProvider(evt.getGenerator()));
        }
    }
}

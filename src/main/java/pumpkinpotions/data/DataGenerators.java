package pumpkinpotions.data;

import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import pumpkinpotions.data.recipes.RecipeProvider;

public class DataGenerators {

    public static void gatherData(GatherDataEvent evt) {
        if (evt.includeServer()) {
            evt.getGenerator().addProvider(new BlockLootProvider(evt.getGenerator()));
            evt.getGenerator().addProvider(new RecipeProvider(evt.getGenerator()));
            evt.getGenerator().addProvider(new ItemModelProvider(evt.getGenerator(), evt.getExistingFileHelper()));
        }
    }
}

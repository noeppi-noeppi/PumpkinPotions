package pumpkinpotions.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import pumpkinpotions.ModBlocks;
import pumpkinpotions.PumpkinPotions;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {

    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, PumpkinPotions.MODID, exFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "PumpkinPotions Blockstates";
    }

    @Override
    protected void registerStatesAndModels() {
        Set<Block> remainingBlocks = Registry.BLOCK.stream()
                .filter(b -> PumpkinPotions.MODID.equals(Registry.BLOCK.getKey(b).getNamespace()))
                .collect(Collectors.toSet());

       remainingBlocks.remove(ModBlocks.cauldron);

        remainingBlocks.forEach(this::defaultBlock);
    }

    private void manualModel(Set<Block> blocks, Block b) {
        String name = Registry.BLOCK.getKey(b).getPath();
        simpleBlock(b, models().getExistingFile(new ResourceLocation(PumpkinPotions.MODID, "block/" + name)));
        blocks.remove(b);
    }

    private void defaultBlock(Block block) {
        @SuppressWarnings("deprecation")
        String name = Registry.ITEM.getKey(block.asItem()).getPath();
        simpleBlock(block);
    }
}
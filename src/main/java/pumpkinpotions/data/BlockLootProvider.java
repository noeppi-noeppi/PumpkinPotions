/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package pumpkinpotions.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pumpkinpotions.PumpkinPotions;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockLootProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	private final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();

	public BlockLootProvider(DataGenerator generator) {
		this.generator = generator;

		//functionTable.put(ModBlocks.bifrost, BlockLootProvider::empty);
		//functionTable.put(ModBlocks.enchanter, b -> genRegular(Blocks.LAPIS_BLOCK));
	}

	@Nonnull
	@Override
	public String getName() {
		return "PumpkinPotions block loot tables";
	}

	@Override
	public void act(@Nonnull DirectoryCache cache) throws IOException {
		Map<ResourceLocation, LootTable.Builder> tables = new HashMap<>();

		for (Block b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
			if (!PumpkinPotions.MODID.equals(id.getNamespace())) {
				continue;
			}
			Function<Block, LootTable.Builder> func = functionTable.getOrDefault(b, BlockLootProvider::genRegular);
			tables.put(id, func.apply(b));
		}

		for (Map.Entry<ResourceLocation, LootTable.Builder> e : tables.entrySet()) {
			Path path = getPath(generator.getOutputFolder(), e.getKey());
			IDataProvider.save(GSON, cache, LootTableManager.toJson(e.getValue().setParameterSet(LootParameterSets.BLOCK).build()), path);
		}
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	private static LootTable.Builder empty(Block b) {
		return LootTable.builder();
	}

	private static LootTable.Builder genCopyNbt(Block b, String... tags) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
		CopyNbt.Builder func = CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY);
		for (String tag : tags) {
			func = func.replaceOperation(tag, "BlockEntityTag." + tag);
		}
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
				.acceptCondition(SurvivesExplosion.builder())
				.acceptFunction(func);
		return LootTable.builder().addLootPool(pool);
	}

	private static LootTable.Builder genRegular(Block b) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
				.acceptCondition(SurvivesExplosion.builder());
		return LootTable.builder().addLootPool(pool);
	}
}

package pumpkinpotions.brew;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import pumpkinpotions.ModEffects;
import pumpkinpotions.cauldron.ColorData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Brew {

    public static final Map<Effect, BrewData> EFFECT_DATA = ImmutableMap.<Effect, BrewData>builder()
            .put(Effects.SPEED, new BrewData(Effects.SLOWNESS, 7, 20 * 60 * 20, Items.SUGAR, AffinityTable.builder()
                    .amplifier(Affinity.AIR, 1)
                    .amplifier(Affinity.EARTH, -1)
                    .duration(Affinity.LANDSCAPE, 1)
                    .duration(Affinity.CAVE, -1)
                    .both(Affinity.DRY, 0.5, -0.5)
                    .both(Affinity.WET, -0.5, 0.5)
            ))
            .put(Effects.SLOWNESS, new BrewData(Effects.SPEED, 5, 20 * 60 * 10, Items.COBWEB, AffinityTable.builder()
                    .amplifier(Affinity.AIR, -1)
                    .amplifier(Affinity.EARTH, 1)
                    .duration(Affinity.LANDSCAPE, -1)
                    .duration(Affinity.CAVE, 1)
                    .both(Affinity.DRY, -0.5, 0.5)
                    .both(Affinity.WET, 0.5, -0.5)
            ))
            .put(Effects.HASTE, new BrewData(Effects.MINING_FATIGUE, 9, 20 * 60 * 20, Items.END_CRYSTAL, AffinityTable.builder()
                    .amplifier(Affinity.MINE, 1)
                    .amplifier(Affinity.CRAFT, -1)
                    .duration(Affinity.TREASURE, 1)
                    .duration(Affinity.TRASH, -1)
                    .both(Affinity.HOT, 0.5, -0.5)
                    .both(Affinity.COLD, -0.5, 0.5)
            ))
            .put(Effects.MINING_FATIGUE, new BrewData(Effects.HASTE, 2, 20 * 60 * 5, Items.WET_SPONGE, AffinityTable.builder()
                    .amplifier(Affinity.MINE, -1)
                    .amplifier(Affinity.CRAFT, 1)
                    .duration(Affinity.TREASURE, -1)
                    .duration(Affinity.TRASH, 1)
                    .both(Affinity.HOT, -0.5, 0.5)
                    .both(Affinity.COLD, 0.5, -0.5)
            ))
            .put(Effects.STRENGTH, new BrewData(Effects.WEAKNESS, 9, 20 * 60 * 20, Items.BLAZE_POWDER, AffinityTable.builder()
                    .amplifier(Affinity.POWER, 1)
                    .amplifier(Affinity.WEAK, -1)
                    .duration(Affinity.FIRE, 1)
                    .duration(Affinity.WATER, -1)
                    .both(Affinity.END, 0.5, -0.5)
                    .both(Affinity.HELL, -0.5, 0.5)
            ))
            .put(Effects.INSTANT_HEALTH, new BrewData(Effects.INSTANT_DAMAGE, 4, 0, Items.GLISTERING_MELON_SLICE, AffinityTable.builder()
                    .amplifier(Affinity.POWER, 1)
                    .amplifier(Affinity.WEAK, -1)
                    .amplifier(Affinity.LIGHT, 0.5)
                    .amplifier(Affinity.DARK, -0.5)
            ))
            .put(Effects.INSTANT_DAMAGE, new BrewData(Effects.INSTANT_HEALTH, 4, 0, Items.SOUL_TORCH, AffinityTable.builder()
                    .amplifier(Affinity.POWER, -1)
                    .amplifier(Affinity.WEAK, 1)
                    .amplifier(Affinity.LIGHT, -0.5)
                    .amplifier(Affinity.DARK, 0.5)
            ))
            .put(Effects.JUMP_BOOST, new BrewData(null, 4, 20 * 60 * 20, Items.RABBIT_FOOT, AffinityTable.builder()
                    .amplifier(Affinity.DRY, 1)
                    .amplifier(Affinity.WET, -1)
                    .duration(Affinity.HOT, 1)
                    .duration(Affinity.COLD, -1)
                    .both(Affinity.CAVE, 0.5, -0.5)
                    .both(Affinity.LANDSCAPE, -0.5, 0.5)
            ))
            .put(Effects.NAUSEA, new BrewData(null, 0, 20 * 60 * 3, Items.POISONOUS_POTATO, AffinityTable.builder()
                    .duration(Affinity.HELL, 1)
                    .duration(Affinity.DARK, -1)
                    .duration(Affinity.LIGHT, 0.75)
                    .duration(Affinity.END, -0.75)
            ))
            .put(Effects.REGENERATION, new BrewData(Effects.POISON, 4, 20 * 60 * 5, Items.GHAST_TEAR, AffinityTable.builder()
                    .amplifier(Affinity.POWER, 1)
                    .amplifier(Affinity.WEAK, -1)
                    .duration(Affinity.WET, 1)
                    .duration(Affinity.DRY, -1)
                    .both(Affinity.CAVE, 0.5, -0.5)
                    .both(Affinity.LANDSCAPE, -0.5, 0.5)
            ))
            .put(Effects.RESISTANCE, new BrewData(Effects.WITHER, 3, 20 * 60 * 10, Items.CRYING_OBSIDIAN, AffinityTable.builder()
                    .amplifier(Affinity.END, 1)
                    .amplifier(Affinity.HELL, -1)
                    .duration(Affinity.EARTH, 1)
                    .duration(Affinity.AIR, -1)
                    .both(Affinity.POWER, 0.5, -0.5)
                    .both(Affinity.WEAK, -0.5, 0.5)
            ))
            .put(Effects.FIRE_RESISTANCE, new BrewData(null, 0, 20 * 60 * 20, Items.MAGMA_CREAM, AffinityTable.builder()
                    .duration(Affinity.FIRE, 1)
                    .duration(Affinity.WATER, -1)
                    .duration(Affinity.HOT, 0.75)
                    .duration(Affinity.COLD, -0.75)
            ))
            .put(Effects.WATER_BREATHING, new BrewData(null, 0, 20 * 60 * 20, Items.PUFFERFISH, AffinityTable.builder()
                    .duration(Affinity.FIRE, -1)
                    .duration(Affinity.WATER, 1)
                    .duration(Affinity.DRY, -0.75)
                    .duration(Affinity.WET, 0.75)
            ))
            .put(Effects.INVISIBILITY, new BrewData(null, 0, 20 * 60 * 20, Items.GLASS, AffinityTable.builder()
                    .duration(Affinity.DARK, 1)
                    .duration(Affinity.LIGHT, -1)
                    .duration(Affinity.WEAK, 0.5)
                    .duration(Affinity.POWER, -0.5)
            ))
            .put(Effects.BLINDNESS, new BrewData(Effects.NIGHT_VISION, 0, 20 * 60 * 3, Items.INK_SAC, AffinityTable.builder()
                    .duration(Affinity.DARK, 1)
                    .duration(Affinity.HELL, -1)
                    .duration(Affinity.END, 0.75)
                    .duration(Affinity.LIGHT, -0.75)
            ))
            .put(Effects.NIGHT_VISION, new BrewData(Effects.BLINDNESS, 0, 20 * 60 * 20, Items.GOLDEN_CARROT, AffinityTable.builder()
                    .duration(Affinity.DARK, -1)
                    .duration(Affinity.LIGHT, 1)
                    .duration(Affinity.CAVE, 0.5)
                    .duration(Affinity.LANDSCAPE, -0.5)
            ))
            .put(Effects.HUNGER, new BrewData(Effects.SATURATION, 49, 20 * 60 * 5, Items.ROTTEN_FLESH, AffinityTable.builder()
                    .amplifier(Affinity.CAVE, 1)
                    .amplifier(Affinity.LANDSCAPE, -1)
                    .duration(Affinity.MINE, 1)
                    .duration(Affinity.CRAFT, -1)
                    .both(Affinity.HOT, 0.5, -0.5)
                    .both(Affinity.COLD, -0.5, 0.5)
            ))
            .put(Effects.WEAKNESS, new BrewData(Effects.STRENGTH, 1, 20 * 60 * 5, Items.FERMENTED_SPIDER_EYE, AffinityTable.builder()
                    .amplifier(Affinity.POWER, -1)
                    .amplifier(Affinity.WEAK, 1)
                    .duration(Affinity.FIRE, -1)
                    .duration(Affinity.WATER, 1)
                    .both(Affinity.END, -0.5, 0.5)
                    .both(Affinity.HELL, 0.5, -0.5)
            ))
            .put(Effects.POISON, new BrewData(Effects.REGENERATION, 3, 20 * 60 * 3, Items.SPIDER_EYE, AffinityTable.builder()
                    .amplifier(Affinity.POWER, -1)
                    .amplifier(Affinity.WEAK, 1)
                    .duration(Affinity.WET, -1)
                    .duration(Affinity.DRY, 1)
                    .both(Affinity.CAVE, -0.5, 0.5)
                    .both(Affinity.LANDSCAPE, 0.5, -0.5)
            ))
            .put(Effects.WITHER, new BrewData(Effects.RESISTANCE, 3, 20 * 60 * 3, Items.WITHER_ROSE, AffinityTable.builder()
                    .amplifier(Affinity.END, -1)
                    .amplifier(Affinity.HELL, 1)
                    .duration(Affinity.EARTH, -1)
                    .duration(Affinity.AIR, 1)
                    .both(Affinity.POWER, -0.5, 0.5)
                    .both(Affinity.WEAK, 0.5, -0.5)
            ))
            .put(Effects.HEALTH_BOOST, new BrewData(null, 9, 20 * 60 * 10, Items.CAKE, AffinityTable.builder()
                    .amplifier(Affinity.HELL, 1)
                    .amplifier(Affinity.END, -1)
                    .duration(Affinity.TRASH, 1)
                    .duration(Affinity.TREASURE, -1)
                    .both(Affinity.WATER, 0.5, -0.5)
                    .both(Affinity.FIRE, -0.5, 0.5)
            ))
            .put(Effects.ABSORPTION, new BrewData(null, 9, 20 * 60 * 20, Items.GOLDEN_APPLE, AffinityTable.builder()
                    .amplifier(Affinity.HELL, -1)
                    .amplifier(Affinity.END, 1)
                    .duration(Affinity.TRASH, -1)
                    .duration(Affinity.TREASURE, 1)
                    .both(Affinity.WATER, -0.5, 0.5)
                    .both(Affinity.FIRE, 0.5, -0.5)
            ))
            .put(Effects.SATURATION, new BrewData(Effects.HUNGER, 19, 0, Items.PUMPKIN_PIE, AffinityTable.builder()
                    .amplifier(Affinity.CAVE, 1)
                    .amplifier(Affinity.LANDSCAPE, -1)
                    .amplifier(Affinity.HOT, 0.5)
                    .amplifier(Affinity.COLD, -0.5)
            ))
            .put(Effects.GLOWING, new BrewData(null, 0, 20 * 60 * 20, Items.LANTERN, AffinityTable.builder()
                    .duration(Affinity.LIGHT, 1)
                    .duration(Affinity.DARK, -1)
                    .duration(Affinity.WET, -0.5)
                    .duration(Affinity.DRY, 0.5)
            ))
            .put(Effects.LEVITATION, new BrewData(null, 39, 25, Items.SHULKER_SHELL, AffinityTable.builder()
                    .amplifier(Affinity.AIR, 1)
                    .amplifier(Affinity.EARTH, -1)
                    .duration(Affinity.ALWAYS, 25)
            ))
            .put(Effects.LUCK, new BrewData(Effects.UNLUCK, 29, 20 * 60 * 20, Items.TWISTING_VINES, AffinityTable.builder()
                    .amplifier(Affinity.TREASURE, 1)
                    .amplifier(Affinity.TRASH, -1)
                    .duration(Affinity.MINE, 1)
                    .duration(Affinity.CRAFT, -1)
                    .both(Affinity.CAVE, -0.5, 0.5)
                    .both(Affinity.LANDSCAPE, 0.5, -0.5)
            ))
            .put(Effects.UNLUCK, new BrewData(Effects.LUCK, 19, 20 * 60 * 10, Items.WEEPING_VINES, AffinityTable.builder()
                    .amplifier(Affinity.TREASURE, -1)
                    .amplifier(Affinity.TRASH, 1)
                    .duration(Affinity.MINE, -1)
                    .duration(Affinity.CRAFT, 1)
                    .both(Affinity.CAVE, 0.5, -0.5)
                    .both(Affinity.LANDSCAPE, -0.5, 0.5)
            ))
            .put(Effects.SLOW_FALLING, new BrewData(null, 0, 20 * 60 * 5, Items.PHANTOM_MEMBRANE, AffinityTable.builder()
                    .amplifier(Affinity.AIR, -1)
                    .amplifier(Affinity.EARTH, 1)
                    .duration(Affinity.COLD, 1)
                    .duration(Affinity.HOT, -1)
                    .both(Affinity.DRY, -0.5, 0.5)
                    .both(Affinity.WET, 0.5, -0.5)
            ))
            .put(Effects.CONDUIT_POWER, new BrewData(null, 0, 20 * 60 * 5, Items.PRISMARINE_CRYSTALS, AffinityTable.builder()
                    .duration(Affinity.WATER, 1)
                    .duration(Affinity.EARTH, -1)
                    .duration(Affinity.WET, -0.5)
                    .duration(Affinity.DRY, 0.5)
            ))
            .put(Effects.DOLPHINS_GRACE, new BrewData(null, 0, 20 * 60 * 20, Items.TROPICAL_FISH, AffinityTable.builder()
                    .duration(Affinity.WATER, 1)
                    .duration(Affinity.AIR, -1)
                    .duration(Affinity.WET, 0.5)
                    .duration(Affinity.DRY, -0.5)
            ))
            .put(Effects.BAD_OMEN, new BrewData(Effects.HERO_OF_THE_VILLAGE, 5, 20 * 60 * 60 * 4, Items.EMERALD, AffinityTable.builder()
                    .amplifier(Affinity.TRASH, 1)
                    .amplifier(Affinity.TREASURE, -1)
                    .duration(Affinity.WEAK, 1)
                    .duration(Affinity.POWER, -1)
                    .both(Affinity.END, 0.5, -0.5)
                    .both(Affinity.HELL, -0.5, 0.5)
            ))
            .put(Effects.HERO_OF_THE_VILLAGE, new BrewData(Effects.BAD_OMEN, 0, 20 * 60 * 60 * 4, Items.TOTEM_OF_UNDYING, AffinityTable.builder()
                    .duration(Affinity.TRASH, 1)
                    .duration(Affinity.TREASURE, -1)
                    .duration(Affinity.WEAK, 0.5)
                    .duration(Affinity.POWER, 0.5)
            ))
            .put(ModEffects.mobDizziness, new BrewData(null, 9, 20 * 60 * 3, Items.ENDER_EYE, AffinityTable.builder()
                    .amplifier(Affinity.DARK, 1)
                    .amplifier(Affinity.LIGHT, -1)
                    .duration(Affinity.END, 1)
                    .duration(Affinity.HELL, -1)
                    .both(Affinity.FIRE, 0.5, -0.5)
                    .both(Affinity.WATER, -0.5, 0.5)
            ))
            .put(ModEffects.ghost, new BrewData(null, 0, 20 * 60 * 5, Items.SCAFFOLDING, AffinityTable.builder()
                    .duration(Affinity.AIR, 1)
                    .duration(Affinity.EARTH, -1)
                    .duration(Affinity.END, -0.5)
                    .duration(Affinity.HELL, 0.5)
            ))
            .put(ModEffects.confusion, new BrewData(null, 0, 20 * 60 * 3, Items.NETHERITE_SCRAP, AffinityTable.builder()
                    .duration(Affinity.EARTH, 1)
                    .duration(Affinity.AIR, -1)
                    .duration(Affinity.HELL, -0.5)
                    .duration(Affinity.END, 0.5)
            ))
            .put(ModEffects.golemAggression, new BrewData(null, 9, 20 * 60 * 10, Items.CARVED_PUMPKIN, AffinityTable.builder()
                    .amplifier(Affinity.TREASURE, -1)
                    .amplifier(Affinity.TRASH, 1)
                    .duration(Affinity.HELL, -1)
                    .duration(Affinity.END, 1)
                    .both(Affinity.COLD, -0.5, 0.5)
                    .both(Affinity.HOT, 0.5, -0.5)
            ))
            .put(ModEffects.randomTeleport, new BrewData(null ,9, 20 * 60 * 3, Items.ENDER_PEARL, AffinityTable.builder()
                    .amplifier(Affinity.END, 1)
                    .amplifier(Affinity.WATER, -1)
                    .duration(Affinity.FIRE, 1)
                    .duration(Affinity.HELL, -1)
            ))
            .put(ModEffects.projectileResistance, new BrewData(ModEffects.deadlyAura, 0, 20 * 60 * 10, Items.SHIELD, AffinityTable.builder()
                    .duration(Affinity.POWER, -0.75)
                    .duration(Affinity.WEAK, 0.75)
                    .duration(Affinity.DARK, -0.75)
                    .duration(Affinity.LIGHT, -0.75)
                    .both(Affinity.AIR, -0.25, 0.25)
                    .both(Affinity.EARTH, 0.25, -0.25)
            ))
            .put(ModEffects.deadlyAura, new BrewData(ModEffects.projectileResistance, 9, 20 * 60 * 5, Items.SPECTRAL_ARROW, AffinityTable.builder()
                    .amplifier(Affinity.POWER, 1)
                    .amplifier(Affinity.WEAK, -1)
                    .duration(Affinity.DARK, 1)
                    .duration(Affinity.LIGHT, -1)
                    .both(Affinity.AIR, 0.5, -0.5)
                    .both(Affinity.EARTH, -0.5, 0.5)
            ))
            .put(ModEffects.knockbackResistance, new BrewData(ModEffects.knockbackBoost, 9, 20 * 60 * 20, Items.BAMBOO, AffinityTable.builder()
                    .amplifier(Affinity.CAVE, 1)
                    .amplifier(Affinity.LANDSCAPE, -1)
                    .duration(Affinity.WEAK, 1)
                    .duration(Affinity.POWER, -1)
                    .both(Affinity.CRAFT, 0.5, -0.5)
                    .both(Affinity.MINE, -0.5, 0.5)
            ))
            .put(ModEffects.knockbackBoost, new BrewData(ModEffects.knockbackResistance, 9, 20 * 60 * 10, Items.CACTUS, AffinityTable.builder()
                    .amplifier(Affinity.CAVE, -1)
                    .amplifier(Affinity.LANDSCAPE, 1)
                    .duration(Affinity.WEAK, -1)
                    .duration(Affinity.POWER, 1)
                    .both(Affinity.CRAFT, -0.5, 0.5)
                    .both(Affinity.MINE, 0.5, -0.5)
            ))
            .put(ModEffects.xpBoost, new BrewData(null, 9, 20 * 60 * 20, Items.QUARTZ, AffinityTable.builder()
                    .amplifier(Affinity.TREASURE, 1)
                    .amplifier(Affinity.TRASH, -1)
                    .duration(Affinity.HELL, 1)
                    .duration(Affinity.END, -1)
                    .both(Affinity.COLD, 0.5, -0.5)
                    .both(Affinity.HOT, -0.5, 0.5)
            ))
            .build();

    public static final Set<Item> FOCI = ImmutableSet.of(
            Items.REDSTONE,
            Items.GLOWSTONE_DUST,
            Items.GUNPOWDER,
            Items.DRAGON_BREATH
    );

    @SuppressWarnings("UnstableApiUsage")
    public static final Set<Item> EFFECT_ITEMS = EFFECT_DATA.values().stream().map(data -> data.item).collect(ImmutableSet.toImmutableSet());

    public static ItemStack getPotion(List<ItemStack> inputs) {
        List<Item> items = inputs.stream().map(ItemStack::getItem).collect(Collectors.toList());

        Item potionItem = Items.POTION;
        PotionAlignment alignment = PotionAlignment.NORMAL;
        for (Item item : items.stream().filter(FOCI::contains).collect(Collectors.toList())) {
            if (item == Items.GUNPOWDER && potionItem != Items.LINGERING_POTION) {
                potionItem = Items.SPLASH_POTION;
            } else if (item == Items.DRAGON_BREATH) {
                potionItem = Items.LINGERING_POTION;
            } else if (item == Items.REDSTONE) {
                alignment = PotionAlignment.LONG;
            } else if (item == Items.GLOWSTONE_DUST) {
                alignment = PotionAlignment.STRONG;
            }
        }

        List<EffectInstance> effects = new ArrayList<>();

        Effect current = null;
        BrewData data = null;
        double duration = 0;
        double amplifier = 0;
        double affinityMultiplier = 1;

        for (Item item : items.stream().filter(item -> !FOCI.contains(item)).collect(Collectors.toList())) {
            if (EFFECT_ITEMS.contains(item)) {
                if (current != null) {
                    effects.add(alignment.finishEffect(current, data, duration, amplifier));
                    current = null;
                }
                current = EFFECT_DATA.entrySet().stream().filter(entry -> entry.getValue().item == item)
                        .findFirst().map(Map.Entry::getKey).orElse(null);
                data = EFFECT_DATA.get(current);
                duration = data == null ? 0 : Math.max(40, data.maxDuration / 20d);
                amplifier = data == null ? 0 : Math.min(2, Math.floor((data.maxLevel - 1) / 2d));
                affinityMultiplier = 1;
            } else if (ItemBrew.EFFECT_DATA.containsKey(item) && current != null && data != null) {
                ItemAffinities affinites = ItemBrew.EFFECT_DATA.get(item);

                amplifier += (affinityMultiplier * data.affinities.amplifier(affinites.affinity1) * affinites.power * data.maxLevel) / ItemAffinities.MAX_POWER;
                duration += (affinityMultiplier * data.affinities.duration(affinites.affinity1) * affinites.power * data.maxDuration) / ItemAffinities.MAX_POWER;
                if (affinites.affinity2 != null) {
                    amplifier += (affinityMultiplier * 0.7 * data.affinities.amplifier(affinites.affinity2) * affinites.power * data.maxLevel) / ItemAffinities.MAX_POWER;
                    duration += (affinityMultiplier * 0.7 * data.affinities.duration(affinites.affinity2) * affinites.power * data.maxDuration) / ItemAffinities.MAX_POWER;
                }
                if (affinites.affinity3 != null) {
                    amplifier += (affinityMultiplier * 0.4 * data.affinities.amplifier(affinites.affinity3) * affinites.power * data.maxLevel) / ItemAffinities.MAX_POWER;
                    duration += (affinityMultiplier * 0.4 * data.affinities.duration(affinites.affinity3) * affinites.power * data.maxDuration) / ItemAffinities.MAX_POWER;
                }

                affinityMultiplier = affinityMultiplier * 2 / 3d;
            }
        }
        if (current != null) {
            effects.add(alignment.finishEffect(current, data, duration, amplifier));
            current = null;
        }

        ItemStack potion = new ItemStack(potionItem);
        if (effects.isEmpty()) {
            return PotionUtils.addPotionToItemStack(potion, Potions.AWKWARD);
        } else {
            int color = PotionUtils.getPotionColorFromEffectList(effects);
            //noinspection ConstantConditions
            potion.setDisplayName(new TranslationTextComponent("item.pumpkinpotions." + potionItem.getRegistryName().getPath()).mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.GREEN));
            CompoundNBT nbt = potion.getOrCreateTag();
            nbt.putInt("CustomPotionColor", color);
            potion.setTag(nbt);
            return PotionUtils.appendEffects(potion, effects);
        }
    }

    public static boolean canAccept(ItemStack stack, List<ItemStack> items) {
        if (stack.getItem() == Items.NETHER_WART) {
            return false;
        } else if (FOCI.contains(stack.getItem())) {
            if (hasItem(stack.getItem(), items)) {
                return false;
            } else if (stack.getItem() == Items.DRAGON_BREATH) {
                return hasItem(Items.GUNPOWDER, items);
            } else if (stack.getItem() == Items.REDSTONE) {
                return !hasItem(Items.GLOWSTONE_DUST, items);
            } else if (stack.getItem() == Items.GLOWSTONE_DUST) {
                return !hasItem(Items.REDSTONE, items);
            } else {
                return true;
            }
        } else if ((EFFECT_ITEMS.contains(stack.getItem()))
                || (ItemBrew.EFFECT_DATA.containsKey(stack.getItem())
                    && items.stream().map(ItemStack::getItem).anyMatch(EFFECT_ITEMS::contains))) {
            return !hasItem(stack.getItem(), items);
        } else {
            return false;
        }
    }

    public static void applyColors(ItemStack stack, ColorData colors) {
        EFFECT_DATA.entrySet().stream().filter(entry -> entry.getValue().item == stack.getItem()).findFirst().ifPresent(entry -> colors.applyColor(entry.getKey().getLiquidColor()));
    }

    private static boolean hasItem(Item item, List<ItemStack> items) {
        return items.stream().map(ItemStack::getItem).anyMatch(elem -> elem == item);
    }
}

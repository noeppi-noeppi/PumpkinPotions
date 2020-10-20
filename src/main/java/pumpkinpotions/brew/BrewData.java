package pumpkinpotions.brew;

import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class BrewData {

    @Nullable
    public final Effect opposite;
    public final int maxLevel;
    public final int maxDuration;
    public final Item item;
    public final AffinityTable affinities;

    public BrewData(@Nullable Effect oppsite, int maxLevel, int maxDuration, Item item, AffinityTable.Builder affinities) {
        this(oppsite, maxLevel, maxDuration, item, affinities.build());
    }

    public BrewData(@Nullable Effect oppsite, int maxLevel, int maxDuration, Item item, AffinityTable affinities) {
        this.opposite = oppsite;
        this.maxLevel = maxLevel;
        this.maxDuration = maxDuration;
        this.item = item;
        this.affinities = affinities;
    }

    public int clampAmplifier(int amplifier) {
        if (affinities.amplifier(Affinity.ALWAYS) != 0) {
            return MathHelper.clamp((int) Math.round(affinities.amplifier(Affinity.ALWAYS)), 0, maxLevel);
        } else {
            return MathHelper.clamp(amplifier, 0, maxLevel);
        }
    }

    public int clampDuration(int duration) {
        if (affinities.duration(Affinity.ALWAYS) != 0) {
            return MathHelper.clamp((int) Math.round(affinities.duration(Affinity.ALWAYS)), 20, maxDuration);
        } else {
            return MathHelper.clamp(duration, 20, maxDuration);
        }
    }
}

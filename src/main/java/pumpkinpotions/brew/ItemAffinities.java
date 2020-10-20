package pumpkinpotions.brew;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemAffinities {

    public static final double MAX_POWER = 70;

    public final double power;
    @Nonnull
    public final Affinity affinity1;
    @Nullable
    public final Affinity affinity2;
    @Nullable
    public final Affinity affinity3;

    private ItemAffinities(double power, @Nonnull Affinity affinity1, @Nullable Affinity affinity2, @Nullable Affinity affinity3) {
        this.power = power;
        this.affinity1 = affinity1;
        this.affinity2 = affinity2;
        this.affinity3 = affinity3;
    }

    public static ItemAffinities insane(Affinity... affinities) {
        return create(50, affinities);
    }

    public static ItemAffinities ultraStrong(Affinity... affinities) {
        return create(30, affinities);
    }

    public static ItemAffinities veryStrong(Affinity... affinities) {
        return create(25, affinities);
    }

    public static ItemAffinities strong(Affinity... affinities) {
        return create(20, affinities);
    }

    public static ItemAffinities decent(Affinity... affinities) {
        return create(15, affinities);
    }

    public static ItemAffinities normal(Affinity... affinities) {
        return create(10, affinities);
    }

    public static ItemAffinities weak(Affinity... affinities) {
        return create(5, affinities);
    }

    /*public static ItemAffinities veryWeak(Affinity... affinities) {
        return create(3, affinities);
    }

    public static ItemAffinities negligible(Affinity... affinities) {
        return create(1, affinities);
    }*/

    private static ItemAffinities create(double power, Affinity[] affinities) {
        if (affinities.length < 1 || affinities.length > 3) {
            throw new IllegalArgumentException("Invalid amount of affinities given");
        } else if (affinities.length == 1) {
            return new ItemAffinities(power, affinities[0], null, null);
        } else if (affinities.length == 2) {
            return new ItemAffinities(power, affinities[0], affinities[1], null);
        } else {
            return new ItemAffinities(power, affinities[0], affinities[1], affinities[2]);
        }
    }
}

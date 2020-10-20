package pumpkinpotions.brew;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class AffinityTable {

    public static final AffinityTable EMPTY = builder().build();

    private final Map<Affinity, Double> amplifier;
    private final Map<Affinity, Double> duration;

    private AffinityTable(ImmutableMap<Affinity, Double> amplifier, ImmutableMap<Affinity, Double> duration) {
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public double amplifier(Affinity affinity) {
        return amplifier.getOrDefault(affinity, 0d);
    }

    public double duration(Affinity affinity) {
        return duration.getOrDefault(affinity, 0d);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<Affinity, Double> amplifier;
        private final Map<Affinity, Double> duration;

        public Builder() {
            this.amplifier = new HashMap<>();
            this.duration = new HashMap<>();
        }

        public Builder amplifier(Affinity affinity, double modifier) {
            this.amplifier.put(affinity, modifier);
            return this;
        }

        public Builder duration(Affinity affinity, double modifier) {
            this.duration.put(affinity, modifier);
            return this;
        }

        public Builder both(Affinity affinity, double amplifier, double duration) {
            this.amplifier.put(affinity, amplifier);
            this.duration.put(affinity, duration);
            return this;
        }

        public AffinityTable build() {
            return new AffinityTable(ImmutableMap.copyOf(amplifier), ImmutableMap.copyOf(duration));
        }
    }
}

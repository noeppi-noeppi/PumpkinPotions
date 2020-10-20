package pumpkinpotions.brew;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public enum PotionAlignment {

    NORMAL() {
        @Override
        public EffectInstance finishEffect(Effect effect, BrewData data, double duration, double amplifier) {
            return new EffectInstance(effect,
                    data.clampDuration((int) Math.round(duration)),
                    data.clampAmplifier((int) Math.round(amplifier)));
        }
    },
    LONG() {
        @Override
        public EffectInstance finishEffect(Effect effect, BrewData data, double duration, double amplifier) {
            return new EffectInstance(effect,
                    data.clampDuration((int) Math.round(duration * 1.4)),
                    data.clampAmplifier((int) Math.round(amplifier - 1)));
        }
    },
    STRONG() {
        @Override
        public EffectInstance finishEffect(Effect effect, BrewData data, double duration, double amplifier) {
            return new EffectInstance(effect,
                    data.clampDuration((int) Math.round(duration * 0.7)),
                    data.clampAmplifier((int) Math.round(amplifier + 1)));
        }
    };

    public abstract EffectInstance finishEffect(Effect effect, BrewData data, double duration, double amplifier);
}

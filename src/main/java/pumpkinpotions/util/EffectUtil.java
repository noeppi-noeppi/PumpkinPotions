package pumpkinpotions.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import java.util.function.BiConsumer;

public class EffectUtil {

    public static void whenEffect(LivingEntity player, Effect effect, BiConsumer<EffectInstance, Integer> action) {
        EffectInstance effectInstance = player.getActivePotionEffect(effect);
        if (effectInstance != null) {
            action.accept(effectInstance, effectInstance.getAmplifier());
        }
    }
}

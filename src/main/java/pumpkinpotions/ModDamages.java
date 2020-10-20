package pumpkinpotions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import pumpkinpotions.util.IndirectDamageSource;
import pumpkinpotions.util.ModDamgeSource;

public class ModDamages {

    public static final DamageSource aura = new ModDamgeSource("aura_damage");

    public static DamageSource createAuraDamage(LivingEntity source) {
        return new IndirectDamageSource(aura, source);
    }
}

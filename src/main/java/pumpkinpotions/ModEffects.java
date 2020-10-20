package pumpkinpotions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import pumpkinpotions.effect.DeadlyAuraEffect;
import pumpkinpotions.effect.GhostEffect;

public class ModEffects {

    public static final Effect mobDizziness = new Effect(EffectType.HARMFUL, 0x4CCB3D);
    public static final Effect ghost = new GhostEffect(EffectType.NEUTRAL, 0xFFFFFF);
    public static final Effect confusion = new Effect(EffectType.HARMFUL, 0x111111);
    public static final Effect golemAggression = new Effect(EffectType.HARMFUL, 0xE3901D);
    public static final Effect randomTeleport = new Effect(EffectType.HARMFUL, 0x2CCDB1);
    public static final Effect projectileResistance = new Effect(EffectType.BENEFICIAL, 0x896727);
    public static final Effect deadlyAura = new DeadlyAuraEffect(EffectType.BENEFICIAL, 0x764857);
    public static final Effect knockbackResistance = new Effect(EffectType.BENEFICIAL, 0xBBBBBB).addAttributesModifier(Attributes.KNOCKBACK_RESISTANCE, "07b55a49-3dcb-45e6-9874-b0a920fef4d5", 0.1, AttributeModifier.Operation.ADDITION);
    public static final Effect knockbackBoost = new Effect(EffectType.BENEFICIAL, 0xBBBBBB).addAttributesModifier(Attributes.ATTACK_KNOCKBACK, "062e5315-5efd-4f47-97eb-a76d85ad3e9a", 0.1, AttributeModifier.Operation.ADDITION);
    public static final Effect xpBoost = new Effect(EffectType.BENEFICIAL, 0XD0E45A);

    public static void register() {
        PumpkinPotions.register("mob_dizziness", mobDizziness); // Max level: 10
        PumpkinPotions.register("ghost", ghost); // Max level: 1
        PumpkinPotions.register("confusion", confusion); // Max level: 1
        PumpkinPotions.register("golem_aggression", golemAggression); // Max level: 10
        PumpkinPotions.register("random_teleport", randomTeleport); // Max level: 10
        PumpkinPotions.register("projectile_resistance", projectileResistance); // Max level: 1
        PumpkinPotions.register("deadly_aura", deadlyAura); // Max level: 10
        PumpkinPotions.register("knockback_resistance", knockbackResistance); // Max level: 10
        PumpkinPotions.register("knockback_boost", knockbackBoost); // Max level: 10
        PumpkinPotions.register("xp_boost", xpBoost); // Max level: 10
    }
}

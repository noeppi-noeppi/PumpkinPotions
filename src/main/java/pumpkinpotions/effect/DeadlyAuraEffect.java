package pumpkinpotions.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class DeadlyAuraEffect extends Effect {

    public DeadlyAuraEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void applyAttributesModifiersToEntity(@Nonnull LivingEntity entity, @Nonnull AttributeModifierManager attributes, int amplifier) {
        super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
        if (entity.getEntityWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) entity.getEntityWorld();
            world.getPlayers().forEach(player -> {
                EffectInstance effect = entity.getActivePotionEffect(this);
                if (effect != null) {
                    player.connection.sendPacket(new SPlayEntityEffectPacket(entity.getEntityId(), effect));
                }
            });
        }
    }
}

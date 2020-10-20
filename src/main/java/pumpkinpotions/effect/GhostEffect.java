package pumpkinpotions.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

public class GhostEffect extends Effect {

    public GhostEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void applyAttributesModifiersToEntity(@Nonnull LivingEntity entity, @Nonnull AttributeModifierManager attributes, int amplifier) {
        super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
        if (!entity.getEntityWorld().isRemote && entity instanceof PlayerEntity) {
            updateEffect(true, (PlayerEntity) entity);
            ((PlayerEntity) entity).sendPlayerAbilities();
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(@Nonnull LivingEntity entity, @Nonnull AttributeModifierManager attributes, int amplifier) {
        super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
        if (!entity.getEntityWorld().isRemote && entity instanceof PlayerEntity) {
            updateEffect(false, (PlayerEntity) entity);
            ((PlayerEntity) entity).abilities.isFlying = false;
            ((PlayerEntity) entity).sendPlayerAbilities();
        }
    }

    public static void updateEffect(boolean effectActive, PlayerEntity player) {
        boolean before = player.abilities.allowFlying;
        if (effectActive) {
            player.abilities.allowFlying = true;
            player.abilities.setFlySpeed(0.01f);
        } else {
            player.abilities.allowFlying = player.isCreative();
            player.abilities.setFlySpeed(0.05f);
        }
        if (before != player.abilities.allowFlying) {
            player.sendPlayerAbilities();
        }
    }
}

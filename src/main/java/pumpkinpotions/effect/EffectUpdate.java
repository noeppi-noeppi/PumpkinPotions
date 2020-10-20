package pumpkinpotions.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class EffectUpdate {

    public static void randomTeleport(LivingEntity entity, float chance) {
        Random random = entity.getRNG();
        if (random.nextFloat() < chance) {
            double x = entity.getPosX();
            double y = entity.getPosY();
            double z = entity.getPosZ();

            for(int i = 0; i < 16; ++i) {
                double d3 = x + (random.nextDouble() - 0.5) * 16;
                double d4 = MathHelper.clamp(y + (random.nextInt(16) - 8), 0.0D, entity.world.func_234938_ad_() - 1);
                double d5 = z + (random.nextDouble() - 0.5) * 16;
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                if (entity.attemptTeleport(d3, d4, d5, true)) {
                    SoundEvent soundevent = entity instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    entity.world.playSound(null, x, y, z, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entity.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }
}

package pumpkinpotions.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import pumpkinpotions.PumpkinPotions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModDamgeSource extends DamageSource {

    public ModDamgeSource(String damageTypeIn) {
        super(damageTypeIn);
    }

    @Nonnull
    @Override
    public ITextComponent getDeathMessage(@Nonnull LivingEntity entity) {
        return getDeathMessage(entity, entity.getAttackingEntity());
    }

    @Nonnull
    public ITextComponent getDeathMessage(LivingEntity entity, @Nullable Entity trueSource) {
        if (trueSource == null) {
            return new TranslationTextComponent("death." + PumpkinPotions.MODID + "." + damageType + ".self", entity.getDisplayName());
        } else {
            return new TranslationTextComponent("death." + PumpkinPotions.MODID + "." + damageType + ".other", entity.getDisplayName(), trueSource.getDisplayName());
        }
    }
}

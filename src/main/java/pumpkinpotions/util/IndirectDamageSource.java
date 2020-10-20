package pumpkinpotions.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IndirectDamageSource extends DamageSource {

    private final DamageSource parent;
    private final Entity directSource;
    private final Entity trueSource;

    public IndirectDamageSource(DamageSource parent, Entity source) {
        this(parent, source, source);
    }

    public IndirectDamageSource(DamageSource parent, Entity directSource, Entity trueSource) {
        super(parent.getDamageType());
        this.parent = parent;
        this.directSource = directSource;
        this.trueSource = trueSource;
    }

    @Override
    public boolean isProjectile() {
        return parent.isProjectile();
    }

    @Override
    public boolean isExplosion() {
        return parent.isExplosion();
    }

    @Override
    public boolean isUnblockable() {
        return parent.isUnblockable();
    }

    @Override
    public float getHungerDamage() {
        return parent.getHungerDamage();
    }

    @Override
    public boolean canHarmInCreative() {
        return parent.canHarmInCreative();
    }

    @Override
    public boolean isDamageAbsolute() {
        return parent.isDamageAbsolute();
    }

    @Nonnull
    @Override
    public Entity getImmediateSource() {
        return directSource;
    }

    @Nonnull
    @Override
    public Entity getTrueSource() {
        return trueSource;
    }

    @Nonnull
    @Override
    public ITextComponent getDeathMessage(@Nonnull LivingEntity entity) {
        if (parent instanceof ModDamgeSource) {
            return ((ModDamgeSource) parent).getDeathMessage(entity, trueSource);
        } else {
            return parent.getDeathMessage(entity);
        }
    }

    @Override
    public boolean isFireDamage() {
        return parent.isFireDamage();
    }

    @Nonnull
    @Override
    public String getDamageType() {
        return parent.getDamageType();
    }

    @Override
    public boolean isDifficultyScaled() {
        return parent.isDifficultyScaled();
    }

    @Override
    public boolean isMagicDamage() {
        return parent.isMagicDamage();
    }

    @Nullable
    @Override
    public Vector3d getDamageLocation() {
        return directSource.getPositionVec();
    }
}

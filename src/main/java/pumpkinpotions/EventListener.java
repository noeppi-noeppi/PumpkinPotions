package pumpkinpotions;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pumpkinpotions.effect.EffectUpdate;
import pumpkinpotions.effect.GhostEffect;
import pumpkinpotions.render.RuneRender;
import pumpkinpotions.util.EffectUtil;

import java.util.Random;

public class EventListener {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void renderEntityPre(RenderLivingEvent.Pre<?, ?> event) {
        MatrixStack matrixStack = event.getMatrixStack();
        IRenderTypeBuffer buffer = event.getBuffers();

        //noinspection ConstantConditions
        EffectUtil.whenEffect(Minecraft.getInstance().player, ModEffects.mobDizziness, (effect, level) -> {
            Random random = new Random(event.getEntity().getUniqueID().getLeastSignificantBits());
            double translateX = (random.nextDouble() - 0.5) * (level + 1) / 2;
            double translateZ = (random.nextDouble() - 0.5) * (level + 1) / 2;
            event.getMatrixStack().translate(translateX, 0, translateZ);
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void renderEntityPost(RenderLivingEvent.Post<?, ?> event) {
        MatrixStack matrixStack = event.getMatrixStack();
        IRenderTypeBuffer buffer = event.getBuffers();

        EffectUtil.whenEffect(event.getEntity(), ModEffects.deadlyAura, (effect, level) -> RuneRender.renderRunes(event.getEntity(), matrixStack, buffer, Minecraft.getInstance(), event.getLight(), OverlayTexture.NO_OVERLAY, event.getPartialRenderTick(), Math.min(40 + (10 * level), 256)));
    }

    @SubscribeEvent
    public void interact(PlayerInteractEvent event) {
        if (event.getPlayer().isPotionActive(ModEffects.ghost)) {
            if (event.isCancelable())
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (!player.world.isRemote && player.ticksExisted % 10 == 0) {
            GhostEffect.updateEffect(player.isPotionActive(ModEffects.ghost), player);

            EffectUtil.whenEffect(player, ModEffects.golemAggression, (effect, level) -> {
                int radius = 2 * (level + 1);
                player.getEntityWorld().getEntitiesWithinAABB(GolemEntity.class, new AxisAlignedBB(player.getPosX() - radius, player.getPosY() - radius, player.getPosZ() - radius, player.getPosX() + radius, player.getPosY() + radius, player.getPosZ() + radius)).forEach(golem -> golem.setAttackTarget(player));
            });
        }
    }

    @SubscribeEvent
    public void livingTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (!living.world.isRemote && living.ticksExisted % 10 == 0) {
            EffectUtil.whenEffect(living, ModEffects.randomTeleport, (effect, level) -> EffectUpdate.randomTeleport(living, (level + 1) / 20f));

            EffectUtil.whenEffect(living, ModEffects.deadlyAura, (effect, level) -> {
                DamageSource damage = ModDamages.createAuraDamage(living);
                living.getEntityWorld().getEntitiesWithinAABBExcludingEntity(living, new AxisAlignedBB(living.getPosX() - 2, living.getPosY() - 2, living.getPosZ() - 2, living.getPosX() + 2, living.getPosY() + 2, living.getPosZ() + 2)).forEach(entity -> {
                    if (entity instanceof LivingEntity && (!(entity instanceof TameableEntity) || !((TameableEntity) entity).isOwner(living)))
                        entity.attackEntityFrom(damage, (level + 1) / 2f);
                });
            });
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void inputUpdate(InputUpdateEvent event) {
        //noinspection ConstantConditions
        if (Minecraft.getInstance().player.isPotionActive(ModEffects.confusion)) {
            MovementInput mi = event.getMovementInput();
            boolean w = mi.forwardKeyDown;
            boolean a = mi.leftKeyDown;
            boolean shift = mi.sneaking;
            mi.forwardKeyDown = mi.backKeyDown;
            mi.leftKeyDown = mi.rightKeyDown;
            mi.sneaking = mi.jump;
            mi.backKeyDown = w;
            mi.rightKeyDown = a;
            mi.jump = shift;
            mi.moveForward = -mi.moveForward;
            mi.moveStrafe = -mi.moveStrafe;
        }
    }

    @SubscribeEvent
    public void damage(LivingAttackEvent event) {
        if (event.getEntityLiving().isPotionActive(ModEffects.projectileResistance) && event.getSource().isProjectile()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void xpPickup(PlayerXpEvent.PickupXp event) {
        EffectUtil.whenEffect(event.getPlayer(), ModEffects.xpBoost, (effect, level) -> {
            double factor = 1 + ((level + 1) / 3d);
            event.getOrb().xpValue = (int) Math.round(event.getOrb().xpValue * factor);
        });
    }

    @SubscribeEvent
    public void itemCrafter(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() == ModBlocks.cauldron.asItem() && event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            IWorldInfo info = player.getServerWorld().getWorldInfo();
            if (info instanceof ServerWorldInfo) {
                ((ServerWorldInfo) info).setDayTime(18000);
            }
        }
    }
}

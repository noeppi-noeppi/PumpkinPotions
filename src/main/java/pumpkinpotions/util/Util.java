package pumpkinpotions.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class Util {

    public static void playSound(World world, @Nullable PlayerEntity player, SoundEvent sound, SoundCategory category, BlockPos pos, float volume, float pitch) {
        if (player != null) {
            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).connection.sendPacket(new SPlaySoundEffectPacket(sound, category, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5f, 1));
            }
        } else {
            if (world instanceof ServerWorld) {
                world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, category, volume, pitch);
            }
        }
    }

    public static void addParticle(World world, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int color) {
        if (world.isRemote) {
            ClientUtil.addParticle(world, particleData, x, y, z, xSpeed, ySpeed, zSpeed, color);
        }
    }
}

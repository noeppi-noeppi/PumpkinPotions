package pumpkinpotions.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.World;

public class ClientUtil {

    public static void addParticle(World theWorld, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int color) {
        ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        if (renderInfo.isValid() && Minecraft.getInstance().particles != null) {
            Particle particle = Minecraft.getInstance().particles.makeParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null) {
                particle.setColor(((color >>> 16) & 0xFF) / 255f, ((color >>> 8) & 0xFF) / 255f, (color & 0xFF) / 255f);
                Minecraft.getInstance().particles.addEffect(particle);
            }
        }
    }
}
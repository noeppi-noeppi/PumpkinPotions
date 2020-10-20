package pumpkinpotions.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Pair;
import pumpkinpotions.PumpkinPotions;

import java.util.List;
import java.util.Random;

public class RuneRender {

    public static final ResourceLocation RUNES_TEX = new ResourceLocation(PumpkinPotions.MODID, "textures/runes.png");
    public static final List<Pair<Float, Float>> TEX_COORDINATES = ImmutableList.of(
            Pair.of(0f, 0f),
            Pair.of(0.25f, 0f),
            Pair.of(0.5f, 0f),
            Pair.of(0.75f, 0f),
            Pair.of(0f, 0.25f),
            Pair.of(0.25f, 0.25f)
    );
    public static final float RUNE_SIZE = 0.25f;

    public static void renderRunes(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, Minecraft mc, int light, int overlay, float partialTicks, int runes) {
        Random random = new Random(entity.getUniqueID().getMostSignificantBits());
        double width = entity.getWidth();
        double radius = (width / 2) + 0.2;
        for (int i = 0; i < runes; i++) {
            double y = random.nextDouble() * entity.getHeight();
            float angleAdd = 360 * random.nextFloat();
            float spinSpeed = (0.5f + random.nextFloat()) * (random.nextBoolean() ? 1 : -1);
            float size = (random.nextFloat() * 0.1f) + 0.08f;
            double distance = (1 + (random.nextDouble() * 0.4)) * radius;

            matrixStack.push();
            matrixStack.rotate(Vector3f.YP.rotationDegrees(angleAdd + ((entity.ticksExisted + partialTicks) * spinSpeed)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
            matrixStack.translate(0, -y, distance);

            Pair<Float, Float> coordinates = TEX_COORDINATES.get(random.nextInt(TEX_COORDINATES.size()));

            mc.getTextureManager().bindTexture(RUNES_TEX);

            Matrix4f model = matrixStack.getLast().getMatrix();
            Matrix3f normal = matrixStack.getLast().getNormal();
            BufferBuilder vertex = Tessellator.getInstance().getBuffer();
            vertex.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertex.pos(model, -0.1f, 0.1f, 0).color(1, 1, 1, 1).tex(coordinates.getLeft(), coordinates.getRight() + RUNE_SIZE).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.pos(model, 0.1f, 0.1f, 0).color(1, 1, 1, 1).tex(coordinates.getLeft() + RUNE_SIZE, coordinates.getRight() + RUNE_SIZE).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.pos(model, 0.1f, -0.1f, 0).color(1, 1, 1, 1).tex(coordinates.getLeft() + RUNE_SIZE, coordinates.getRight()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.pos(model, -0.1f, -0.1f, 0).color(1, 1, 1, 1).tex(coordinates.getLeft(), coordinates.getRight()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.finishDrawing();

            //noinspection deprecation
            RenderSystem.enableAlphaTest();
            WorldVertexBufferUploader.draw(vertex);

            matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
            matrixStack.scale(-1, 1, 1);

            model = matrixStack.getLast().getMatrix();
            normal = matrixStack.getLast().getNormal();
            vertex = Tessellator.getInstance().getBuffer();
            vertex.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertex.pos(model, -size, size, 0).color(1, 1, 1, 1).tex(coordinates.getLeft(), coordinates.getRight() + RUNE_SIZE).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.pos(model, size, size, 0).color(1, 1, 1, 1).tex(coordinates.getLeft() + RUNE_SIZE, coordinates.getRight() + RUNE_SIZE).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.pos(model, size, -size, 0).color(1, 1, 1, 1).tex(coordinates.getLeft() + RUNE_SIZE, coordinates.getRight()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.pos(model, -size, -size, 0).color(1, 1, 1, 1).tex(coordinates.getLeft(), coordinates.getRight()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
            vertex.finishDrawing();

            //noinspection deprecation
            RenderSystem.enableAlphaTest();
            WorldVertexBufferUploader.draw(vertex);

            matrixStack.pop();
        }
    }
}

package pumpkinpotions.cauldron;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Triple;
import pumpkinpotions.PumpkinPotions;
import pumpkinpotions.book.BookHandler;
import pumpkinpotions.util.RenderUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class RenderCauldron extends TileEntityRenderer<TileCauldron> {

    public static final int TICKS_PER_QUARTER = 100;
    public static final RenderType LECTERN_EFFECT = RenderType.getEntityTranslucent(new ResourceLocation(PumpkinPotions.MODID, "textures/lectern_effect.png"));

    public RenderCauldron(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull TileCauldron tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
        if (tile.getFill().full) {
            matrixStack.push();
            matrixStack.translate(0, 12.75 / 16d, 0);
            renderPotion(matrixStack, buffer, tile.colorData.getColor1(), tile.colorData.getColor2(), tile.colorData.getColor3(), tile.colorData.getColor4(), partialTicks, light);
            matrixStack.pop();
        }
        for (Direction dir : Direction.values()) {
            if (dir.getYOffset() == 0) {
                BlockPos pos = tile.getPos().toImmutable().offset(dir);
                @SuppressWarnings("ConstantConditions")
                BlockState state = tile.getWorld().getBlockState(pos);
                if (state.getBlock() == Blocks.LECTERN) {
                    TileEntity lectern = tile.getWorld().getTileEntity(pos);
                    if (lectern instanceof LecternTileEntity) {
                        matrixStack.push();
                        matrixStack.translate(dir.getXOffset(), 0, dir.getZOffset());
                        renderLectern(matrixStack, buffer, tile, (LecternTileEntity) lectern, partialTicks, light);
                        matrixStack.pop();
                    }
                }
            }
        }
    }

    private void renderPotion(MatrixStack matrixStack, IRenderTypeBuffer buffer, int color1, int color2, int color3, int color4, float partialTicks, int light) {
        @SuppressWarnings("ConstantConditions")
        float ticks = Minecraft.getInstance().player.ticksExisted + partialTicks;

        float partQ = (ticks % TICKS_PER_QUARTER) / TICKS_PER_QUARTER;
        Triple<Float, Float, Float> b12 = RenderUtil.blendRGB(color1, color2, partQ);
        Triple<Float, Float, Float> b23 = RenderUtil.blendRGB(color2, color3, partQ);
        Triple<Float, Float, Float> b34 = RenderUtil.blendRGB(color3, color4, partQ);
        Triple<Float, Float, Float> b41 = RenderUtil.blendRGB(color4, color1, partQ);

        int direction = Minecraft.getInstance().player.ticksExisted % (TICKS_PER_QUARTER * 4) / TICKS_PER_QUARTER;
        if (direction > 0) {
            for (int i = 0; i < direction; i++) {
                Triple<Float, Float, Float> tmp = b12;
                b12 = b23;
                b23 = b34;
                b34 = b41;
                b41 = tmp;
            }
        }

        float alpha = 1 - (((ticks % 40) / 40) * 0.05f);
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(Fluids.WATER.getAttributes().getStillTexture());

        Matrix4f model = matrixStack.getLast().getMatrix();
        Matrix3f normal = matrixStack.getLast().getNormal();

        IVertexBuilder vertex = buffer.getBuffer(RenderType.getTranslucentNoCrumbling());
        vertex.pos(model, 2/16f, 0, 14/16f).color(b12.getLeft(), b12.getMiddle(), b12.getRight(), alpha).tex(sprite.getInterpolatedU(0), sprite.getInterpolatedV(16)).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 14/16f, 0, 14/16f).color(b23.getLeft(), b23.getMiddle(), b23.getRight(), alpha).tex(sprite.getInterpolatedU(16), sprite.getInterpolatedV(16)).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 14/16f, 0, 2/16f).color(b34.getLeft(), b34.getMiddle(), b34.getRight(), alpha).tex(sprite.getInterpolatedU(16), sprite.getInterpolatedV(0)).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 2/16f, 0, 2/16f).color(b41.getLeft(), b41.getMiddle(), b41.getRight(), alpha).tex(sprite.getInterpolatedU(0), sprite.getInterpolatedV(0)).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
    }

    private void renderLectern(MatrixStack matrixStack, IRenderTypeBuffer buffer, TileCauldron tile, LecternTileEntity lectern, float partialTicks, int light) {
        //noinspection ConstantConditions
        if (!tile.getFill().full || !lectern.getWorld().getBlockState(lectern.getPos()).get(BlockStateProperties.HAS_BOOK)) {
            return;
        }
        ItemStack book = lectern.getBook();
        if (book.isEmpty()) {
            return;
        }
        List<ItemStack> bookStacks = BookHandler.getStacks(book);
        if (bookStacks.isEmpty()) {
            return;
        }
        if (tile.getFill() == TileCauldron.FillState.WATER) {
            renderLecternItem(new ItemStack(Items.NETHER_WART), matrixStack, buffer, partialTicks, light, tile.getPickupCooldown() / (float) TileCauldron.MAX_PICKUP_COOLDOWN);
        } else {
            List<ItemStack> stacks = tile.getPotionItems();
            if (stacks.size() > bookStacks.size()) {
                renderLecternItem(new ItemStack(Items.BARRIER), matrixStack, buffer, partialTicks, light, 0);
            } else {
                for (int i = 0; i < stacks.size(); i++) {
                    if (stacks.get(i).getItem() != bookStacks.get(i).getItem()) {
                        renderLecternItem(new ItemStack(Items.BARRIER), matrixStack, buffer, partialTicks, light, 0);
                        return;
                    }
                }
                if (bookStacks.size() > stacks.size()) {
                    renderLecternItem(bookStacks.get(stacks.size()), matrixStack, buffer, partialTicks, light, tile.getPickupCooldown() / (float) TileCauldron.MAX_PICKUP_COOLDOWN);
                }
            }
        }
        //renderLecternGlint(matrixStack, buffer, partialTicks, light, 1);
        //renderLecternItem(new ItemStack(Items.NETHER_WART), matrixStack, buffer, partialTicks, light, 1);
    }

    private void renderLecternGlint(MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, int light, float open) {
        open = 1 - open;

        @SuppressWarnings("ConstantConditions")
        float ticks = Minecraft.getInstance().player.ticksExisted + partialTicks;

        float alpha = 0.3f + (0.4f * (float) Math.abs(Math.sin(ticks / 20)));

        matrixStack.push();
        matrixStack.translate(0.5, 0.75 + (open * 0.5), 0.5);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-ticks));

        Matrix4f model = matrixStack.getLast().getMatrix();
        Matrix3f normal = matrixStack.getLast().getNormal();

        IVertexBuilder vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.5f, open * 0.4f, open * -0.125f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.5f, open * 0.4f, open * 0.125f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.5f, open * 0.4f, open * -0.125f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.5f, open * 0.4f, open * 0.125f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.125f, open * 0.4f, open * 0.5f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.125f, open * 0.4f, open * 0.5f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.125f, open * 0.4f, open * -0.5f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.125f, open * 0.4f, open * -0.5f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.5f, open * 0.4f, open * 0.125f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.125f, open * 0.4f, open * 0.5f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.5f, open * 0.4f, open * -0.125f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * 0.125f, open * 0.4f, open * -0.5f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.5f, open * 0.4f, open * 0.125f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.125f, open * 0.4f, open * 0.5f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        vertex = buffer.getBuffer(LECTERN_EFFECT);
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(0, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, 0, 0, 0).color(1f, 1f, 1f, alpha).tex(1, 0).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.5f, open * 0.4f, open * -0.125f).color(1f, 1f, 1f, alpha).tex(1, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();
        vertex.pos(model, open * -0.125f, open * 0.4f, open * -0.5f).color(1f, 1f, 1f, alpha).tex(0, 1).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(normal, 1, 0, 0).endVertex();

        matrixStack.pop();
    }

    private void renderLecternItem(ItemStack item, MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, int light, float open) {
        open = 1 - open;

        @SuppressWarnings("ConstantConditions")
        float ticks = Minecraft.getInstance().player.ticksExisted + partialTicks;

        matrixStack.push();
        matrixStack.translate(0.5,  1.25 + (open * 0.2), 0.5);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(ticks));
        matrixStack.scale(1.2f, 1.2f, 1.2f);
        matrixStack.scale(open, open, open);

        Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);

        matrixStack.pop();
    }

    @Override
    public boolean isGlobalRenderer(@Nonnull TileCauldron tile) {
        return true;
    }
}

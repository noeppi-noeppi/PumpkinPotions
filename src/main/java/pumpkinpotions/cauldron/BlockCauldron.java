package pumpkinpotions.cauldron;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import pumpkinpotions.base.BlockTE;
import pumpkinpotions.util.DirectionShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCauldron extends BlockTE<TileCauldron> {

    public static final DirectionShape SHAPE = new DirectionShape(VoxelShapes.or(
            makeCuboidShape(1, 5, 3, 2, 13, 13),
            makeCuboidShape(2, 5, 11, 3, 6, 13),
            makeCuboidShape(2, 13, 3, 3, 14, 4),
            makeCuboidShape(1, 13, 4, 2, 14, 12),
            makeCuboidShape(2, 14, 3, 3, 15, 5),
            makeCuboidShape(1, 14, 5, 2, 15, 11),
            makeCuboidShape(2, 13, 12, 3, 14, 13),
            makeCuboidShape(2, 14, 11, 3, 15, 13),
            makeCuboidShape(13, 5, 11, 14, 6, 13),
            makeCuboidShape(14, 5, 3, 15, 13, 13),
            makeCuboidShape(13, 5, 3, 14, 6, 5),
            makeCuboidShape(14, 13, 4, 15, 14, 12),
            makeCuboidShape(14, 14, 5, 15, 15, 11),
            makeCuboidShape(13, 13, 12, 14, 14, 13),
            makeCuboidShape(13, 14, 11, 14, 15, 13),
            makeCuboidShape(13, 13, 3, 14, 14, 4),
            makeCuboidShape(13, 14, 3, 14, 15, 5),
            makeCuboidShape(11, 5, 2, 13, 6, 3),
            makeCuboidShape(3, 5, 1, 13, 13, 2),
            makeCuboidShape(3, 5, 2, 5, 6, 3),
            makeCuboidShape(12, 13, 2, 13, 14, 3),
            makeCuboidShape(4, 13, 1, 12, 14, 2),
            makeCuboidShape(11, 14, 2, 13, 15, 3),
            makeCuboidShape(5, 14, 1, 11, 15, 2),
            makeCuboidShape(3, 13, 2, 4, 14, 3),
            makeCuboidShape(3, 14, 2, 5, 15, 3),
            makeCuboidShape(11, 5, 13, 13, 6, 14),
            makeCuboidShape(3, 5, 14, 13, 13, 15),
            makeCuboidShape(3, 5, 13, 5, 6, 14),
            makeCuboidShape(12, 13, 13, 13, 14, 14),
            makeCuboidShape(4, 13, 14, 12, 14, 15),
            makeCuboidShape(11, 14, 13, 13, 15, 14),
            makeCuboidShape(5, 14, 14, 11, 15, 15),
            makeCuboidShape(3, 13, 13, 4, 14, 14),
            makeCuboidShape(3, 14, 13, 5, 15, 14),
            makeCuboidShape(2, 6, 2, 3, 13, 3),
            makeCuboidShape(13, 6, 2, 14, 13, 3),
            makeCuboidShape(2, 6, 13, 3, 13, 14),
            makeCuboidShape(13, 6, 13, 14, 13, 14),
            makeCuboidShape(12, 4, 11, 13, 5, 12),
            makeCuboidShape(3, 3, 5, 4, 4, 11),
            makeCuboidShape(2, 4, 5, 3, 5, 11),
            makeCuboidShape(12, 3, 5, 13, 4, 11),
            makeCuboidShape(13, 4, 5, 14, 5, 11),
            makeCuboidShape(4, 3, 4, 12, 4, 12),
            makeCuboidShape(5, 3, 3, 11, 4, 4),
            makeCuboidShape(5, 4, 2, 11, 5, 3),
            makeCuboidShape(5, 3, 12, 11, 4, 13),
            makeCuboidShape(5, 4, 13, 11, 5, 14),
            makeCuboidShape(11, 4, 3, 13, 5, 4),
            makeCuboidShape(3, 4, 3, 5, 5, 4),
            makeCuboidShape(3, 4, 12, 5, 5, 13),
            makeCuboidShape(11, 4, 12, 13, 5, 13),
            makeCuboidShape(12, 4, 4, 13, 5, 5),
            makeCuboidShape(3, 4, 4, 4, 5, 5),
            makeCuboidShape(3, 4, 11, 4, 5, 12),
            makeCuboidShape(0, 0, 11, 16, 3, 14),
            makeCuboidShape(0, 0, 2, 16, 3, 5),
            makeCuboidShape(5, 15, 0, 11, 16, 1),
            makeCuboidShape(5, 15, 15, 11, 16, 16),
            makeCuboidShape(15, 15, 5, 16, 16, 11),
            makeCuboidShape(0, 15, 5, 1, 16, 11),
            makeCuboidShape(14, 15, 3, 15, 16, 13),
            makeCuboidShape(1, 15, 3, 2, 16, 13),
            makeCuboidShape(13, 15, 10, 14, 16, 13),
            makeCuboidShape(2, 15, 10, 3, 16, 13),
            makeCuboidShape(13, 15, 3, 14, 16, 6),
            makeCuboidShape(2, 15, 3, 3, 16, 6),
            makeCuboidShape(3, 15, 1, 13, 16, 2),
            makeCuboidShape(3, 15, 14, 13, 16, 15),
            makeCuboidShape(10, 15, 2, 14, 16, 3),
            makeCuboidShape(10, 15, 13, 14, 16, 14),
            makeCuboidShape(12, 15, 3, 13, 16, 4),
            makeCuboidShape(12, 15, 12, 13, 16, 13),
            makeCuboidShape(2, 15, 2, 6, 16, 3),
            makeCuboidShape(2, 15, 13, 6, 16, 14),
            makeCuboidShape(3, 15, 3, 4, 16, 4),
            makeCuboidShape(3, 15, 12, 4, 16, 13)
    ));

    public BlockCauldron(Properties properties) {
        super(TileCauldron.class, properties);
    }

    @Override
    public void registerClient(String id) {
        super.registerClient(id);
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
        ClientRegistry.bindTileEntityRenderer(getTileType(), RenderCauldron::new);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite().rotateY());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SHAPE.getShape(state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        TileCauldron tile = getTile(world, pos);
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() != 1) {
            return ActionResultType.PASS;
        }
        ActionResult<ItemStack> result = tile.handleItemClick(player, stack);
        if (result.getResult() != stack) {
            player.setHeldItem(hand, result.getResult());
        }
        return result.getType();
    }
}

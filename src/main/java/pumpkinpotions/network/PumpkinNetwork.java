package pumpkinpotions.network;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import pumpkinpotions.PumpkinPotions;
import pumpkinpotions.cauldron.TileCauldron;

import java.util.Optional;

public class PumpkinNetwork {

    private static final String PROTOCOL_VERSION = "1";
    private static int discriminator = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PumpkinPotions.MODID, "netchannel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        register(new TeUpdateHandler(), NetworkDirection.PLAY_TO_CLIENT);

        register(new TeRequestHandler(), NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T> void register(NetworkHandler<T> handler, NetworkDirection direction) {
        INSTANCE.registerMessage(discriminator++, handler.messageClass(), handler::encode, handler::decode, handler::handle, Optional.of(direction));
    }

    public static void updateTE(World world, BlockPos pos) {
        if (!world.isRemote) {
            updateTE(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), world, pos, false);
        }
    }

    public static void updateTE(World world, BlockPos pos, boolean vanilla) {
        if (!world.isRemote) {
            updateTE(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), world, pos, vanilla);
        }
    }

    static void updateTE(PacketDistributor.PacketTarget target, World world, BlockPos pos, boolean vanilla) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te == null)
                return;

            if (te instanceof TileCauldron) {
                for (Direction dir : Direction.values()) {
                    if (dir.getYOffset() == 0) {
                        BlockPos newPos = pos.toImmutable().offset(dir);
                        if (world.getBlockState(newPos).getBlock() == Blocks.LECTERN) {
                            updateTE(target, world, newPos, true);
                        }
                    }
                }
            }

            CompoundNBT nbt;
            if (vanilla) {
                nbt = te.write(new CompoundNBT());
            } else {
                nbt = te.getUpdateTag();
            }
            //noinspection ConstantConditions
            if (nbt == null)
                return;
            ResourceLocation id = te.getType().getRegistryName();
            if (id == null)
                return;
            PumpkinNetwork.INSTANCE.send(target,
                    new TeUpdateHandler.TeUpdateMessage(world.func_234923_W_().getRegistryName(), pos, id, nbt));
        }
    }

    public static void requestTE(World world, BlockPos pos) {
        if (world.isRemote) {
            PumpkinNetwork.INSTANCE.sendToServer(new TeRequestHandler.TeRequestMessage(world.func_234923_W_().getRegistryName(), pos));
        }
    }
}

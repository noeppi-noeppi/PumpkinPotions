package pumpkinpotions.network;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import pumpkinpotions.PumpkinPotions;
import pumpkinpotions.cauldron.TileCauldron;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PumpkinNetwork {

    private static final String PROTOCOL_VERSION = "2";
    private static int discriminator = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PumpkinPotions.MODID, "netchannel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        register(new TeUpdateSerializer(), () -> TeUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
        register(new TeRequestSerializer(), () -> TeRequestHandler::handle, NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T> void register(PacketSerializer<T> serializer, Supplier<BiConsumer<T, Supplier<NetworkEvent.Context>>> handler, NetworkDirection direction) {
        Objects.requireNonNull(direction);
        BiConsumer<T, Supplier<NetworkEvent.Context>> realHandler;
        if (direction == NetworkDirection.PLAY_TO_CLIENT || direction == NetworkDirection.LOGIN_TO_CLIENT) {
            realHandler = DistExecutor.unsafeRunForDist(() -> handler, () -> () -> (msg, ctx) -> {});
        } else {
            realHandler = handler.get();
        }
        INSTANCE.registerMessage(discriminator++, serializer.messageClass(), serializer::encode, serializer::decode, realHandler, Optional.of(direction));

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
                    new TeUpdateSerializer.TeUpdateMessage(world.func_234923_W_().getRegistryName(), pos, id, nbt));
        }
    }

    public static void requestTE(World world, BlockPos pos) {
        if (world.isRemote) {
            PumpkinNetwork.INSTANCE.sendToServer(new TeRequestSerializer.TeRequestMessage(world.func_234923_W_().getRegistryName(), pos));
        }
    }
}

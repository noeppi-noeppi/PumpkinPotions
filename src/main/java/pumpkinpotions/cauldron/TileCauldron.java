package pumpkinpotions.cauldron;

import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import pumpkinpotions.base.TileEntityBase;
import pumpkinpotions.book.BookHandler;
import pumpkinpotions.brew.Brew;
import pumpkinpotions.util.Util;

import javax.annotation.Nonnull;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TileCauldron extends TileEntityBase implements ITickableTileEntity {

    public static final int MAX_PICKUP_COOLDOWN = 40;

    private int pickupCooldown = 0;
    private FillState fill = FillState.EMPTY;
    private final List<ItemStack> potionItems = new ArrayList<>();
    public final ColorData colorData = new ColorData();

    public TileCauldron(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (world != null && pos != null) {
            if (world.isRemote && fill == FillState.POTION) {
                double x = pos.getX() + 0.2;
                double y = pos.getY() + 0.8;
                double z = pos.getZ() + 0.2;
                Util.addParticle(world, ParticleTypes.ENTITY_EFFECT,
                        x + (world.rand.nextDouble() * 0.6),
                        y,
                        z + (world.rand.nextDouble() * 0.6),
                        0, 0.1, 0,
                        colorData.getRandomColor(world.rand));
            }
            if (pickupCooldown > 0) {
                pickupCooldown -= 1;
                markDirty();
            } else if (!world.isRemote && fill.full) {
                List<ItemEntity> itemsOnGround = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
                for (ItemEntity entity : itemsOnGround) {
                    if (entity.pickupDelay < 5) {
                        ItemStack stack = entity.getItem();
                        if (stack.getCount() >= 1) {
                            if (fill.full && stack.getItem() == Items.BONE && stack.getDisplayName().getUnformattedComponentText().equalsIgnoreCase("Oh Spooky")) {
                                stack.shrink(1);
                                entity.setItem(stack);
                                ZonedDateTime zdt = ZonedDateTime.now();
                                boolean halloween = zdt.getDayOfMonth() == 31 && zdt.getMonth() == Month.OCTOBER;
                                for (int i = 0; i < (halloween ? 50 : 1); i++) {
                                    BatEntity bat = EntityType.BAT.create(world);
                                    if (bat != null) {
                                        bat.setLocationAndAngles(pos.getX() + 0.4, pos.getY() + 1.4, pos.getZ() + 0.5, 0, 0);
                                        if (halloween) {
                                            bat.setCustomName(new TranslationTextComponent("pumpkinpotions.itshalloween"));
                                            bat.setCustomNameVisible(true);
                                            bat.enablePersistence();
                                        }
                                        world.addEntity(bat);
                                        bat.setPositionAndUpdate(pos.getX() + 0.4, pos.getY() + 1.4, pos.getZ() + 0.5);
                                    }
                                }
                            } else if (fill == FillState.WATER) {
                                if (stack.getItem() == Items.NETHER_WART) {
                                    stack.shrink(1);
                                    entity.setItem(stack);
                                    fill = FillState.POTION;
                                    colorData.reset();
                                    pickupCooldown = MAX_PICKUP_COOLDOWN;
                                    markDirty();
                                    Util.playSound(world, null, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, pos, 1, 1);
                                    break;
                                }
                            } else {
                                if (Brew.canAccept(stack, potionItems)) {
                                    ItemStack newStack = stack.split(1);
                                    entity.setItem(stack);
                                    potionItems.add(newStack);
                                    Brew.applyColors(newStack, colorData);
                                    pickupCooldown = MAX_PICKUP_COOLDOWN;
                                    markDirty();
                                    Util.playSound(world, null, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, pos, 1, 1);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ActionResult<ItemStack> handleItemClick(PlayerEntity player, ItemStack stack) {
        if (stack.getItem() == Items.GLASS_BOTTLE && fill == FillState.POTION) {
            ItemStack potion = Brew.getPotion(potionItems);
            if (!potion.isEmpty()) {
                fill = FillState.EMPTY;
                colorData.reset();
                pickupCooldown = 0;
                markDirty();
                Util.playSound(world, player, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, pos, 1, 1);
                return ActionResult.resultSuccess(potion);
            }
        }

        if (stack.getItem() == Items.BOOK && fill == FillState.EMPTY && !potionItems.isEmpty()) {
            Util.playSound(world, player, SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.BLOCKS, pos, 2, 1);
            return ActionResult.resultSuccess(BookHandler.getBook(player, potionItems));
        }

        Optional<IFluidHandlerItem> fluidHandlerCap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve();
        if (fluidHandlerCap.isPresent()) {
            IFluidHandlerItem fluidHandler = fluidHandlerCap.get();
            if (fill.full) {
                int filled = fluidHandler.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
                if (filled >= FluidAttributes.BUCKET_VOLUME) {
                    if (!player.isCreative())
                        fluidHandler.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                    fill = FillState.EMPTY;
                    potionItems.clear();
                    colorData.reset();
                    pickupCooldown = 0;
                    markDirty();
                    Util.playSound(world, player, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, pos, 1, 1);
                    return ActionResult.resultSuccess(fluidHandler.getContainer());
                }
            } else {
                int drained = fluidHandler.drain(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE).getAmount();
                if (drained >= FluidAttributes.BUCKET_VOLUME) {
                    if (!player.isCreative())
                        fluidHandler.drain(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                    fill = FillState.WATER;
                    potionItems.clear();
                    colorData.reset();
                    pickupCooldown = MAX_PICKUP_COOLDOWN;
                    markDirty();
                    Util.playSound(world, player, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, pos, 1, 1);
                    return ActionResult.resultSuccess(fluidHandler.getContainer());
                }

            }
            return ActionResult.resultConsume(stack);
        }
        return ActionResult.resultPass(stack);
    }

    public List<ItemStack> getPotionItems() {
        return Collections.unmodifiableList(potionItems);
    }

    public FillState getFill() {
        return fill;
    }

    public int getPickupCooldown() {
        return pickupCooldown;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);

        if (nbt.contains("fill", Constants.NBT.TAG_STRING)) {
            try {
                fill = FillState.valueOf(nbt.getString("fill"));
            } catch (IllegalArgumentException e) {
                fill = FillState.EMPTY;
            }
        } else {
            fill = FillState.EMPTY;
        }

        potionItems.clear();
        if (nbt.contains("items", Constants.NBT.TAG_LIST)) {
            ListNBT items = nbt.getList("items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < items.size(); i++) {
                potionItems.add(ItemStack.read(items.getCompound(i)));
            }
        }

        colorData.deserializeNBT(nbt.getCompound("colors"));
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.putString("fill", fill.name());

        ListNBT items = new ListNBT();
        for (ItemStack stack : potionItems) {
            items.add(stack.write(new CompoundNBT()));
        }
        nbt.put("items", items);

        nbt.put("colors", colorData.serializeNBT());

        return super.write(nbt);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("fill", Constants.NBT.TAG_STRING)) {
            try {
                fill = FillState.valueOf(nbt.getString("fill"));
            } catch (IllegalArgumentException e) {
                fill = FillState.EMPTY;
            }
        } else {
            fill = FillState.EMPTY;
        }

        potionItems.clear();
        if (nbt.contains("items", Constants.NBT.TAG_LIST)) {
            ListNBT items = nbt.getList("items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < items.size(); i++) {
                potionItems.add(ItemStack.read(items.getCompound(i)));
            }
        }

        colorData.deserializeNBT(nbt.getCompound("colors"));
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();

        nbt.putString("fill", fill.name());

        ListNBT items = new ListNBT();
        for (ItemStack stack : potionItems) {
            items.add(stack.write(new CompoundNBT()));
        }
        nbt.put("items", items);

        nbt.put("colors", colorData.serializeNBT());

        return nbt;
    }

    public enum FillState {
        EMPTY(false),
        WATER(true),
        POTION(true);

        public final boolean full;

        FillState(boolean full) {
            this.full = full;
        }
    }
}

package pumpkinpotions.book;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookHandler {

    public static ItemStack getBook(PlayerEntity player, List<ItemStack> stacks) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);

        book.setTagInfo("author", StringNBT.valueOf(player.getName().getString()));
        book.setTagInfo("title", StringNBT.valueOf("Potion Recipe"));

        List<IFormattableTextComponent> pagesText = new ArrayList<>();
        addStack(pagesText, new ItemStack(Items.NETHER_WART));
        for (ItemStack stack : stacks) {
            addStack(pagesText, stack);
        }

        ListNBT pages = new ListNBT();
        IFormattableTextComponent current = null;
        for (int i = 0; i < pagesText.size(); i++) {
            if (current == null) {
                current = pagesText.get(i);
            } else {
                current.append(new StringTextComponent("\n")).append(pagesText.get(i));
            }
            if (i % 10 == 9) {
                String json = ITextComponent.Serializer.toJson(current);
                pages.add(StringNBT.valueOf(json));
                current = null;
            }
        }
        if (current != null) {
            String json = ITextComponent.Serializer.toJson(current);
            pages.add(StringNBT.valueOf(json));
        }
        book.setTagInfo("pages", pages);

        ListNBT recipeItems = new ListNBT();
        for (ItemStack stack : stacks) {
            recipeItems.add(stack.write(new CompoundNBT()));
        }
        book.setTagInfo("potionRecipeItems", recipeItems);

        return book;
    }

    private static void addStack(List<IFormattableTextComponent> pagesText, ItemStack stack) {
        ITextComponent tc = stack.getDisplayName();
        if (tc instanceof IFormattableTextComponent) {
            pagesText.add(tc.deepCopy());
        } else {
            pagesText.add(new StringTextComponent("").append(tc.deepCopy()));
        }
    }

    public static List<ItemStack> getStacks(ItemStack book) {
        CompoundNBT nbt = book.getTag();
        if (nbt == null) {
            return Collections.emptyList();
        }
        if (!nbt.contains("potionRecipeItems", Constants.NBT.TAG_LIST)) {
            return Collections.emptyList();
        }
        ListNBT recipeItems = nbt.getList("potionRecipeItems", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < recipeItems.size(); i++) {
            stacks.add(ItemStack.read(recipeItems.getCompound(i)));
        }
        return Collections.unmodifiableList(stacks);
    }
}

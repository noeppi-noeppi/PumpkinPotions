package pumpkinpotions.cauldron;

import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class ColorData implements INBTSerializable<CompoundNBT> {

    private int color1 = -1;
    private int color2 = -1;
    private int color3 = -1;
    private int color4 = -1;
    private int nextReplace = 0;

    public void applyColor(int color) {
        if (color1 < 0) {
            color1 = color;
            color3 = color;
        } else if (color2 < 0) {
            color2 = color;
            color4 = color;
            nextReplace = 0;
        } else {
            switch (nextReplace) {
                case 0:
                    color1 = color;
                    break;
                case 1:
                    color2 = color;
                    break;
                case 2:
                    color3 = color;
                    break;
                case 3:
                    color4 = color;
                    break;
            }
            nextReplace = (nextReplace + 1) % 4;
        }
    }

    public int getColor1() {
        if (color1 < 0) {
            return Fluids.WATER.getAttributes().getColor();
        } else {
            return color1;
        }
    }

    public int getColor2() {
        if (color2 < 0) {
            return Fluids.WATER.getAttributes().getColor();
        } else {
            return color2;
        }
    }

    public int getColor3() {
        if (color3 < 0) {
            return Fluids.WATER.getAttributes().getColor();
        } else {
            return color3;
        }
    }

    public int getColor4() {
        if (color4 < 0) {
            return Fluids.WATER.getAttributes().getColor();
        } else {
            return color4;
        }
    }

    public void reset() {
        color1 = -1;
        color2 = -1;
        color3 = -1;
        color4 = -1;
        nextReplace = 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("color1", color1);
        nbt.putInt("color2", color2);
        nbt.putInt("color3", color3);
        nbt.putInt("color4", color4);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        color1 = nbt.getInt("color1");
        color2 = nbt.getInt("color2");
        color3 = nbt.getInt("color3");
        color4 = nbt.getInt("color4");
    }

    public int getRandomColor(Random rand) {
        switch (rand.nextInt(4)) {
            case 0:
                return getColor1();
            case 1:
                return getColor2();
            case 2:
                return getColor3();
            default:
                return getColor4();
        }
    }
}

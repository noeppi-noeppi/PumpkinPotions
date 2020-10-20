package pumpkinpotions.util;

import org.apache.commons.lang3.tuple.Triple;

public class RenderUtil {

    public static Triple<Float, Float, Float> blendRGB(int rgb1, int rgb2, float spread) {
        float r1 = ((rgb1 >>> 16) & 0xFF) / 255f;
        float g1 = ((rgb1 >>> 8) & 0xFF) / 255f;
        float b1 = (rgb1 & 0xFF) / 255f;

        float r2 = ((rgb2 >>> 16) & 0xFF) / 255f;
        float g2 = ((rgb2 >>> 8) & 0xFF) / 255f;
        float b2 = (rgb2 & 0xFF) / 255f;

        return Triple.of(r1 * (1-spread) + r2 * spread,
                g1 * (1-spread) + g2 * spread,
                b1 * (1-spread) + b2 * spread);
    }
}

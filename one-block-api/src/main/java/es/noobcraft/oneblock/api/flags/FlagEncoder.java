package es.noobcraft.oneblock.api.flags;

import java.util.Arrays;

public final class FlagEncoder {

    public static int encode(char[] parts) {
        return Integer.parseInt(Arrays.toString(parts),2);
    }

    public static char[] decode(int part) {
        return Integer.toBinaryString(part).toCharArray();
    }
}

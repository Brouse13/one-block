package es.noobcraft.oneblock.api.flags;

public final class FlagEncoder {

    public static int encode(char[] parts) {
        return Integer.parseInt(String.valueOf(parts),2);
    }

    public static char[] decode(int part) {
        if (part == -1) return Integer.toBinaryString(IslandFlags.allPerms()).toCharArray();
        return Integer.toBinaryString(part).toCharArray();
    }
}

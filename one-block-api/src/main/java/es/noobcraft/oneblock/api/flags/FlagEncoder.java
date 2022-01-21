package es.noobcraft.oneblock.api.flags;

import java.util.Arrays;

public final class FlagEncoder {

    public static int encode(char[] parts) {
        return Integer.parseInt(String.valueOf(parts),2);
    }

    public static char[] decode(int part) {
        if (part == -1) return Integer.toBinaryString(IslandFlag.allPerms()).toCharArray();
        return Integer.toBinaryString(part).toCharArray();
    }

    public static FlagType getType(IslandFlag flag, byte[] perms) {
        String bytes = perms[(flag.getPos()  * 2)] + perms[(flag.getPos() * 2) + 1] + "";

        return Arrays.stream(FlagType.values())
                .filter(flagType -> flagType.getValue() == Integer.parseInt(bytes, 2))
                .findFirst().orElse(null);
    }

    public static boolean getBool(IslandFlag flag, byte[] perms) {
        final FlagType flagType = getType(flag, perms);

        if (flagType == null) throw new NullPointerException("Can't get bytes perms from flag "+ flag.name());
        if (flagType.getValue() != 0 && flagType.getValue() != 1) throw new IllegalStateException("Bool flag("+ flag.name()+ ") can't have "+ flagType+ " as value");

        return flagType.getValue() != 0;
    }
}

package es.noobcraft.oneblock.api.flags;

import es.noobcraft.oneblock.api.exceptions.FlagException;

import java.util.Arrays;

public final class FlagEncoder {
    public static int encode(char[] parts) {
        StringBuilder bin = new StringBuilder();
        for (char part : parts)
            bin.append(Integer.toBinaryString(Integer.parseInt(part+ "", 10)));

        return Integer.parseInt(reverse(new StringBuilder(bin)), 2);
    }

    public static char[] decode(int perm) {
        StringBuilder tempBin = new StringBuilder(Integer.toBinaryString(perm));
        if (tempBin.length() % 2 != 0) tempBin.insert(0, "0");

        char[] reverseChar = reverse(new StringBuilder(tempBin)).toCharArray();

        char[] decoded = new char[15];
        Arrays.fill(decoded, '0');
        
        for (int i = 0; i < (reverseChar.length / 2) ; i++)
            decoded[i] = Character.forDigit(Integer.parseInt(reverseChar[i * 2]+ ""+ reverseChar[(i * 2) + 1], 2), 10);

        return decoded;
    }

    public static FlagType getType(IslandFlag flag, char[] perms) throws FlagException {
        if (perms.length < flag.getPos()) return FlagType.OWNER;
        try {
            char bytes = perms[flag.getPos()];

            return Arrays.stream(FlagType.values())
                    .filter(flagType -> flagType.getValue() == Integer.parseInt(String.valueOf(bytes)))
                    .findFirst().orElseThrow(FlagException::new);
        }catch (IndexOutOfBoundsException exception) {
            return FlagType.OWNER;
        }
    }

    public static int setType(IslandFlag flag, char[] perms, FlagType type) throws FlagException {
        if (perms.length < flag.getPos()) throw new FlagException("Unable to set value on pair "+ flag.getPos());
        perms[flag.getPos()] = Character.forDigit(Integer.parseInt(type.getValue()+ ""), 10);
        return encode(perms);
    }

    private static String reverse(StringBuilder bin) {
        if (bin.length() % 2 != 0) bin.insert(0, "0");

        char[] binary = bin.toString().toCharArray();
        StringBuilder reverse = new StringBuilder();

        for (int i = (binary.length / 2) - 1; i >= 0; i--) {
            reverse.append(binary[(i * 2)]);
            reverse.append(binary[(i *2) + 1]);
        }
        return reverse.toString();
    }
}

package es.noobcraft.oneblock.api.flags;

import java.util.BitSet;

public final class FlagEncoder {
    /**
     * Decode the int perm value into a BitSet
     * to represent all the perms ins a binary form.
     * @param value permission value
     * @return the BitSet perm
     */
    public static BitSet decode(int value) {
        BitSet bits = new BitSet(IslandFlag.values().length - 1);
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) bits.set(index);
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    /**
     * Encode a BitSet into a int to store the permission
     * into the database.
     * @param bits bits from the permission
     * @return the int perm
     */
    public static int encode(BitSet bits) {
        int value = 0;
        for (int i = 0; i < bits.length(); ++i)
            value += bits.get(i) ? (1L << i) : 0L;
        return value;
    }
}

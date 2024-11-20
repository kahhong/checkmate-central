package com.ila.checkmatecentral.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

    public static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }
    
    public static boolean isPowerOf2(int num) {
        /*
         * Power of 2 means a single bit is set.
         * Negating by 1 means the rest of the lower bits is set
         *
         * Example of 2^3
         *
         * 1000
         * 0111 &
         * ----
         * 0000
         *
         */
        
        if (num < 0) {
            throw new IllegalArgumentException("Require positive number. Given: " + num);
        }

        return (num & (num - 1)) == 0;
    }
}

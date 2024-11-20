package com.ila.checkmatecentral.unitTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ila.checkmatecentral.utility.MathUtils;

public class MathUtilsTest {
    
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 4, 8, 16})
    void testIsPowerOf2_isTrue(int n) {
        assertTrue(MathUtils.isPowerOf2(n));
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 5, 10})
    void testIsPowerOf2_isNotTrue(int n) {
        assertFalse(MathUtils.isPowerOf2(n));
    }

}

package org.pozi.photonium.util;

public class FastMath {
    private static final float[] SIN_TABLE = new float[65536];

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = (float)Math.sin((double)i * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }

    public static float sin(float value) {
        return SIN_TABLE[(int)(value * 10430.378F) & 65535];
    }

    public static float cos(float value) {
        return SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }

    public static float invSqrt(float x) {
        float xhalf = 0.5F * x;
        int i = Float.floatToIntBits(x);
        i = 1597463007 - (i >> 1);
        x = Float.intBitsToFloat(i);
        return x * (1.5F - xhalf * x * x);
    }
}
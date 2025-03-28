package com.example.buuktu.utils;

import android.content.Context;
import android.util.TypedValue;

import java.security.PublicKey;

public class DimensionsUtils {
    public static float convertDpToPx(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

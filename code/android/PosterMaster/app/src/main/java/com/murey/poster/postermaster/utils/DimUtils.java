package com.murey.poster.postermaster.utils;

import android.content.Context;

public class DimUtils {
    public static int dipToPixel(Context ctx, float dips) {
        return (int) (dips * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }
}

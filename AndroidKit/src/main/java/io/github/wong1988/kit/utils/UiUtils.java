package io.github.wong1988.kit.utils;

import android.util.TypedValue;

import io.github.wong1988.kit.AndroidKit;

public class UiUtils {

    /**
     * dp转像素
     */
    public static float dip2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AndroidKit.getInstance().getAppContext().getResources().getDisplayMetrics());
    }

}


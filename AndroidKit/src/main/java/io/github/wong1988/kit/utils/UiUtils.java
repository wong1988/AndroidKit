package io.github.wong1988.kit.utils;

import android.content.Context;
import android.util.TypedValue;

import io.github.wong1988.kit.AndroidKit;

public class UiUtils {

    /**
     * dp转像素
     * 注意：自定义View如果想实时预览请调用2参的方法
     */
    public static float dip2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AndroidKit.getInstance().getAppContext().getResources().getDisplayMetrics());
    }

    /**
     * dp转像素
     */
    public static float dip2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}


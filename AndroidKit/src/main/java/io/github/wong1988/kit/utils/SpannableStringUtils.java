package io.github.wong1988.kit.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class SpannableStringUtils {

    /**
     * 图片+文本 ： 文本过长会自动换行到图片下方（系统的换行后不会在图片下方）
     *
     * @param textView    文本控件
     * @param imgRes      左侧图片的资源id
     * @param drawPadding 图片距离文字的长度
     * @param text        设置的文本内容
     */
    public static void drawableLeft(TextView textView, @DrawableRes int imgRes, int drawPadding, String text) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append("替");
        builder.append(text);

        // 替换 预留位置
        builder.setSpan(new VerticalImageSpan(ContextCompat.getDrawable(textView.getContext(), imgRes), textView.getIncludeFontPadding(), drawPadding),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }

    // TODO 垂直居中的图片文字(暂不对外提供)
    private static class VerticalImageSpan extends ImageSpan {

        // 图片资源
        private final Drawable mDrawable;
        // 图片尺寸
        private final int mOriginWidth;
        private final int mOriginHeight;
        // 画图间距
        private final int mDrawPadding;

        // 是否有自带padding
        private final boolean mIncludeFontPadding;
        // 行高
        private int mLineHeight;

        VerticalImageSpan(Drawable drawable, boolean includeFontPadding, int drawPadding) {
            super(drawable);
            this.mDrawable = drawable;
            // eg: 华为手机 mate40e 1080 *2 376px
            // 图片 200 * 200 放到xxhdpi 获取到为 200 * 200
            // 图片不变放到 xhdpi 获取到为300 * 300
            this.mOriginWidth = mDrawable.getIntrinsicWidth();
            this.mOriginHeight = mDrawable.getIntrinsicHeight();
            this.mDrawPadding = drawPadding;
            this.mIncludeFontPadding = includeFontPadding;
        }


        /**
         * update the text line height
         */
        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {

            // 获取文本的实际高度
            Paint.FontMetricsInt metrics = paint.getFontMetricsInt();

            int canvasHeight;
            if (mIncludeFontPadding)
                canvasHeight = metrics.bottom - metrics.top;
            else
                canvasHeight = metrics.descent - metrics.ascent;

            mLineHeight = canvasHeight;

            int canvasWidth;

            if (canvasHeight >= mOriginHeight) {
                // 图片过小，以图片尺寸显示
                canvasHeight = mOriginHeight;
                canvasWidth = mOriginWidth;
            } else {
                // 图片过大，以最大高度进行显示
                canvasWidth = canvasHeight * mOriginWidth / mOriginHeight;
            }

            // 设置drawable范围(就是图片实际大小，让其画完整)
            mDrawable.setBounds(0, 0, canvasWidth, canvasHeight);
            // 返回图片占用的大小
            return mDrawable.getBounds().right + mDrawPadding;
        }

        /**
         * see detail message in android.text.TextLine
         *
         * @param canvas the canvas, can be null if not rendering
         * @param text   the text to be draw
         * @param start  the text start position
         * @param end    the text end position
         * @param x      the edge of the replacement closest to the leading margin
         * @param top    the top of the line
         * @param y      the baseline
         * @param bottom the bottom of the line
         * @param paint  the work paint
         */
        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {

            Drawable drawable = getDrawable();
            canvas.save();

            // 中心点
            float centerY = 1.0f * mLineHeight / 2;
            // 偏移
            float transY = centerY - 1.0f * (drawable.getBounds().bottom - drawable.getBounds().top) / 2;

            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

    }
}

package io.github.wong1988.kit.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class SpannableStringUtils {

    /**
     * 一般左侧无需设置padding
     * 上下padding一样则为居中效果，不设置则充满textview的高度（includeFontPadding = false时一行的高度）
     * 右侧padding比较常用，一般距离文字有一段距离
     */
    public static void drawLeft(TextView textView, @DrawableRes int imgRes, int drawPaddingTop, int drawPaddingRight, int drawPaddingBottom, String text) {
        drawLeft(textView, imgRes, 0, drawPaddingTop, drawPaddingRight, drawPaddingBottom, text);
    }

    /**
     * 图片+文本 ： 文本过长会自动换行到图片下方（系统的换行后不会在图片下方）
     *
     * @param textView          文本控件
     * @param imgRes            左侧图片的资源id
     * @param drawPaddingLeft   图片距离文字的左侧距离
     * @param drawPaddingTop    图片距离文字的顶部距离
     * @param drawPaddingRight  图片距离文字的右侧距离
     * @param drawPaddingBottom 图片距离文字的底部距离
     * @param text              设置的文本内容
     */
    public static void drawLeft(TextView textView, @DrawableRes int imgRes, int drawPaddingLeft, int drawPaddingTop, int drawPaddingRight, int drawPaddingBottom, String text) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append("替");
        builder.append(text);

        // 替换 预留位置
        builder.setSpan(new VerticalImageSpan(ContextCompat.getDrawable(textView.getContext(), imgRes), drawPaddingLeft, drawPaddingTop, drawPaddingRight, drawPaddingBottom),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }

    // TODO 垂直居中的图片文字(不对外提供)
    static class VerticalImageSpan extends ImageSpan {

        // 图片资源
        private final Drawable mDrawable;
        // 图片尺寸
        private final int mOriginWidth;
        private final int mOriginHeight;
        // 画图间距
        private int mDrawPaddingLeft;
        private int mDrawPaddingRight;
        private int mDrawPaddingTop;
        private int mDrawPaddingBottom;

        // 是否是小图
        private boolean mSmallImg;

        VerticalImageSpan(Drawable drawable, int drawPaddingLeft, int drawPaddingTop, int drawPaddingRight, int drawPaddingBottom) {
            super(drawable);
            this.mDrawable = drawable;
            this.mOriginWidth = mDrawable.getIntrinsicWidth();
            this.mOriginHeight = mDrawable.getIntrinsicHeight();
            this.mDrawPaddingLeft = drawPaddingLeft;
            this.mDrawPaddingRight = drawPaddingRight;
            this.mDrawPaddingTop = drawPaddingTop;
            this.mDrawPaddingBottom = drawPaddingBottom;

            if (mDrawPaddingTop < 0) {
                Log.e(StringUtils.class.getName(), "warn", new Throwable("drawPaddingTop小于0属性不会生效"));
                mDrawPaddingTop = 0;
            }
            if (mDrawPaddingBottom < 0) {
                Log.e(StringUtils.class.getName(), "warn", new Throwable("drawPaddingBottom小于0属性不会生效"));
                mDrawPaddingBottom = 0;
            }
            if (mDrawPaddingLeft < 0) {
                Log.e(StringUtils.class.getName(), "warn", new Throwable("drawPaddingLeft小于0属性不会生效"));
                mDrawPaddingLeft = 0;
            }
            if (mDrawPaddingRight < 0) {
                Log.e(StringUtils.class.getName(), "warn", new Throwable("drawPaddingRight小于0属性不会生效"));
                mDrawPaddingRight = 0;
            }
        }


        /**
         * update the text line height
         */
        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {

            // 获取文本的实际高度
            Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
            int canvasHeight = metrics.descent - metrics.ascent;

            if (mDrawPaddingTop + mDrawPaddingBottom >= canvasHeight) {
                Log.e(StringUtils.class.getName(), "warn", new Throwable("drawPaddingTop + drawPaddingBottom 大于 可绘制的高度属性不会生效"));
                mDrawPaddingTop = 0;
                mDrawPaddingBottom = 0;
            }

            // 最终的高度
            canvasHeight = canvasHeight - mDrawPaddingTop - mDrawPaddingBottom;

            int right = 0;

            if (canvasHeight >= mOriginHeight) {
                // 图片过小，以图片尺寸显示
                canvasHeight = mOriginHeight;
                right = mOriginWidth + mDrawPaddingLeft;
                mSmallImg = true;
            } else {
                // 图片过大，以最大高度进行显示
                right = canvasHeight * mOriginWidth / mOriginHeight + mDrawPaddingLeft;
                mSmallImg = false;
            }

            // 设置drawable范围
            mDrawable.setBounds(mDrawPaddingLeft, 0, right, canvasHeight);
            return mDrawable.getBounds().right + Math.max(mDrawPaddingRight, 0);
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

            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int centerY = y + fmPaint.descent - fontHeight / 2;

            if (mSmallImg) {
                // 小图垂直居中
                int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
                canvas.translate(x, transY);
                if (mDrawPaddingTop != 0 || mDrawPaddingBottom != 0)
                    Log.e(io.github.wong1988.kit.utils.StringUtils.class.getName(), "warn", new Throwable("当前图片过小，将强制垂直居中"));
            } else {
                int transY = centerY - fontHeight / 2 + mDrawPaddingTop;
                // 大图y轴偏移
                canvas.translate(x, transY);
            }

            drawable.draw(canvas);
            canvas.restore();
        }

    }
}

package io.github.wong1988.kit.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class SpannableStringUtils {

    /**
     * 图片 +文本
     * <效果1：drawableMargin = false>
     * 1. ⊙ 我是文字，我很长，只
     * 2. 能换行了
     * <效果2：drawableMargin = true>
     * 1. ⊙ 我是文字，我很长，只
     * 2.   能换行了
     * 注意：多行文本时，最好以中文开头，否则可能会触发 图片占用一行 后边都是空白，第二行才是文字
     *
     * @param textView       文本控件
     * @param imgRes         左侧图片的资源id
     * @param drawPadding    图片距离文字的长度
     * @param text           设置的文本内容
     * @param drawableMargin 多行时，第二行及以后的文本是在图片的正下方还是跳过图片才绘制文字
     */
    public static void drawableLeft(TextView textView, @DrawableRes int imgRes, int drawPadding, String text, boolean drawableMargin) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("替");
        builder.append(text);

        // 设置了 LeadingMarginSpan.Standard 就会自动多出padding , 即使设置了不包含padding，所以此处忽略字体padding属性
        // 而未设置则不忽略属性
        ImageSpanInfo imageSpanInfo = new ImageSpanInfo(Objects.requireNonNull(ContextCompat.getDrawable(textView.getContext(), imgRes)), textView, drawableMargin);

        // 替换 预留位置
        builder.setSpan(new VerticalImageSpan(imageSpanInfo, drawPadding), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (drawableMargin)
            // LeadingMarginSpan.Standard(首行缩进的px，其他行缩进的px)
            // start必须从0开始才会生效
            builder.setSpan(new LeadingMarginSpan.Standard(0, imageSpanInfo.mCanvasWidth + drawPadding), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }

    /**
     * 仅获取builder，可进行更多的span操作，请注意 index需进行 +1 操作
     * 因为图片占用1个位置
     */
    public static SpannableStringBuilder drawableLeftSpannableString(TextView textView, @DrawableRes int imgRes, int drawPadding, String text, boolean drawableMargin) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("替");
        builder.append(text);

        ImageSpanInfo imageSpanInfo = new ImageSpanInfo(Objects.requireNonNull(ContextCompat.getDrawable(textView.getContext(), imgRes)), textView, drawableMargin);

        // 替换 预留位置
        builder.setSpan(new VerticalImageSpan(imageSpanInfo, drawPadding), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (drawableMargin)
            // LeadingMarginSpan.Standard(首行缩进的px，其他行缩进的px)
            // start必须从0开始才会生效
            builder.setSpan(new LeadingMarginSpan.Standard(0, imageSpanInfo.mCanvasWidth + drawPadding), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

    /**
     * 文字图片的信息
     */
    private static class ImageSpanInfo {

        // 图片资源
        private final Drawable mDrawable;
        // 行高
        private final int mLineHeight;
        // 绘制宽高
        private int mCanvasHeight;
        private final int mCanvasWidth;

        public ImageSpanInfo(@NonNull Drawable mDrawable, TextView t, boolean ignoreFontPaddingParam) {
            this.mDrawable = mDrawable;

            // eg: 华为手机 mate40e 1080 *2 376px
            // 图片 200 * 200 放到xxhdpi 获取到为 200 * 200
            // 图片不变放到 xhdpi 获取到为300 * 300
            // 图片尺寸
            int mOriginWidth = mDrawable.getIntrinsicWidth();
            int mOriginHeight = mDrawable.getIntrinsicHeight();

            TextPaint paint = t.getPaint();

            // 获取文本的实际高度
            Paint.FontMetricsInt metrics = paint.getFontMetricsInt();

            if (t.getIncludeFontPadding() || ignoreFontPaddingParam)
                mCanvasHeight = metrics.bottom - metrics.top;
            else
                mCanvasHeight = metrics.descent - metrics.ascent;

            mLineHeight = mCanvasHeight;

            if (mCanvasHeight >= mOriginHeight) {
                // 图片过小，以图片尺寸显示
                mCanvasHeight = mOriginHeight;
                mCanvasWidth = mOriginWidth;
            } else {
                // 图片过大，以最大高度进行显示
                mCanvasWidth = mCanvasHeight * mOriginWidth / mOriginHeight;
            }
        }

    }

    // TODO 垂直居中的图片文字(暂不对外提供)
    private static class VerticalImageSpan extends ImageSpan {

        // 图片信息
        private final ImageSpanInfo mImageSpanInfo;
        // 图文距离
        private final int mDrawPadding;

        VerticalImageSpan(ImageSpanInfo imageSpanInfo, int drawPadding) {
            super(imageSpanInfo.mDrawable);
            this.mImageSpanInfo = imageSpanInfo;
            this.mDrawPadding = drawPadding;
        }

        /**
         * update the text line height
         */
        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            // 设置drawable范围(就是图片实际大小，让其画完整)
            getDrawable().setBounds(0, 0, mImageSpanInfo.mCanvasWidth, mImageSpanInfo.mCanvasHeight);
            // 返回图片占用的大小
            return getDrawable().getBounds().right + mDrawPadding;
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
            float centerY = 1.0f * mImageSpanInfo.mLineHeight / 2;
            // 偏移
            float transY = centerY - 1.0f * (drawable.getBounds().bottom - drawable.getBounds().top) / 2;

            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

    }
}

package io.github.kit.example.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.R;
import io.github.wong1988.kit.utils.SpannableStringUtils;

public class SpannableStringUtilsActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private String text = "MfgiǍga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string_utils);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        SpannableStringUtils.drawableLeft(tv3, R.mipmap.eg, 12, "东菱 Donlim 无线绞肉机打蛋器家用料理机绞馅机碎肉机搅拌机婴儿辅食机剥蒜机充电式", false);
        SpannableStringBuilder spannableStringBuilder = SpannableStringUtils.drawableLeftSpannableString(tv4, R.mipmap.yes, 20, "由第三方商家提供商品、发货开票及售后服务", true);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 2, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv4.setText(spannableStringBuilder);
    }

    public void setMin(View view) {
        SpannableStringUtils.drawableLeft(tv1, R.mipmap.min, 0, text, false);
        SpannableStringUtils.drawableLeft(tv2, R.mipmap.min, 0, text, false);
    }

    public void setMax(View view) {
        SpannableStringUtils.drawableLeft(tv1, R.mipmap.max, 0, text, false);
        SpannableStringUtils.drawableLeft(tv2, R.mipmap.max, 0, text, false);
    }

    public void setMin2(View view) {
        SpannableStringUtils.drawableLeft(tv1, R.mipmap.min, 0, text, true);
        SpannableStringUtils.drawableLeft(tv2, R.mipmap.min, 0, text, true);
    }

    public void setMax2(View view) {
        SpannableStringUtils.drawableLeft(tv1, R.mipmap.max, 0, text, true);
        SpannableStringUtils.drawableLeft(tv2, R.mipmap.max, 0, text, true);
    }
}
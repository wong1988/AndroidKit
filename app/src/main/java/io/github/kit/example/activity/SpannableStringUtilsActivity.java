package io.github.kit.example.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.R;
import io.github.wong1988.kit.utils.SpannableStringUtils;

public class SpannableStringUtilsActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private final String text = "MfgiǍ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string_utils);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        SpannableStringUtils.drawableLeft(tv3, R.mipmap.eg, 12, "东菱 Donlim 无线绞肉机打蛋器家用料理机绞馅机碎肉机搅拌机婴儿辅食机剥蒜机充电式");
    }

    public void setMin(View view) {
        SpannableStringUtils.drawableLeft(tv1, R.mipmap.min, 0, text);
        SpannableStringUtils.drawableLeft(tv2, R.mipmap.min, 0, text);
    }

    public void setMax(View view) {
        SpannableStringUtils.drawableLeft(tv1, R.mipmap.max, 0, text);
        SpannableStringUtils.drawableLeft(tv2, R.mipmap.max, 0, text);
    }
}
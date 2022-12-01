package io.github.kit.example.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.R;
import io.github.wong1988.kit.filter.CashierListener;
import io.github.wong1988.kit.utils.EditTextUtils;

public class EditTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        EditText cash = findViewById(R.id.cash);
        EditText chinese = findViewById(R.id.chinese);
        TextView noticeCash = findViewById(R.id.noticeCash);

        EditTextUtils.setCashFilter(cash, "111", new CashierListener() {
            @Override
            public void overMax(String max, String fillZeroMaxValue) {
                noticeCash.setText("超出最大值：" + fillZeroMaxValue);
            }

            @Override
            public void correct(String value, String fillZeroValue) {
                noticeCash.setText(fillZeroValue);
            }
        });

        EditTextUtils.setChineseFilter(chinese);
    }
}
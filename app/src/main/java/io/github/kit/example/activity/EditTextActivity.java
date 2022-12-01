package io.github.kit.example.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.R;
import io.github.wong1988.kit.filter.CashierListener;
import io.github.wong1988.kit.type.InputFilterAddMode;
import io.github.wong1988.kit.utils.EditTextUtils;

public class EditTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        EditText cash = findViewById(R.id.cash);
        EditText chinese = findViewById(R.id.chinese);
        EditText maxLength = findViewById(R.id.maxLength);
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


        printFilter(maxLength);
        Log.e("ddd", "xml添加过滤器打印完成");

        EditTextUtils.setMaxLengthFilter(maxLength, 4, InputFilterAddMode.APPEND_SUPER);

        printFilter(maxLength);
        Log.e("ddd", "class添长度滤器打印完成");
    }

    private void printFilter(EditText e) {
        InputFilter[] filters = e.getFilters();
        for (InputFilter filter : filters) {
            if (filter instanceof InputFilter.LengthFilter)
                Log.e("ddd", ((InputFilter.LengthFilter) filter).getMax() + "：最大值");
            else
                Log.e("ddd", filter.toString());
        }
    }
}
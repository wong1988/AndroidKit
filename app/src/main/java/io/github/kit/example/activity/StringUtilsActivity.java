package io.github.kit.example.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.R;
import io.github.wong1988.kit.entity.StringInfo;
import io.github.wong1988.kit.utils.StringUtils;

public class StringUtilsActivity extends AppCompatActivity {

    private EditText et;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_utils);
        et = (EditText) findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void parseInt(View view) {
        tv.setText(String.valueOf(StringUtils.parseInt(et.getText().toString())));
    }

    public void parseLong(View view) {
        tv.setText(String.valueOf(StringUtils.parseLong(et.getText().toString())));
    }

    public void parseFloat(View view) {
        tv.setText(String.valueOf(StringUtils.parseFloat(et.getText().toString())));
    }

    public void parseDouble(View view) {
        tv.setText(String.valueOf(StringUtils.parseDouble(et.getText().toString())));
    }

    public void getStringInfo(View view) {
        StringInfo stringInfo = StringUtils.stringInfo(et.getText().toString());

        String buffer = "所有字符：" + stringInfo.getAllChar() + "\n" +
                "小写字符：(" + stringInfo.getLowercase() + "个)" + stringInfo.getLowercaseChar() + "\n" +
                "小写字符原字符串中的下标：" + stringInfo.getLowercaseCharIndex().toString() + "\n" +
                "大写字符：(" + stringInfo.getUppercase() + "个)" + stringInfo.getUppercaseChar() + "\n" +
                "大写字符原字符串中的下标：" + stringInfo.getUppercaseCharIndex().toString() + "\n" +
                "数字：(" + stringInfo.getNumber() + "个)" + stringInfo.getNumberChar() + "\n" +
                "数字原字符串中的下标：" + stringInfo.getNumberCharIndex().toString() + "\n" +
                "其他字符：" + stringInfo.getOtherChar() + "\n" +
                "其他字符原字符串中的下标：" + stringInfo.getOtherCharIndex();

        tv.setText(buffer);
    }
}
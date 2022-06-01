package io.github.wong1988.kit.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import io.github.wong1988.kit.filter.CashierInputFilter;
import io.github.wong1988.kit.filter.CashierListener;

public class EditTextUtils {


    /**
     * EditText设置输入现金格式
     */
    public static void setCashFilter(EditText editText, CashierListener listener) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains(".")) {
                    int index = text.lastIndexOf(".");
                    if (index + 3 < text.length()) {
                        int selectionStart = editText.getSelectionStart();
                        String substring = text.substring(0, index + 3);
                        editText.setText(substring);
                        editText.setSelection(Math.min(selectionStart, substring.length()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 限制输入数字和.
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // 过滤器
        InputFilter[] inputFilter = new InputFilter[1];
        inputFilter[0] = new CashierInputFilter(listener);
        editText.setFilters(inputFilter);
    }

    public static void setCashFilter(EditText editText, String MAX_VALUE, CashierListener listener) {
        setCashFilter(editText, listener);

        // 过滤器
        InputFilter[] inputFilter = new InputFilter[1];
        inputFilter[0] = new CashierInputFilter(MAX_VALUE, listener);
        editText.setFilters(inputFilter);

    }
}

package io.github.wong1988.kit.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.filter.CashierInputFilter;
import io.github.wong1988.kit.filter.CashierListener;
import io.github.wong1988.kit.filter.ChineseInputFilter;
import io.github.wong1988.kit.filter.DigitsFilter;
import io.github.wong1988.kit.type.InputFilterAddMode;

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

    /**
     * 设置只能输入汉字
     */
    public static void setChineseFilter(EditText editText) {
        // 过滤器
        InputFilter[] inputFilter = new InputFilter[1];
        inputFilter[0] = new ChineseInputFilter();
        editText.setFilters(inputFilter);
    }

    /**
     * 设置EditText可输入最大值的过滤
     * mode = append 如果出现重复设置长度，系统会用重复的几个最大值里最小的进行限制
     */
    public static void setMaxLengthFilter(EditText editText, int max, InputFilterAddMode mode) {
        switch (mode) {
            case REPLACE:
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
                break;
            case APPEND:
                InputFilter[] filters = editText.getFilters();
                InputFilter[] newFilters = ArrayUtils.copyArray(filters, filters.length + 1);
                newFilters[newFilters.length - 1] = new InputFilter.LengthFilter(max);
                editText.setFilters(newFilters);
                break;
            case APPEND_SUPER:
                InputFilter[] filters1 = editText.getFilters();
                List<InputFilter> temp = new ArrayList<>();
                for (InputFilter inputFilter : filters1) {
                    if (!(inputFilter instanceof InputFilter.LengthFilter))
                        temp.add(inputFilter);
                }
                temp.add(new InputFilter.LengthFilter(max));
                editText.setFilters(ArrayUtils.toArray(temp, new InputFilter[temp.size()]));
                break;
        }
    }

    /**
     * 移除EditText长度限制的filter
     */
    public static void removeMaxLengthFilter(EditText editText) {
        InputFilter[] filters = editText.getFilters();
        List<InputFilter> temp = new ArrayList<>();
        for (InputFilter inputFilter : filters) {
            if (!(inputFilter instanceof InputFilter.LengthFilter))
                temp.add(inputFilter);
        }
        editText.setFilters(ArrayUtils.toArray(temp, new InputFilter[temp.size()]));
    }

    /**
     * 设置EditText可输入字符的过滤
     * mode = append 如果出现重复设置字符过滤，系统会从重复的几个过滤器里取出都包含的字符进行限制
     * 即 ：filter1 abc  filter2 bcd 即可以输入 bc  filter1 a filter2 b 即什么也不可输入
     */
    public static void setDigitsFilter(EditText editText, String digits, InputFilterAddMode mode) {
        switch (mode) {
            case REPLACE:
                editText.setFilters(new InputFilter[]{new DigitsFilter(digits)});
                break;
            case APPEND:
                InputFilter[] filters = editText.getFilters();
                InputFilter[] newFilters = ArrayUtils.copyArray(filters, filters.length + 1);
                newFilters[newFilters.length - 1] = new DigitsFilter(digits);
                editText.setFilters(newFilters);
                break;
            case APPEND_SUPER:
                InputFilter[] filters1 = editText.getFilters();
                List<InputFilter> temp = new ArrayList<>();
                for (InputFilter inputFilter : filters1) {
                    if (!(inputFilter instanceof DigitsFilter))
                        temp.add(inputFilter);
                }
                temp.add(new DigitsFilter(digits));
                editText.setFilters(ArrayUtils.toArray(temp, new InputFilter[temp.size()]));
                break;
        }
    }

    /**
     * 移除EditText字符限制的filter
     */
    public static void removeDigitsFilter(EditText editText) {
        InputFilter[] filters = editText.getFilters();
        List<InputFilter> temp = new ArrayList<>();
        for (InputFilter inputFilter : filters) {
            if (!(inputFilter instanceof DigitsFilter))
                temp.add(inputFilter);
        }
        editText.setFilters(ArrayUtils.toArray(temp, new InputFilter[temp.size()]));
    }
}

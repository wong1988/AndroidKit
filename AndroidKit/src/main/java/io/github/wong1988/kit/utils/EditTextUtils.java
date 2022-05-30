package io.github.wong1988.kit.utils;

import android.text.InputFilter;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import io.github.wong1988.kit.filter.CashierInputFilter;
import io.github.wong1988.kit.filter.CashierListener;

public class EditTextUtils {

    /**
     * EditText禁用双击以及长按弹出 复制、全选菜单
     * 仅可输入
     */
    public static void onlyEdit(EditText editText) {

        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);

        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    /**
     * EditText设置输入现金格式
     */
    public static void setCashFilter(EditText editText, CashierListener listener) {

        // 仅可输入,否则粘贴的内容不符合条件，需按返回才可隐藏粘贴弹窗
//        onlyEdit(editText);
        // 限制输入数字和.
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // 过滤器
        InputFilter[] inputFilter = new InputFilter[1];

        inputFilter[0] = new CashierInputFilter(listener);
        editText.setFilters(inputFilter);
    }

    public static void setCashFilter(EditText editText, String MAX_VALUE, CashierListener listener) {

        // 仅可输入,否则粘贴的内容不符合条件，需按返回才可隐藏粘贴弹窗
//        onlyEdit(editText);
        // 限制输入数字和.
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // 过滤器
        InputFilter[] inputFilter = new InputFilter[1];
        inputFilter[0] = new CashierInputFilter(MAX_VALUE, listener);
        editText.setFilters(inputFilter);
    }
}

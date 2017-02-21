package com.shuqu.microcredit.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by le on 2015/9/24 0024.
 */
public class SearchWather implements TextWatcher {

    EditText mEditText;
    public SearchWather(EditText etRegisterPwd) {
        mEditText = etRegisterPwd;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = mEditText.getText().toString();
        String str = stringFilter(editable.toString());
        if(!editable.equals(str)){
            ToastUtils.show(mEditText.getContext(),"只能输入字母或数字");
            mEditText.setText(str);
            //设置新的光标所在位置
            mEditText.setSelection(str.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}

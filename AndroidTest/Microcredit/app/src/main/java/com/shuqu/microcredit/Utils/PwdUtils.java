package com.shuqu.microcredit.Utils;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import com.shuqu.microcredit.R;

/**
 * Created by wuxin on 16/6/7.
 */
public class PwdUtils {

    /**
     * 设置密码显示与否
     */
    public static boolean showPassword(boolean isPwdShow, EditText et, ImageView imgShowPwd) {

        isPwdShow = !isPwdShow;
        int selection = et.getSelectionStart();
        if(isPwdShow) {
            //显示
            et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgShowPwd.setImageResource(R.mipmap.xianshi_icon);
        } else {
            et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgShowPwd.setImageResource(R.mipmap.buxianshi_icon);
        }
        et.setSelection(selection);

        return isPwdShow;
    }

}

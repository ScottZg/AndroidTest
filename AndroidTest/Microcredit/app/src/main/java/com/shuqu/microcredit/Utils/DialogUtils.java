package com.shuqu.microcredit.Utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.shuqu.microcredit.R;

/**
 * Class Info ：dialog
 * Created by Lyndon on 16/6/1.
 */
public class DialogUtils {
    public static AlertDialog showConfirmCancleDialog(Activity context,
                                                      CharSequence title,
                                                      dialogClickListener clickListener) {
        return showConfirmCancleDialog(context, title, null, null,
                clickListener);
    }

    public static AlertDialog showConfirmCancleDialog(Activity context,
                                                      CharSequence title,
                                                      CharSequence textCancelBtn,
                                                      CharSequence textOKBtn,
                                                      dialogClickListener clickListener) {
        return showConfirmCancleDialog(context, title, "", textCancelBtn,
                textOKBtn, clickListener);
    }

    public static AlertDialog showConfirmCancleDialog(Activity context,
                                                      CharSequence title, CharSequence content,
                                                      dialogClickListener clickListener) {
        return showConfirmCancleDialog(context, title, content, null, null,
                clickListener);
    }

    private static AlertDialog getDialog(Activity context, CharSequence title,
                                         CharSequence content, CharSequence textLeftBtn,
                                         CharSequence textRightBtn,
                                         final dialogClickListener clickListener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();

        // dialog.setView(context.getLayoutInflater().inflate(R.layout.confirm_cancle_dialog,
        // null));
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        int width = DisplayUtil.getWidth(context) * 3 / 4;
        window.setLayout(width,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = context.getLayoutInflater().inflate(R.layout.dialog_confirm_cancle, null);
        window.setContentView(view);//

        ((TextView) view.findViewById(R.id.title)).setText(title);
        TextView tvLeft = (TextView) view.findViewById(R.id.tv_left);
        TextView tvRight = (TextView) view.findViewById(R.id.tv_right);
        TextView contentTv = (TextView) view.findViewById(R.id.content);
        if (!TextUtils.isEmpty(content)) {
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText(content);
        } else {
            contentTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(textRightBtn)) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(textRightBtn);
        }else{
            tvRight.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(textLeftBtn)) {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(textLeftBtn);
        }else{
            tvLeft.setVisibility(View.GONE);
        }

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClickRight();
                }
                dialog.dismiss();
            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClickLeft();
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static AlertDialog showConfirmCancleDialog(Activity context,
                                                      CharSequence title, CharSequence content, CharSequence textCancelBtn,
                                                      CharSequence textOKBtn,
                                                      dialogClickListener clickListener) {
        return getDialog(context, title, content, textCancelBtn, textOKBtn,
                clickListener);
    }


    public interface dialogClickListener {
        void onClickLeft();
        void onClickRight();
    }

}

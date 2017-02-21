package com.shuqu.microcredit.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.shuqu.microcredit.R;

public class LoadingDialog extends Dialog {

	private TextView mTvMessage;
	private String mMessage;

	public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoadingDialog(Context context) {
		super(context);
	}


	public static LoadingDialog show(Context context, String msg) {
		try {
			if(context == null){
				return null;
			}
			if(context instanceof Activity){
				if(((Activity)context).isFinishing()){
					return null;
				}
			}
			LoadingDialog dialog = new LoadingDialog(context, R.style.lyndonding_Dialog_None);
			dialog.setContentView(R.layout.item_loading_dialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.mMessage = msg;
			dialog.mTvMessage = (TextView) dialog.findViewById(R.id.tv_info_msg);
			dialog.mTvMessage.setText(msg);
			dialog.show();
			return dialog;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void show() {
		super.show();
	}

	public void setMessage(String msg) {
		mMessage = msg;
		mTvMessage.setText(mMessage);
	}


	@Override
	public void dismiss() {
		//setMessage("");
		super.dismiss();
	}

}

package com.shuqu.microcredit.Push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.shuqu.microcredit.Content.ParamKeys;

public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			if (ParamKeys.NOTIFICATION_OPEN_ACTION.equals(intent.getAction())) {
				PushUtils.onNotificationClicked(context, intent);
			}
		} catch (Exception e) {
		}
	}
}

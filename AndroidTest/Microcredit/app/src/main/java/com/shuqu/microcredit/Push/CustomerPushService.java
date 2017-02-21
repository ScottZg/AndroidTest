package com.shuqu.microcredit.Push;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.shuqu.microcredit.Admin.DBAdmin;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.StringUtil;
import com.xiaomi.mipush.sdk.MiPushClient;
import org.json.JSONObject;

public class CustomerPushService extends IntentService {

    private static final String TAG = "CustomerPushService";

    private Context mContext;
    private NotificationManager mNotificationManager;


    public CustomerPushService() {
        super("CustomerPushService");
        Log.d(TAG, "CustomerPushService() ");
    }


    public CustomerPushService(String name) {
        super("CustomerPushService");
        Log.d(TAG, "CustomerPushService(String name) ");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "CustomerPushService()==onCreate ");
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Bundle bundle = getIn.getExtras();
        try {
            String msg = intent.getStringExtra(ParamKeys.PUSH_MESSAGE);
            showNotify(msg);
        } catch (Exception e) {
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "CustomerPushService()==onDestroy");
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "CustomerPushService()==onBind");
        return super.onBind(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("CustomerReceiver", "CustomerPushService()==onHandleIntent=intent.getAction() is " + intent.getAction());
    }


    private void showNotify(String message) {
        Log.d(TAG, "CustomerPushService()==showNotify==message is " + message);
        if (!StringUtil.checkStr(message)) {
            return;
        }
        try {

            JSONObject jsonObj = new JSONObject(message);
            String id = jsonObj.optString("id");
            String uid = jsonObj.optString("uid");
            //
            if (DBAdmin.getInstance(mContext).hasMessage(id) || !UserInfo.getUid().equals(uid)) {
                return;
            }
            else {
                DBAdmin.getInstance(mContext).savePushMsg(id, message);
            }
            MiPushClient.clearNotification(mContext);
            final String tit = jsonObj.optString("title");
            final String msg = jsonObj.optString("message");

            final Intent intent = new Intent();
            intent.putExtras(PushUtils.parseData(message));

            intent.setClass(mContext, MyReceiver.class);
            intent.setAction(ParamKeys.NOTIFICATION_OPEN_ACTION);
            showNotif(tit, msg, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示通知栏
     */
    void showNotif(String title, String summary, Intent intent) {

        //PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
        //intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent contentIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(title)
                .setContentText(summary)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(summary));
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
}
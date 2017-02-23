package com.zhanggui.servicesbestpractice.Services;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.zhanggui.servicesbestpractice.MainActivity;
import com.zhanggui.servicesbestpractice.NetWork.DownloadListener;
import com.zhanggui.servicesbestpractice.NetWork.DownloadTask;
import com.zhanggui.servicesbestpractice.R;

import java.io.File;

/**
 * Created by zhanggui on 2017/2/23.
 */

public class DownloadServices extends Service {
    private static final String TAG = "DownloadServices";
    private DownloadTask downloadTask;

    private String downloadUrl;

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",-1));
            Toast.makeText(DownloadServices.this, "Download Success", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadServices.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadServices.this, "Paused", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadServices.this, "Cancel", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    public class DownloadBinder extends Binder {
        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("Downloading...",0));
                Toast.makeText(DownloadServices.this, "Downloading...", Toast.LENGTH_SHORT).show();;
            }
        }
        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }else  {
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String direction = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(direction + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadServices.this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    private NotificationManager getNotificationManager() {
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String title,int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentIntent(pi);
        if (progress>=0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }

}

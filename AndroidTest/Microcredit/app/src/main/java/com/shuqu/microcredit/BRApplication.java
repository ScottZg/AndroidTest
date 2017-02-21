package com.shuqu.microcredit;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import com.lyndon.mobiledata.BRSDK;
import com.shuqu.microcredit.Admin.DBAdmin;
import com.shuqu.microcredit.Admin.StatisticsMgr;
import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Model.BrNetConfig;
import com.shuqu.microcredit.NetWork.BrNetConfigService;
import com.shuqu.microcredit.UI.BaseActivity;
import com.shuqu.microcredit.Utils.FileUtils;
import com.shuqu.microcredit.Utils.LogUtils;
import com.shuqu.microcredit.faceRec.BrFaceRecognition;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.List;
import java.util.Stack;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/6.
 */
public class BRApplication extends Application {

    public static BRApplication mContext;
    private Stack<BaseActivity> mActivityStack = new Stack<>();
    public static Handler mHandler = new Handler(Looper.myLooper());

    private static final String MIPUSH_APP_ID = "2882303761517506147";
    private static final String MIPUSH_APP_KEY = "5991750667147";

    @Override public void onCreate() {
        super.onCreate();
        mContext = this;
        LogUtils.isDebug = BuildConfig.DEBUG;

        initStatisticsMgr();
        initSupportHost();
        initDataSdk();
        //CrashHandler.startMonitor(getApplicationContext());
        initMipush();
        deleteOldMsg();
        BrFaceRecognition.init(this);
    }

    private void initDataSdk(){
        BRSDK.init(this, "333", "333", "333");
    }

    private void initStatisticsMgr(){
        StatisticsMgr.init(mContext);
    }

    private void initSupportHost() {

        if(BrNetConfigService.checkShouldNetSupportHost(this)) {
            //重新请求并写入文件
            BrNetConfigService.getNetConfig(this);
        } else {
            BrNetConfigService.mBrNetConfig = (BrNetConfig) FileUtils.readObjectFromFile(getApplicationContext().getFilesDir().getAbsolutePath() + Config.SUPPORT_HOST_FILE_NAME);
        }
    }

    public void exit() {
        closeAllActivities();
    }

    private void closeAllActivities() {
        if (mActivityStack.empty()) return;

        for (BaseActivity a : mActivityStack) {
            if (a == null || a.isFinishing()) continue;
            ActivityCompat.finishAffinity(a);
        }
        mActivityStack.clear();
    }

    public void addActivity(BaseActivity a) {
        if (a == null) return;
        mActivityStack.add(a);
    }

    public void removeActivity(BaseActivity a) {
        if (a == null) return;

        if (mActivityStack.contains(a)) {
            mActivityStack.remove(a);
        }
    }

    public static void runOnUiThread(Runnable runable){
        if(runable != null) {
            mHandler.post(runable);
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private void initMipush(){
        if (shouldInit()) {
            MiPushClient.registerPush(this, MIPUSH_APP_ID, MIPUSH_APP_KEY);
            MiPushClient.setAlias(this, "1234567890", null);
        }
    }

    private void deleteOldMsg(){
        DBAdmin.getInstance(this).deleteOldMessage(System.currentTimeMillis(), 1);
    }
}

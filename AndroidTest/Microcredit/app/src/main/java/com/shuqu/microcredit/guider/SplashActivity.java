package com.shuqu.microcredit.guider;

import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Model.DeviceInfo;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.FreshTokenService;
import com.shuqu.microcredit.NetWork.Header;
import com.shuqu.microcredit.UI.BaseActivity;
import com.shuqu.microcredit.UI.MainActivity;
import com.shuqu.microcredit.Utils.DeviceInfoUtil;
import com.shuqu.microcredit.Utils.FileUtils;
import com.shuqu.microcredit.Utils.ForwardActivityUtils;
import com.shuqu.microcredit.Utils.ManifestUtils;
import com.shuqu.microcredit.Utils.NetworkUtil;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.ReadAssetsUtil;
import com.shuqu.microcredit.Utils.StringUtil;
import java.util.ArrayList;

public class SplashActivity extends BaseActivity implements FreshTokenService.onFreshToken{

    boolean quit = false;

    private ArrayList<String> mPermissions;
    private static final int REQUEST_PERMISSION_CODE = 10005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override protected void initView() {

    }


    @Override protected void initData() {
        if(openTestMode()){
            showEnvironmentSelectDialog();
        }else {
            initAppData();
        }
    }

    private void initAppData(){

        FileUtils.deleteFile(getApplicationContext().getCacheDir().getAbsolutePath());

        UserInfo.setRefresh_token(PrefsUtils.getString(SplashActivity.this, Header.BRFRESHTOKEN, ""));
        UserInfo.setUid(PrefsUtils.getString(SplashActivity.this, Header.BRUID));
        UserInfo.setUserPhone(PrefsUtils.getString(SplashActivity.this, ParamKeys.USER_PHONE));

        if (StringUtil.checkStr(UserInfo.getRefresh_token())) {
            UserInfo.setAccess_token("");
            FreshTokenService.freshToken(SplashActivity.this, this);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toCheckPermissions();
            } else {
                handlePost();
            }
        }
    }

    /**
     * 检查所需权限
     */

    private void toCheckPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            addPermission(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.CAMERA);
        }

        //定位
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        //联系人
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.READ_CONTACTS);
        }
        //短信
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.READ_SMS);
        }
        //通话记录
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED){
            addPermission(Manifest.permission.READ_CALL_LOG);
        }

        if(mPermissions != null) {
            //有需要申请的权限
            toRequestPermissions();
        } else {
            handlePost();
        }

    }

    /**
     * 申请权限
     */
    private void toRequestPermissions() {

        String[] permissions = this.mPermissions.toArray(new String[this.mPermissions.size()]);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);
    }

    private void addPermission(String permission){
        if(mPermissions == null)
            mPermissions = new ArrayList<>();
        mPermissions.add(permission);
    }

    void handlePost(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!quit) {
                    initDeviceDatas();
                    toNaviActivity();
                }
            }
        }, 1000);
    }

    void toNaviActivity() {
        if (GuideUtil.isShouldOpenNaviForCurVersionCode(this)) {
            startActivity(new Intent(SplashActivity.this, NavigationPicActivity.class));
        } else {
            ForwardActivityUtils.forwardActivity(SplashActivity.this, MainActivity.class);
        }
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(requestCode == REQUEST_PERMISSION_CODE) {

            for (int i = 0; i < permissions.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mPermissions.remove(permissions[i]);
                    if(mPermissions.size() == 0) {
                        break;
                    }
                } else {
                    showTipPermissions();
                    return;
                }
            }
            handlePost();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showTipPermissions(){
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("温馨提示")
                .setMessage("如果您拒绝权限,APP将无法使用..很抱歉")
                .setCancelable(false)
                .setPositiveButton("重新申请", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toRequestPermissions();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Process.killProcess(Process.myPid());
                        System.exit(0);
                    }
                })
                .create().show();
    }

    private void initDeviceDatas(){
        DeviceInfo deviceinfo = DeviceInfoUtil.getDeviceInfo(this);
        DeviceParams.appVersion = deviceinfo.getSoftVersion();
        DeviceParams.appVersionCode = deviceinfo.getAppVersionCode();
        DeviceParams.channelID = ReadAssetsUtil.getMetaValue(this, Application.class, ParamKeys.CHANNEL_ID_KEY);
        //DeviceParams.channelName = ChannelUtil.getChannel(mContext);
        DeviceParams.deviceSize = deviceinfo.getScreenWidth() + "*" + deviceinfo.getScreenHeight();
        DeviceParams.deviceModel = deviceinfo.getDeviceModel();
        int deviceId = deviceinfo.getDeviceID().hashCode();
        DeviceParams.deviceId = String.valueOf(deviceId >0 ?  deviceId : -deviceId);
        DeviceParams.systemVersion = deviceinfo.getSystemVersion();
        DeviceParams.currentNetType = NetworkUtil.getCurrentNetworkTypeName(this);

        DeviceParams.appVersionCode = ManifestUtils.getAppVersionCode(this);
        DeviceParams.appName = getPackageName();
        DeviceParams.appVersion = ManifestUtils.getAppVersionName(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quit = true;
    }

    //现实环境切换对话框
    int mEnvironment = 0;
    private void showEnvironmentSelectDialog(){

        final String[] environment = new String[] { "日常", "预发"};
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("请选择调试环境")
                .setCancelable(false)
                .setSingleChoiceItems(environment, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEnvironment = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mEnvironment == 0){
                            Urls.BASE_API_URL = Urls.DAILY_API_URL;
                            Urls.BASE_WEB_URL = Urls.DAILY_WEB_URL;
                        }else{
                            Urls.BASE_API_URL = Urls.PRE_API_URL;
                            Urls.BASE_WEB_URL = Urls.PRE_WEB_URL;
                        }
                        initAppData();
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private boolean openTestMode(){
        if(!Config.IS_DEBUG){
            return false;
        }
        String path = Environment.getExternalStorageDirectory() + "/bairong.app";
        if(FileUtils.isFileExists(path)){
            return true;
        }
        return false;
    }


    @Override public void onTokenFreshed() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toCheckPermissions();
        } else{
            handlePost();
        }
    }

}

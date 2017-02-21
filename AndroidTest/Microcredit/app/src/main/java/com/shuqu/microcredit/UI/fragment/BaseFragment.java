package com.shuqu.microcredit.UI.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shuqu.microcredit.Admin.StatisticsMgr;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Model.MenuItem;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.BaseActivity;
import com.shuqu.microcredit.Utils.FileUtils;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.filter.fragment.MultiSelectorFragment;
import com.shuqu.microcredit.filter.fragment.SingleSelectorFragment;
import com.shuqu.microcredit.widget.LoadingDialog;
import com.shuqu.microcredit.widget.LoadingView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class Info ：base fragment
 * Created by Lyndon on 16/6/1.
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected BaseActivity mParentActivity = null;
    protected ArrayList<MenuItem> mMenuDatas = new ArrayList<>();
    protected String mCurrentMethodId = "";
    private IntentFilter mFilter;
    LocalBroadcastManager mBroadcastManager;

    LoadingView mLoadingView;
    LoadingDialog mLoadingDialog;

    SingleSelectorFragment mSelectorFragment = null;
    MultiSelectorFragment mMultiSelectorFragment = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (BaseActivity) activity;
        mFilter = new IntentFilter();
        mFilter.addAction(ParamKeys.ACTION_PAY_CANCEL);
        mFilter.addAction(ParamKeys.ACTION_PAY_FAIL);
        mFilter.addAction(ParamKeys.ACTION_PAY_SUCCESS);
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(setContentView(), null);
            initView();
            initData();
        } else {
            try {
                if(mRootView.getParent() != null) {
                    ((ViewGroup) (mRootView.getParent())).removeView(mRootView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StatisticsMgr.onPageStart(getClass().getSimpleName());
        return mRootView;
    }


    @Override public void onDestroyView() {
        StatisticsMgr.onPageEnd(getClass().getSimpleName());
        super.onDestroyView();
    }


    @Override
    public void onResume() {
        super.onResume();
        StatisticsMgr.onResume(mParentActivity);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatisticsMgr.onPause(mParentActivity);
    }

    public void setLeftButton(int drawableId, View.OnClickListener
            clickListener) {
        ImageView ivLeft = (ImageView) mRootView.findViewById(R.id.iv_left);
        if (ivLeft != null) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(drawableId);
            ivLeft.setOnClickListener(clickListener);
        }
    }

    protected void hideLeftBtn() {
        ImageView ivLeft = (ImageView) mRootView.findViewById(R.id.iv_left);
        if (ivLeft != null) {
            ivLeft.setVisibility(View.GONE);
        }
    }

    protected void setRightButton(int drawableId, View.OnClickListener
            clickListener) {
        ImageView ivRight = (ImageView) mRootView.findViewById(R.id.iv_right);
        if (ivRight != null) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(drawableId);
            ivRight.setOnClickListener(clickListener);
        }
    }

    protected void hideRightBtn() {
        ImageView ivRight = (ImageView) mRootView.findViewById(R.id.iv_right);
        if (ivRight != null) {
            ivRight.setVisibility(View.GONE);
        }
    }

    protected void setTitle(String title) {
        TextView tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void showLoadingDialog(String message){
        hideLoadingDialog();
        if(StringUtil.checkStr(message)){
            mLoadingDialog = LoadingDialog.show(mParentActivity, message);
        }else {
            mLoadingDialog = LoadingDialog.show(mParentActivity, "加载中...");
        }
    }

    public void hideLoadingDialog(){
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }
    //show error view
    protected void showErrorView(String errMsg){
        if(mLoadingView != null){
            mLoadingView.loadingError(errMsg);
        }
        setTitle("");
    }

    //show error view
    protected void showErrorView(int drawableId, String errMsg){
        if(mLoadingView != null){
            mLoadingView.setEmpty(errMsg, drawableId);
        }
        setTitle("");
    }

    //show error view
    protected void showErrorView(int drawableId, String errMsg, View.OnClickListener listener){
        if(mLoadingView != null){
            mLoadingView.setEmpty(errMsg, drawableId, listener);
        }
        setTitle("");
    }

    //hide error view
    protected void hideErrorView(){
        if(mLoadingView != null){
            mLoadingView.finishLoading();
        }
        setTitle("");
    }
    //show loading view
    public void showLoading(){
        if(mLoadingView != null){
            mLoadingView.startLoading();
        }
    }
    //hide loading
    protected void hideLoading(){
        if(mLoadingView != null){
            mLoadingView.finishLoading();
        }
    }

    protected void clickLeftBtn(boolean isRefresh) {
        if (mParentActivity != null) {
            Intent intent = new Intent();
            intent.putExtra(ParamKeys.HISTORY_BACK_REFRESH, isRefresh);
            mParentActivity.setResult(Activity.RESULT_OK, intent);
            mParentActivity.finish();
        }
    }

    protected void clickBack() {
        if (mParentActivity != null) {
            mParentActivity.onBackPressed();
        }
    }

    protected void setMenuDatas(String datas) {
        try {
            JSONObject jsonDatas = new JSONObject(datas);
            String menuData = jsonDatas.optString("menus");
            boolean mergeMenu = jsonDatas.optBoolean("stopMerge", false);
            if (mergeMenu) {
                mMenuDatas.clear();
            }
            mMenuDatas.addAll(MenuItem.parse(menuData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void clearMenuData() {
        mMenuDatas.clear();
    }

    protected abstract int setContentView();

    protected abstract void initView();
    protected void initData() {

    }
    //扫描二维码相关
    protected final int REQUEST_CODE_SCANNER = 11;
    private final String DEPEND_PACKAGE_NAME = "com.lyndon.scanner";
    private final String APK_PATH = Environment.getExternalStorageDirectory()
                                               .getAbsolutePath() + "/scanner.apk";
    protected void handlerScan(Context context) {
        if (context == null) return;
        if (isAvalable(context, DEPEND_PACKAGE_NAME)) {
            startScan();
            FileUtils.deleteFile(APK_PATH);
        } else {
            showInstallTips(context);
        }
    }

    private void showInstallTips(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("安装提示").setMessage("您需要安装扫描插件，才能使用该功能")
                .setIcon(R.mipmap.icon)
                .setPositiveButton("安装", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                copyApkToGivenPah(context, APK_PATH);
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                installApk(context);
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }

    private boolean isAvalable(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    private void copyApkToGivenPah(Context context, String path) {
        InputStream is;
        try {
            is = context.getAssets().open("scanner.apk");
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            String errMsg = "安装失败，请稍后重试";
            Toast.makeText(mParentActivity, errMsg, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void installApk(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + APK_PATH),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    protected void startScan() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(DEPEND_PACKAGE_NAME,
                "com.lyndon.qrcanner.FullScannerActivity");
        intent.setComponent(componentName);
        startActivityForResult(intent, REQUEST_CODE_SCANNER);
    }
}

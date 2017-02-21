package com.shuqu.microcredit.UI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Interface.IMainDrawerOptionCallBack;
import com.shuqu.microcredit.Interface.UserApi;
import com.shuqu.microcredit.Model.BottomMenuTabItem;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.Model.MainDrawerOptionItem;
import com.shuqu.microcredit.NetWork.CheckVersionService;
import com.shuqu.microcredit.NetWork.StatisticsService;
import com.shuqu.microcredit.NetWork.TokenService;
import com.shuqu.microcredit.NetWork.UpdateService;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.Adapter.CustomFragmentAdapter;
import com.shuqu.microcredit.UI.fragment.DrawerFragment;
import com.shuqu.microcredit.UI.fragment.WebFragment;
import com.shuqu.microcredit.Utils.FileUtils;
import com.shuqu.microcredit.Utils.ForwardActivityUtils;
import com.shuqu.microcredit.Utils.MenuIconMap;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.TimeUtils;
import com.shuqu.microcredit.widget.CustomViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity implements CheckVersionService.OnCheckVersionListener, IMainDrawerOptionCallBack {

    private static final String TAG = "MainActivity";
    private CustomViewPager mViewPager;
    private CustomFragmentAdapter mFragmentAdapter;
    private ArrayList<WebFragment> mFragments = new ArrayList<>();
    private ArrayMap<String, Integer> mUrl2IndexMap = new ArrayMap<>();
    private ArrayMap<Integer, WebFragment> mFragmentIndex = new ArrayMap<>();
    private ArrayList<BottomMenuTabItem> mTabItems = new ArrayList<>();
    LayoutInflater layoutInflater;
    LinearLayout tabContainer;

    ArrayList<View> menuTabs = new ArrayList<>();
    private AddBottomTabTask mAddBottomTabTask;
    private boolean mFromPush = false;

    private boolean mIsRefresh;

    DrawerLayout mDrawerLayout;
    DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadStartLog();
        //
        //testDownload();
    }

    private void uploadStartLog(){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("prespm", "");
        String spm = ParamKeys.APPID + ".1.." + DeviceParams.deviceId;
        data.put("spm", spm);

        new StatisticsService(this, data);
    }

    @Override protected void initView() {
        setContentView(R.layout.activity_main_drawer);
        mViewPager = (CustomViewPager) findViewById(R.id.vp_content);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fl_main_drawer);
        mViewPager.setScrollble(false);
        layoutInflater = LayoutInflater.from(this);
        tabContainer = (LinearLayout) findViewById(R.id.ll_bottom_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
        mDrawerFragment.addOnOptionClickCallBack(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String tabUrl = intent.getStringExtra(ParamKeys.MAIN_TAG_KEY);
        mIsRefresh = intent.getBooleanExtra(ParamKeys.MAIN_TAG_REFRESH, false);
        Integer index = mUrl2IndexMap.get(tabUrl);
        int relIndex = index == null ? 0 : index;
        mViewPager.setCurrentItem(mFragments.indexOf(mFragmentIndex.get(index)), false);
        setTabSelect(relIndex);
        refreshFragment(relIndex, mIsRefresh);
    }

    private void refreshFragment(int index, boolean isRefresh) {
        if (isRefresh) {
            WebFragment webFragment = mFragments.get(index);
            if(webFragment != null) {
                webFragment.startFresh();
            }
        }
    }


    public void addOrSelectTab(BottomMenuTabItem item) {
        String urlKey = item.mForwardUrl;
        int index = item.index;
        if (StringUtil.checkStr(urlKey)) {
            if (!(urlKey.startsWith("http:") || urlKey.startsWith("https:"))) {
                urlKey = Urls.BASE_WEB_URL + urlKey;
            }
            try {
                String tempData = urlKey.replace(Urls.BASE_WEB_URL, "");
                int endIndex = tempData.indexOf("?");
                if (endIndex != -1) {
                    tempData = tempData.substring(0, endIndex);
                }
                if (!mUrl2IndexMap.containsKey(tempData)) {
                    mUrl2IndexMap.put(tempData, index);
                    WebFragment webFragment = initFragment(urlKey);
                    mFragments.add(webFragment);
                    mFragmentIndex.put(index, webFragment);
                    mFragmentAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(mFragments.indexOf(mFragmentIndex.get(index)), false);
                } else {
                    mViewPager.setCurrentItem(mFragments.indexOf(mFragmentIndex.get(index)), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addTab(BottomMenuTabItem item) {
        String urlKey = item.mForwardUrl;
        int index = item.index;
        if (StringUtil.checkStr(urlKey)) {
            if (!(urlKey.startsWith("http:") || urlKey.startsWith("https:"))) {
                urlKey = Urls.BASE_WEB_URL + urlKey;
            }
            try {
                String tempData = urlKey.replace(Urls.BASE_WEB_URL, "");
                int endIndex = tempData.indexOf("?");
                if (endIndex != -1) {
                    tempData = tempData.substring(0, endIndex);
                }
                if (!mUrl2IndexMap.containsKey(tempData)) {
                    mUrl2IndexMap.put(tempData, index);
                    WebFragment webFragment = initFragment(urlKey);
                    mFragments.add(webFragment);
                    mFragmentIndex.put(index, webFragment);
                    mFragmentAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override protected void initData() {
        mUrl2IndexMap.put(Urls.FirstTab, 0);
        mFragmentAdapter = new CustomFragmentAdapter(getSupportFragmentManager(), mFragments);
        mFragments.add(initFragment(Urls.BASE_WEB_URL + Urls.FirstTab));
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);

        handlePushMessage();
        setPressTwoExitEnable(true);
        checkFreshToken();
        //CheckVersionService.checkVersion(this, this);
        registLoginCallback();
    }

    // 处理Push点击事件
    private void handlePushMessage(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                mFromPush = bundle.getBoolean(ParamKeys.FROM_PUSH, false);
                if (mFromPush) {
                    String pushParams = bundle.getString(ParamKeys.PUSH_PARAM);
                    //使用第一个fragment 加载 url
                    try {
                        JSONObject paramData = new JSONObject(pushParams);
                        String url = paramData.optString(ParamKeys.PUSH_URL);
                        if (mFragments.size() > 0 && StringUtil.checkStr(url)) {
                            mFragments.get(0).loadData(url);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private WebFragment initFragment(String url) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        if(url.contains(Urls.FirstTab)){
            bundle.putBoolean(ParamKeys.HIDEFRAGMENTHEAD, true);
        }
        bundle.putString(ParamKeys.WEBURL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    //check fresh token expire or not
    private void checkFreshToken() {
        String lastUpdateTime = PrefsUtils.getString(getApplicationContext(), ParamKeys.TOKEN_FRESH_TIME);
        if (TimeUtils.getInterval(lastUpdateTime) >= Config.TOKEN_MAX_VALID) {
            startService(new Intent(this, TokenService.class));
        }
    }

    public void createBottomTab(String data) {
        if(!menuTabs.isEmpty()) {
            return;
        }
        try {
            JSONObject tempdata = new JSONObject(data);
            JSONArray menuDatas = tempdata.optJSONArray("config");
            tabContainer.removeAllViews();

            if (menuDatas != null) {
                mAddBottomTabTask = new AddBottomTabTask(menuDatas);
                mAddBottomTabTask.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBottomTab(final BottomMenuTabItem item) {
        final View tabItem = layoutInflater.inflate(R.layout.item_bottom_tab, null);
        TextView tvName = (TextView) tabItem.findViewById(R.id.tv_tab_name);
        tvName.setText(item.mTitle);
        ImageView ivIcon = (ImageView) tabItem.findViewById(R.id.iv_tab_icon);
        //
        try {
            Drawable activityDrawable = null;
            Drawable normalDrawable = null;

            if(ParamKeys.ICON_SOUSE_BASE64.equals(item.mIconSource)) {
                activityDrawable = FileUtils.decodeBase64ToDrawable(item.mIconActive);
                normalDrawable = FileUtils.decodeBase64ToDrawable(item.mIconDefault);
            } else if(ParamKeys.ICON_SOUSE_APP.equals(item.mIconSource)) {
                int menuIconActive = MenuIconMap.getMenuIcon(item.mIconActive);
                int menuIconDefault = MenuIconMap.getMenuIcon(item.mIconDefault);
                activityDrawable = getResources().getDrawable(menuIconActive);
                normalDrawable = getResources().getDrawable(menuIconDefault);
            } else if(ParamKeys.ICON_SOUSE_URL.equals(item.mIconSource)) {
                Bitmap bitmapActive = BitmapFactory.decodeStream(new URL(item.mIconActive).openStream());
                activityDrawable = new BitmapDrawable(bitmapActive);
                Bitmap bitmapDefault = BitmapFactory.decodeStream(new URL(item.mIconDefault).openStream());
                normalDrawable = new BitmapDrawable(bitmapDefault);
            }

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[] { android.R.attr.state_pressed }, activityDrawable);
            states.addState(new int[] { android.R.attr.state_focused }, activityDrawable);
            states.addState(new int[] { android.R.attr.state_selected }, activityDrawable);
            states.addState(new int[] {}, normalDrawable);
            ivIcon.setImageDrawable(states);

            int activityTilte = Color.parseColor(item.mTitleColorActive);
            int defaultTilte = Color.parseColor(item.mTitleColorDefault);
            ColorStateList colorStateList = new ColorStateList(new int[][] {
                    { android.R.attr.state_pressed },
                    { android.R.attr.state_focused },
                    { android.R.attr.state_selected },
                    {} }, new int[] { activityTilte, activityTilte, activityTilte, defaultTilte });
            tvName.setTextColor(colorStateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        tabItem.setLayoutParams(params);
        menuTabs.add(tabItem);
    }


    @Override
    public void onShouldUpdateVersion(String updateUrl) {
        CheckVersionService.showVersionDialog(this, updateUrl);
    }

    @Override
    public void onOptionClick(MainDrawerOptionItem item) {
        if(item.mNeedLogin && !UserApi.userLogined()) {
            ForwardActivityUtils.forwardActivity(this, LoginActivity.class, false);
            return;
        }

        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(ParamKeys.WEBURL, item.mForwardUrl);
        startActivity(intent);
    }

    @Override
    public void onUserLogin() {
        mFragments.get(0).startFresh();
    }

    private class AddBottomTabTask extends AsyncTask<Void, Void, Void> {

        private JSONArray menuDatas;

        public AddBottomTabTask(JSONArray json) {
            this.menuDatas = json;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTabItems.clear();
            menuTabs.clear();
            for (int i = 0; i < menuDatas.length(); ++i) {
                BottomMenuTabItem item = BottomMenuTabItem.parse(menuDatas.optJSONObject(i));
                item.index = i;
                mTabItems.add(item);
                initBottomTab(item);
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int size = menuTabs.size();
            if(size > 0 && tabContainer.getChildCount() == 0) {
                for (int i = 0; i < size; i++) {
                    final View tabItem = menuTabs.get(i);
                    tabContainer.addView(tabItem);
                    final BottomMenuTabItem item = mTabItems.get(i);
                    //初始化底部tab,只创建第一个fragment
                    if(i == 0) {
                        addTab(item);
                    }
                    tabItem.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) {

                            if(!UserApi.userLogined() && item.mNeedLogin){
                                ForwardActivityUtils.forwardLogin(MainActivity.this, false);
                            }else{
                                for (int i = 0; i < menuTabs.size(); ++i) {
                                    menuTabs.get(i).setSelected(false);
                                }
                                addOrSelectTab(item);
                                tabItem.setSelected(true);
                            }
                        }
                    });
                }
                menuTabs.get(0).setSelected(true);
            }
        }
    }

    private void setTabSelect(int index) {
        if(menuTabs == null || menuTabs.isEmpty()) {
            return;
        }
        for (int i = 0; i < menuTabs.size(); ++i) {
            menuTabs.get(i).setSelected(false);
        }
        menuTabs.get(index).setSelected(true);
    }

    public void openUserCenter(){
        if(mDrawerLayout != null){
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    //test code
    private void testDownload(){
        String downloadUrl = "http://filelx.liqucn.com/upload/2015/shipin/com.qiyi.video_7.7.1_liqucn.com.apk";
        UpdateService.Builder.create(downloadUrl).build(this);
    }
}

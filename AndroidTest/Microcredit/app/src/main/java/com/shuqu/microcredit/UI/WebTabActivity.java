package com.shuqu.microcredit.UI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shuqu.microcredit.BRApplication;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Model.TopTabItem;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.Adapter.CustomFragmentAdapter;
import com.shuqu.microcredit.UI.fragment.WebFragment;
import com.shuqu.microcredit.widget.CustomViewPager;
import java.util.ArrayList;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/1.
 */
public class WebTabActivity extends BaseActivity implements View.OnClickListener {

    private CustomViewPager mViewPager;
    private CustomFragmentAdapter mFragmentAdapter;
    LayoutInflater layoutInflater;
    private ArrayList<WebFragment> mFragments = new ArrayList<>();
    ArrayList<View> mTopTabs = new ArrayList<>();

    WebFragment mCurrentFrag = null;

    LinearLayout tabContainer;

    private String mTag = "";
    //The corners are ordered top-left, top-right, bottom-right, bottom-left
    float cornerRadius = 46;
    private float[] leftCornerData = {cornerRadius, cornerRadius, 0 , 0, 0, 0, cornerRadius, cornerRadius};
    private float[] middleCornerData = {0, 0 , 0, 0, 0, 0 , 0, 0};
    private float[] rightCornerData = {0, 0, cornerRadius, cornerRadius , cornerRadius, cornerRadius, 0, 0};
    private float[] allCornerData = {cornerRadius, cornerRadius , cornerRadius, cornerRadius, cornerRadius, cornerRadius , cornerRadius, cornerRadius};

    private int mColorFocusBg;
    private int mColorDefaultBg;

    @Override protected void initView() {
        setContentView(R.layout.activity_tab_web);
        layoutInflater = LayoutInflater.from(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
        mViewPager = (CustomViewPager) findViewById(R.id.vp_content);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override public void onPageSelected(int position) {
                setTabSelect(mTopTabs.get(position), false);
            }


            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        tabContainer = (LinearLayout) findViewById(R.id.ll_top_tab_container);
        tabContainer.setVisibility(View.VISIBLE);
        mFragmentAdapter = new CustomFragmentAdapter(getSupportFragmentManager(), mFragments);
    }


    @Override protected String currentClassName() {
        return super.currentClassName() + "-" + mTag;
    }


    @Override protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String params = intent.getStringExtra(ParamKeys.TABWEBPARAM);
            if (!TextUtils.isEmpty(params)) {
                initTopTab(TopTabItem.parse(params));
            }
        }
    }


    private void initTopTab(ArrayList<TopTabItem> datas) {
        tabContainer.removeAllViews();
        if (datas != null) {

            TopTabItem item;
            int focusIndex = -1;
            int size = datas.size();
            for (int i = 0; i < size; ++i) {
                item = datas.get(i);
                if (item.mFocus) {
                    focusIndex = i;
                }
                initTopTab(item, i, size);
                mFragments.add(initFragment(item.mUrl));
            }
            mViewPager.setAdapter(mFragmentAdapter);
            mFragmentAdapter.notifyDataSetChanged();
            focusIndex = focusIndex == -1 ? 0 : focusIndex;
            mViewPager.setCurrentItem(focusIndex);
            setTabSelect(tabContainer.getChildAt(focusIndex), false);
        }
    }


    private WebFragment initFragment(String url) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ParamKeys.HIDEFRAGMENTHEAD, true);
        bundle.putString(ParamKeys.WEBURL, url);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initTopTab(final TopTabItem item, int currentIndex, int totalSize) {
        final View tabItem = layoutInflater.inflate(R.layout.item_top_tab, null);
        TextView tvName = (TextView) tabItem.findViewById(R.id.tv_tab_name);
        tabContainer.addView(tabItem);
        try {
            int activityTilte = Color.parseColor(item.mFocusFontColor);
            int defaultTilte = Color.parseColor(item.mDefaultFontColor);
            ColorStateList colorStateList = new ColorStateList(new int[][] {
                    { android.R.attr.state_pressed },
                    { android.R.attr.state_focused },
                    { android.R.attr.state_selected },
                    {} }, new int[] { activityTilte, activityTilte, activityTilte, defaultTilte });
            tvName.setTextColor(colorStateList);

            GradientDrawable gradientDrawable = new GradientDrawable();

            gradientDrawable.setStroke(2, Color.parseColor(item.mBorderColor));

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mColorFocusBg = Color.parseColor(item.mFocusBgColor);
                mColorDefaultBg = Color.parseColor(item.mDefaultBgColor);
                ColorStateList colorDatas = new ColorStateList(new int[][] {
                        { android.R.attr.state_pressed },
                        { android.R.attr.state_focused },
                        { android.R.attr.state_selected },
                        {} }, new int[] { mColorFocusBg, mColorFocusBg, mColorFocusBg, mColorDefaultBg });
                gradientDrawable.setColor(colorDatas);
            }
            //
            if(currentIndex == 0){
                if(totalSize == 1) {
                    gradientDrawable.setCornerRadii(allCornerData);
                }else{
                    gradientDrawable.setCornerRadii(leftCornerData);
                }
            }else if(totalSize >= 1){
                if(currentIndex < totalSize - 1){
                    gradientDrawable.setCornerRadii(middleCornerData);
                } else if(currentIndex == totalSize - 1){
                    gradientDrawable.setCornerRadii(rightCornerData);
                }
            }
            tabItem.setBackgroundDrawable(gradientDrawable);
        } catch (Exception e) {
        }

        tvName.setText(item.mTitleName);
        mTopTabs.add(tabItem);
        tabItem.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setTabSelect(tabItem, item.mRefresh);
            }
        });
    }


    private void setTabSelect(View item, boolean refresh) {
        for (int i = 0; i < mTopTabs.size(); ++i) {
            mTopTabs.get(i).setSelected(false);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                ((GradientDrawable)mTopTabs.get(i).getBackground()).setColor(mColorDefaultBg);
            }
        }
        item.setSelected(true);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ((GradientDrawable)item.getBackground()).setColor(mColorDefaultBg);
        }
        int currentIndex = mTopTabs.indexOf(item);
        if (currentIndex >= 0 && currentIndex < mTopTabs.size()) {
            mViewPager.setCurrentItem(currentIndex);
            mCurrentFrag = mFragments.get(currentIndex);
            if (refresh) {
                mCurrentFrag.onRefresh();
            }
        }
    }


    @Override public void onBackPressed() {
        if (mCurrentFrag != null && !mCurrentFrag.webGoBack()) {
            WebTabActivity.this.finish();
        }
    }


    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }

    //
    public void showFreshButton(final boolean show) {
        Runnable runable = new Runnable() {
            @Override
            public void run() {
                ImageView ivFresh = (ImageView) findViewById(R.id.iv_fresh);
                if (ivFresh != null) {
                    ivFresh.setVisibility(View.VISIBLE);
                    if (show) {
                        ivFresh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mCurrentFrag != null) {
                                    mCurrentFrag.onRefresh();
                                }
                            }
                        });
                    } else {
                        ivFresh.setVisibility(View.GONE);
                    }
                }
            }
        };
        BRApplication.runOnUiThread(runable);
    }
}

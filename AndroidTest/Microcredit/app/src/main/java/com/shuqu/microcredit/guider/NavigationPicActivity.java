package com.shuqu.microcredit.guider;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.shuqu.microcredit.R;

public class NavigationPicActivity extends Activity {
	ViewPager mNaviPager;
	NaviViewPagerAdapter mNaviPagerAdapter;
	int[] viewBackId = { R.mipmap.guide_1, R.mipmap.guide_2,R.mipmap.guide_3
			};
	private boolean isFirst = true;
	private int currentPage;
	private boolean isToLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_single_product_bigimage);
		initPager();
	}

	private void initPager() {

		mNaviPagerAdapter = new NaviViewPagerAdapter(this, viewBackId);
		mNaviPager = (ViewPager) findViewById(R.id.sigle_pager);
		mNaviPager.setAdapter(mNaviPagerAdapter);
		mNaviPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				//只能进入一次
				if(currentPage == (viewBackId.length - 1) && isFirst) {
					isFirst = false;
					if(positionOffsetPixels == 0) {
						isToLogin = true;
					}
				}
			}

			@Override
			public void onPageSelected(int position) {
				currentPage = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if(state == ViewPager.SCROLL_STATE_IDLE) {
					isFirst = true;
					if(isToLogin) {
						mNaviPagerAdapter.toMainActivity();
					}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalImageManger.getInstance().recycleAllBitmap(NavigationPicActivity.this.hashCode());
	}
}

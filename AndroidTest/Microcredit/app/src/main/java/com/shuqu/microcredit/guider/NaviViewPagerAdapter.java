package com.shuqu.microcredit.guider;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import com.shuqu.microcredit.UI.MainActivity;

/**
 * 引导页 
 * @author yang
 *
 */
public class NaviViewPagerAdapter extends PagerAdapter {

	/**
	 * 上下文
	 */
	private Activity mActivity;

	int[] arrays;

	public NaviViewPagerAdapter(Activity context, int[] arrays) {
		this.mActivity = context;
		this.arrays = arrays;
	}

	// 这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		ViewPagerItemView itemView = (ViewPagerItemView) object;
		itemView.recycle();
	}

	@Override
	public void finishUpdate(View view) {

	}

	// 这里返回相册有多少条,和BaseAdapter一样.
	@Override
	public int getCount() {
		return arrays.length;
	}

	// 这里就是初始化ViewPagerItemView.如果ViewPagerItemView已经存在,
	// 重新reload，不存在new一个并且填充数据.
	@Override
	public Object instantiateItem(View container, int position) {
		ViewPagerItemView itemView;
		itemView = new ViewPagerItemView(mActivity);
		itemView.setData(arrays[position]);
		if (position == arrays.length - 1) {
			itemView.getButton().setVisibility(View.VISIBLE);
			itemView.getButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					toMainActivity();
				}
			});
		}
		((ViewPager) container).addView(itemView);
		return itemView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {

	}

	public void toMainActivity() {
		GuideUtil.setShouldOpenNaviFalse(mActivity);
		mActivity.startActivity(new Intent(mActivity, MainActivity.class));
		mActivity.finish();
	}
}

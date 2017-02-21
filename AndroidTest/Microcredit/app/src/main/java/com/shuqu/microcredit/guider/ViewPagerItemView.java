package com.shuqu.microcredit.guider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.shuqu.microcredit.R;

public class ViewPagerItemView extends FrameLayout {

	/**
	 * 图片的ImageView.
	 */
	private ImageView mAlbumImageView;

	private Button startButton;

	/**
	 * 图片的Bitmap.
	 */
	private Bitmap mBitmap;

	private int resId;
	Activity mActivity;

	public ViewPagerItemView(Context context) {
		super(context);
		mActivity = (Activity) context;
		setupViews();
	}

	public ViewPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (Activity) context;
		setupViews();
	}

	// 初始化View.
	private void setupViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.navi_itemview, null);

		mAlbumImageView = (ImageView) view.findViewById(R.id.album_imgview);
		startButton = (Button) view.findViewById(R.id.start_button);
		addView(view);
	}

	public Button getButton() {

		return startButton;
	}

	/**
	 * 填充数据，共外部调用.
	 * 
	 */
	public void setData(int resId) {
		this.resId = resId;
		LocalImageManger.getInstance().setBackground(mActivity.hashCode(), mAlbumImageView, resId);

	}

	/**
	 * 这里内存回收.外部调用.
	 */
	public void recycle() {
		LocalImageManger.getInstance().recycleBitmap(mActivity.hashCode(), mAlbumImageView);
		mAlbumImageView = null;
	}

	/**
	 * 重新加载.外部调用.
	 */
	public void reload() {
		LocalImageManger.getInstance().setBackground(mActivity.hashCode(), mAlbumImageView, resId);
	}

}

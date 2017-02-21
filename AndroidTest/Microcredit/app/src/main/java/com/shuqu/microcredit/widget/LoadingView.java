package com.shuqu.microcredit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.shuqu.microcredit.R;

public class LoadingView extends ViewSwitcher {

	InnerView mLoadingView;
	/**
	 * 加载中文字信息，空值不显示文字
	 */
	private String mStrInfoMsg = "";
	/**
	 * 异常时文字信息
	 */
	private String mStrErrorMsg = "网络异常，请稍后重试";
	/**
	 * 异常时背景色
	 */
	private int mBgError;
	/**
	 * 普通加载状态下背景色
	 */
	private int mBgNormal;
	boolean disloading;
	boolean mHasBgDrawable = false;
	
	public LoadingView(Context context) {
		super(context);
		init(null);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	@SuppressLint({ "Recycle", "NewApi" })
	private void init(AttributeSet attrs) {
		if (!isInEditMode()) {
			setClickable(true);
			mLoadingView = new InnerView(getContext());
			addView(mLoadingView);
			final Resources res = getResources();
			final float defaultTxtSize = res.getDimension(R.dimen.default_text_size);
			final int defaultTxtColor = res.getColor(R.color.default_text_color);
			if (attrs != null) {
				TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0);

				disloading = typedArray.getBoolean(R.styleable.LoadingView_lv_loading, false);
				//text color
				int textColor = typedArray.getColor(R.styleable.LoadingView_lv_text_color,  defaultTxtColor);
				mLoadingView.setTextColor(textColor);
				
				// text size
				//float textSize = typedArray.getDimension(R.styleable.LoadingView_lv_text_size, defaultTxtSize);
				//mLoadingView.setTextSize(textSize);
				
				//bg
				Drawable bgId = typedArray.getDrawable(R.styleable.LoadingView_lv_background);
				try {
					if(bgId != null){
						mLoadingView.setBackground(bgId);
						mHasBgDrawable = true;
					}
				} catch (Exception e) {
					mHasBgDrawable = false;
				}
				
				//bg color
				mBgNormal = typedArray.getColor(R.styleable.LoadingView_lv_background_color,  0x00ffffff);
				if(!mHasBgDrawable){
					mLoadingView.setBackgroundColor(mBgNormal);
				}
				
				//bg color if error
				mBgError = typedArray.getColor(R.styleable.LoadingView_lv_error_background_color,  0xffffffff);

				// info msg
				mStrInfoMsg = typedArray.getString(R.styleable.LoadingView_lv_title);
				setText(mStrInfoMsg);
				//error msg
				mStrErrorMsg = typedArray.getString(R.styleable.LoadingView_lv_error_info);

				typedArray.recycle();
			}
		}
		this.setVisibility(View.GONE);
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);

		if (getChildCount() == 2){
			setLoading(disloading);
		}
	}

	public boolean getLoading() {
		return getCurrentView() == mLoadingView;
	}

	public void setLoading(boolean state) {
		if ((getCurrentView() == mLoadingView) != state) {
			showNext();
		}
	}

	public void setText(int stringResourceId) {
		mLoadingView.setText(stringResourceId);
	}

	public void setText(String text) {
		mLoadingView.setText(text);
	}
	/**
	 * start loading
	 */
	public void startLoading(){
		this.setVisibility(View.VISIBLE);
		mLoadingView.setText(mStrInfoMsg);
		mLoadingView.setProgressVisiable(false);
		mLoadingView.setEmptyVisiable(true);
		if(!mHasBgDrawable){
			mLoadingView.setBackgroundColor(mBgNormal);
		}
	}
	
	/**
	 * start loading
	 */
	public void startLoading(String msg){
		this.setVisibility(View.VISIBLE);
		mStrInfoMsg = msg;
		mLoadingView.setText(mStrInfoMsg);
		mLoadingView.setEmptyVisiable(true);
		mLoadingView.setProgressVisiable(false);
		if(!mHasBgDrawable){
			mLoadingView.setBackgroundColor(mBgNormal);
		}
	}
	
	/**
	 * finish loading
	 */
	public void finishLoading(){
		this.setVisibility(View.GONE);
	}
	
	/**
	 * loading error
	 */
	public void loadingError(){
		this.setVisibility(View.VISIBLE);
		mLoadingView.setText(mStrErrorMsg);
		mLoadingView.setEmptyVisiable(true);
		mLoadingView.setProgressVisiable(true);
		if(!mHasBgDrawable){
			mLoadingView.setBackgroundColor(mBgError);
		}
	}
	
	/**
	 * loading error
	 */
	public void loadingError(String info){
		this.setVisibility(View.VISIBLE);
		mStrErrorMsg = info;
		mLoadingView.setText(info);
		mLoadingView.setEmptyVisiable(true);
		mLoadingView.setProgressVisiable(true);
		if(!mHasBgDrawable){
			mLoadingView.setBackgroundColor(mBgError);
		}
	}
	
	/**
	 * set empty
	 */
	public void setEmpty(String info, int drawableId){
		this.setVisibility(View.VISIBLE);
		mStrErrorMsg = info;
		mLoadingView.setText(info);
		mLoadingView.setEmptyImg(drawableId);
		mLoadingView.setEmptyVisiable(false);
		mLoadingView.setProgressVisiable(true);
		if(!mHasBgDrawable){
			mLoadingView.setBackgroundColor(mBgError);
		}
	}

	/**
	 * set empty
	 */
	public void setEmpty(String info, int drawableId, OnClickListener listener){
		this.setVisibility(View.VISIBLE);
		mStrErrorMsg = info;
		mLoadingView.setText(info);
		mLoadingView.setEmptyImg(drawableId, listener);
		mLoadingView.setEmptyVisiable(false);
		mLoadingView.setProgressVisiable(true);
		if(!mHasBgDrawable){
			mLoadingView.setBackgroundColor(mBgError);
		}
	}
	
	/**
	 * InnerView
	 */
	public static class InnerView extends LinearLayout {
		
		private TextView mTvInfoMsg;
		private ProgressBar mProgressBar;
		private ImageView mIvEmpty;
		
		public InnerView(Context context) {
			super(context);
			setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			setOrientation(HORIZONTAL);
			LayoutInflater.from(context).inflate(R.layout.item_loadingview, this, true);
			// init view
			mTvInfoMsg = (TextView) findViewById(R.id.tv_info_msg);
			mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
			mIvEmpty = (ImageView) findViewById(R.id.iv_empty);
		}

		public void setText(int stringResourceId) {
			mTvInfoMsg.setVisibility(View.VISIBLE);
			mTvInfoMsg.setText(stringResourceId);
		}

		public void setText(String loadingText) {
			if (TextUtils.isEmpty(loadingText)) {
				mTvInfoMsg.setVisibility(View.INVISIBLE);
			} else {
				mTvInfoMsg.setVisibility(View.VISIBLE);
				mTvInfoMsg.setText(loadingText);
			}
		}
		
		public void setTextColor(int color){
			mTvInfoMsg.setTextColor(color);
		}
		
		public void setTextSize(float size){
			mTvInfoMsg.setTextSize(size);
		}
		
		public void setProgressVisiable(boolean hide){
			if(hide){
				mProgressBar.setVisibility(View.GONE);
			} else {
				mProgressBar.setVisibility(View.VISIBLE);
			}
		}
		
		public void setEmptyImg(int drawable){
			mIvEmpty.setVisibility(View.VISIBLE);
			mIvEmpty.setImageResource(drawable);
		}

		public void setEmptyImg(int drawable, OnClickListener listener){
			mIvEmpty.setVisibility(View.VISIBLE);
			mIvEmpty.setImageResource(drawable);
			if(listener != null){
				mIvEmpty.setOnClickListener(listener);
			}
		}
		
		public void setEmptyVisiable(boolean hide){
			if(hide){
				mIvEmpty.setVisibility(View.GONE);
			} else {
				mIvEmpty.setVisibility(View.VISIBLE);
			}
		}
	}
}

package com.shuqu.microcredit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SectionIndexer;
import com.shuqu.microcredit.Utils.DisplayUtil;

public class SideBar extends View {

	OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	SectionIndexer mSectionIndexer;

	int choose = -1;
	Paint paint = new Paint();
	boolean showBkg = false;

	private int textColor_default = 0XFFF38989;
	private int textColor_choose = 0XFF439DF8;
	private int pressBgColor = 0XFFA6a6a6;

	private int mTopPositonAdd;
	private int mLetterHeight;

	private int mHeight;

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SideBar(Context context) {
		super(context);
		init();
	}

	private void init() {
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setAntiAlias(true);
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics()));
		paint.setTextAlign(Align.CENTER);
		paint.setColor(textColor_choose);
		mTopPositonAdd = DisplayUtil.dp2px(getContext(), 10);
		mLetterHeight = DisplayUtil.dp2px(getContext(), 16);
	}

	public SectionIndexer getSectionIndexer() {
		return mSectionIndexer;
	}

	public void setSectionIndexer(SectionIndexer sectionIndexer) {
		this.mSectionIndexer = sectionIndexer;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mSectionIndexer == null || mSectionIndexer.getSections() == null
				|| mSectionIndexer.getSections().length == 0) {
			return;
		}

//		if (showBkg) {
//			canvas.drawColor(pressBgColor);
//		}
		int height = mLetterHeight * mSectionIndexer.getSections().length + mTopPositonAdd;
		mHeight = height;
		int width = getWidth();
		int singleHeight = height / mSectionIndexer.getSections().length;
		for (int i = 0; i < mSectionIndexer.getSections().length; i++) {

//			if (i == choose) {
//				paint.setColor(textColor_choose);
//			} else {
//				paint.setColor(textColor_default);
//			}
			float xPos = width / 2;
			float yPos = singleHeight * i + singleHeight / 2 + mTopPositonAdd;
			canvas.drawText(mSectionIndexer.getSections()[i].toString(), xPos, yPos, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mSectionIndexer == null || mSectionIndexer.getSections() == null) {
			return false;
		}

		final int action = event.getAction();
		final float y = event.getY() - mTopPositonAdd;
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int section = (int) (y / mHeight * mSectionIndexer.getSections().length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != section && listener != null) {
				if (section >= 0 && section < mSectionIndexer.getSections().length) {
					listener.onTouchingLetterChanged(mSectionIndexer.getSections()[section].toString(), section);
					choose = section;

					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != section && listener != null) {
				if (section >= 0 && section < mSectionIndexer.getSections().length) {
					listener.onTouchingLetterChanged(mSectionIndexer.getSections()[section].toString(), section);
					choose = section;
					invalidate();

				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s, int section);
	}

}
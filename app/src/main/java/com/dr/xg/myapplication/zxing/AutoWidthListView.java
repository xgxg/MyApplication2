package com.dr.xg.myapplication.zxing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class AutoWidthListView extends ListView {

	public AutoWidthListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoWidthListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoWidthListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int maxWidth = meathureWidthByChilds() + getPaddingLeft()
				+ getPaddingRight();
		super.onMeasure(
				MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.UNSPECIFIED),
				heightMeasureSpec);// 注意，这个地方一定是MeasureSpec.UNSPECIFIED
	}

	public int meathureWidthByChilds() {
		int maxWidth = 0;
		View view = null;
		for (int i = 0; i < getAdapter().getCount(); i++) {
			view = getAdapter().getView(i, view, this);
			view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			if (view.getMeasuredWidth() > maxWidth) {
				maxWidth = view.getMeasuredWidth();
			}
			view = null;
		}
		return maxWidth;
	}
}

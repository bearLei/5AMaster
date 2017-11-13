package com.puti.education.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 *
 */
public class GridViewForScrollView extends GridView{
	
	  public GridViewForScrollView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	
	    public GridViewForScrollView(Context context) {
	        super(context);
	    }
	
	    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }
	
	
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        //核心在此
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec+50);
	    }
}

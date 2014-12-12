/*******************************************************************************
 * [y] hybris Platform
 *  
 *   Copyright (c) 2000-2013 hybris AG
 *   All rights reserved.
 *  
 *   This software is the confidential and proprietary information of hybris
 *   ("Confidential Information"). You shall not disclose such Confidential
 *   Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with hybris.
 ******************************************************************************/
package com.hybris.mobile.utility;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {

	static final String logTag = "ActivitySwipeDetector";
	private OnSwipeListener activity;
	static final int MIN_DISTANCE = 100;
	private float downX, downY, upX, upY;
	
	public static interface OnSwipeListener {

		public void onRightToLeftSwipe();

		public void onLeftToRightSwipe();

		public void onTopToBottomSwipe();

		public void onBottomToTopSwipe();
	}

	public SwipeDetector(OnSwipeListener activity){
	    this.activity = activity;
	}

	public boolean onTouch(View v, MotionEvent event) {
	    switch(event.getAction()){
	        case MotionEvent.ACTION_DOWN: {
	            downX = event.getX();
	            downY = event.getY();
	            return true;
	        }
	        case MotionEvent.ACTION_UP: {
	            upX = event.getX();
	            upY = event.getY();

	            float diffX = downX - upX;
	            float diffY = downY - upY;

	            if(Math.abs(diffX) > MIN_DISTANCE){
	                if(diffX < 0) { activity.onLeftToRightSwipe(); return true; }
	                if(diffX > 0) { activity.onRightToLeftSwipe(); return true; }
	            }
	            else {
	                    return false;
	            }

	            if(Math.abs(diffY) > MIN_DISTANCE){
	                if(diffY < 0) { activity.onTopToBottomSwipe(); return true; }
	                if(diffY > 0) { activity.onBottomToTopSwipe(); return true; }
	            }
	            else {
	            }

	            return true;
	        }
	    }
	    return false;
	}

}

/**
*    This is Mainboard of app.
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.futureshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
public class VflipperLayout extends RelativeLayout{
	private static final int ID_RADIOGROUP_LOC = 11;
	private static final int ID_RADIOBTN_GOODS = 12;
	private static final int ID_RADIOBTN_PUSH = 13;
	private static final int ID_RADIOBTN_LOCATION = 14;
	private RadioGroup  mRadioGroupLoc;
	private RadioButton mRadioBtnGoods;
	private RadioButton mRadioBtnPush;
	private RadioButton mRadioBtnLocation;
	private ViewFlipper mViewFlipperLoc;
	private ViewLayoutA mViewLayoutA;
	private ViewLayoutB mViewLayoutB;
	private ViewLayoutC mViewLayoutC;
	public VflipperLayout(Context context) {
		super(context);
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		Bitmap bmNull=null;
		BitmapDrawable bDrawable=new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.browse_bg_tile));
		bDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		this.setBackgroundDrawable(bDrawable);
		
		LayoutParams relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 55);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mRadioGroupLoc=new RadioGroup(context);
		mRadioGroupLoc.setOrientation(LinearLayout.HORIZONTAL);
		mRadioGroupLoc.setId(ID_RADIOGROUP_LOC);
		mRadioGroupLoc.setGravity(Gravity.CENTER_HORIZONTAL);
		this.addView(mRadioGroupLoc, relativeParams);
		
		RadioGroup.LayoutParams redioGroupParams=new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		redioGroupParams.weight=1;
		mRadioBtnGoods=new RadioButton(context);
		mRadioBtnGoods.setBackgroundResource(R.drawable.nav_inactive_tile);
//		Bitmap a = BitmapFactory.decodeResource(getResources(), R.drawable.add);
//		mRadioBtnGoods.setButtonDrawable(new BitmapDrawable(a));
		mRadioBtnGoods.setText(R.string.navBar1);
		mRadioBtnGoods.setTextSize(25);
		mRadioBtnGoods.getPaint().setFakeBoldText(true);
		mRadioBtnGoods.setButtonDrawable(new BitmapDrawable(bmNull));
		mRadioBtnGoods.setId(ID_RADIOBTN_GOODS);
		mRadioBtnGoods.setTextColor(0xffffffff);
		mRadioBtnGoods.setOnClickListener(mOnClickListener);
		mRadioBtnGoods.setGravity(Gravity.CENTER);
		mRadioGroupLoc.addView(mRadioBtnGoods,redioGroupParams);
		
		redioGroupParams=new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		ImageView mImgSplitlineLeft=new ImageView(context);
		mImgSplitlineLeft.setImageResource(R.drawable.nav_divider);
		mRadioGroupLoc.addView(mImgSplitlineLeft, redioGroupParams);
		
		redioGroupParams=new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		redioGroupParams.weight=1;
		mRadioBtnPush=new RadioButton(context);
		mRadioBtnPush.setBackgroundResource(R.drawable.nav_inactive_tile);
		mRadioBtnPush.setText(R.string.navBar2);
		mRadioBtnPush.setTextSize(25);
		//mRadioBtnPush.setBackgroundColor(ColorManager.MainBgColor);
		mRadioBtnPush.setButtonDrawable(new BitmapDrawable(bmNull));
		mRadioBtnPush.getPaint().setFakeBoldText(true);
		mRadioBtnPush.setId(ID_RADIOBTN_PUSH);
		mRadioBtnPush.setTextColor(0xffffffff);
		mRadioBtnPush.setOnClickListener(mOnClickListener);
		mRadioBtnPush.setGravity(Gravity.CENTER);
		mRadioGroupLoc.addView(mRadioBtnPush,redioGroupParams);
		mRadioGroupLoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				changeCheckStatus(checkedId);
			}
		});
		
		redioGroupParams=new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		ImageView mImgSplitlineRight=new ImageView(context);
		mImgSplitlineRight.setImageResource(R.drawable.nav_divider);
		mRadioGroupLoc.addView(mImgSplitlineRight, redioGroupParams);
		
		redioGroupParams=new RadioGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		redioGroupParams.weight=1;
		mRadioBtnLocation=new RadioButton(context);
		mRadioBtnLocation.setButtonDrawable(new BitmapDrawable(bmNull));
		mRadioBtnLocation.setBackgroundResource(R.drawable.nav_inactive_tile);
		mRadioBtnLocation.setText(R.string.navBar3);
		mRadioBtnLocation.setTextSize(25);
		mRadioBtnLocation.setTextColor(0xffffffff);
		mRadioBtnLocation.getPaint().setFakeBoldText(true);
		mRadioBtnLocation.setId(ID_RADIOBTN_LOCATION);
		mRadioBtnLocation.setGravity(Gravity.CENTER);
		mRadioGroupLoc.addView(mRadioBtnLocation,redioGroupParams);
		mRadioBtnLocation.setOnClickListener(mOnClickListener);
		
		relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.BELOW,ID_RADIOGROUP_LOC);
		mViewFlipperLoc=new ViewFlipper(context);
		mViewFlipperLoc.setInAnimation(getContext(),R.anim.push_left_in);
		mViewFlipperLoc.setOutAnimation(getContext(),R.anim.push_left_out);
		mViewFlipperLoc.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
		mViewFlipperLoc.setFlipInterval(1000);
		mViewFlipperLoc.setClickable(true);
	//	mViewFlipperLoc.setOnTouchListener(mOnTouchListener);
		this.addView(mViewFlipperLoc,relativeParams);

		relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		mViewLayoutA=new ViewLayoutA(context);
		mViewLayoutB=new ViewLayoutB(context);
		mViewLayoutC=new ViewLayoutC(context);
		
		mViewFlipperLoc.addView(mViewLayoutA,relativeParams);
		mViewFlipperLoc.addView(mViewLayoutB,relativeParams);
		mViewFlipperLoc.addView(mViewLayoutC,relativeParams);
		
		changeCheckStatus(0);
	}

	
	static int pre = ID_RADIOBTN_GOODS;
	private OnClickListener mOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			if(v.getId()>pre){
				mViewFlipperLoc.setInAnimation(AnimationHelper.inFromLeftAnimation());
		        mViewFlipperLoc.setOutAnimation(AnimationHelper.outToRightAnimation());
			}
			else
			{
				mViewFlipperLoc.setInAnimation(AnimationHelper.inFromRightAnimation());
		    	mViewFlipperLoc.setOutAnimation(AnimationHelper.outToLeftAnimation());
			}
			switch (v.getId()) {
			case ID_RADIOBTN_GOODS:
				mViewFlipperLoc.setDisplayedChild(0);
				break;
			case ID_RADIOBTN_PUSH:
				mViewFlipperLoc.setDisplayedChild(1);
				break;
			case ID_RADIOBTN_LOCATION:
				mViewFlipperLoc.setDisplayedChild(2);
				break;
			default:
				break;
			}
			
			pre = v.getId();
			
		}
	};
	
	private void changeCheckStatus(int checkedId)
	{

		switch (checkedId) {
		case 0:
		case ID_RADIOBTN_GOODS:
			mRadioBtnGoods.setBackgroundResource(R.drawable.nav_active_2options);
			mRadioBtnPush.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnLocation.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnGoods.setTextColor(Color.GRAY);
			mRadioBtnPush.setTextColor(Color.WHITE);
			mRadioBtnLocation.setTextColor(Color.WHITE);
			break;
		case 1:
		case ID_RADIOBTN_PUSH:
			mRadioBtnGoods.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnPush.setBackgroundResource(R.drawable.nav_active_2options);
			mRadioBtnLocation.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnGoods.setTextColor(Color.WHITE);
			mRadioBtnPush.setTextColor(Color.GRAY);
			mRadioBtnLocation.setTextColor(Color.WHITE);
			break;
		case 2:
		case ID_RADIOBTN_LOCATION:
			mRadioBtnGoods.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnPush.setBackgroundResource(R.drawable.nav_inactive_tile);
			mRadioBtnLocation.setBackgroundResource(R.drawable.nav_active_2options);
			mRadioBtnGoods.setTextColor(Color.WHITE);
			mRadioBtnPush.setTextColor(Color.WHITE);
			mRadioBtnLocation.setTextColor(Color.GRAY);
			break;
		default:
			break;
		}
	}
    private void showNext()
    {
    	mViewFlipperLoc.setInAnimation(AnimationHelper.inFromLeftAnimation());
        mViewFlipperLoc.setOutAnimation(AnimationHelper.outToRightAnimation());
        mViewFlipperLoc.showNext();
        Log.e("debug", "showNext():"+mViewFlipperLoc.getDisplayedChild());
        changeCheckStatus(mViewFlipperLoc.getDisplayedChild());
    }
    private void showPrevious()
    {
    	  mViewFlipperLoc.setInAnimation(AnimationHelper.inFromRightAnimation());
    	  mViewFlipperLoc.setOutAnimation(AnimationHelper.outToLeftAnimation());
    	  mViewFlipperLoc.showPrevious();
    	  Log.e("debug", "showPrevious():"+mViewFlipperLoc.getDisplayedChild());
    	  changeCheckStatus(mViewFlipperLoc.getDisplayedChild());
    }
    
	private OnTouchListener mOnTouchListener=new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			// TODO Auto-generated method stub
			 return mGestureDetector.onTouchEvent(ev);  
		}
	};
    
      GestureDetector mGestureDetector =new GestureDetector(new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
			if(velocityX<0)
			{
				showNext();
			}
			else
			{
	            showPrevious();
			}
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	}); 
      
    private static int duration = 50;
	public static class AnimationHelper {
      public static Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(duration);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
      }

      public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, +1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(duration);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
      }

      // for the next movement
      public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, +1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(duration);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
      }

      public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(duration);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
      }
    }
}

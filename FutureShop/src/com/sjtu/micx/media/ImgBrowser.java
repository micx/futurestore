/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.media;

import com.sjtu.micx.config.ItemManager;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.futureshop.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 图片浏览、缩放、拖动、自动居中
 */
public class ImgBrowser extends Activity implements OnTouchListener {

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    DisplayMetrics dm;
    ImageView imgView;
    Bitmap bitmap;

    float minScaleR;// 最小缩放比例
    static final float MAX_SCALE = 4f;// 最大缩放比例

    static final int NONE = 0;// 初始状态
    static final int DRAG = 1;// 拖动
    static final int ZOOM = 2;// 缩放
    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;
    private int ID=0;

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
		
		
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
    	ID = intent.getIntExtra("IMG", 0);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
        setContentView(R.layout.scale);
        imgView = (ImageView) findViewById(R.id.imag);// 获取控件
        //Parcel imgbyte = this.getIntent().getParcelableExtra("IMG");
       
        // bitmap = Bitmap.CREATOR.createFromParcel(imgbyte);
         //System.out.println(imgbyte);
      
        //bitmap = BitmapFactory.decodeResource(getResources(), this.getIntent().getIntExtra("IMG", 0));// 获取图片资源
        String imageFile = DataCenter.cache+"/"+ItemManager.getImageByID_local(ID);
       BitmapFactory.Options opts =  new  BitmapFactory.Options();
        opts.inJustDecodeBounds =  true ;
        BitmapFactory.decodeFile(imageFile, opts);
                      
        opts.inSampleSize = computeSampleSize(opts, - 1 ,  1920 * 1080 );       
        opts.inJustDecodeBounds =  false ;
        bitmap = BitmapFactory.decodeFile(imageFile, opts);
//        try {
//			imgView.setImageURI(getImageURI(DataCenter.images[ID],DataCenter.cache));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        imgView.setImageBitmap(bitmap);// 填充控件
        
        
        imgView.setOnTouchListener(this);// 设置触屏监听
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
        minZoom();
        center();
        imgView.setImageMatrix(matrix);
        }catch(Exception e){
        	System.out.println(e);
        }
    }

    /**
     * 触屏监听
     */
    @Override
	public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // 主点按下
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            prev.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        // 副点按下
        case MotionEvent.ACTION_POINTER_DOWN:
            dist = spacing(event);
            // 如果连续两点距离大于10，则判定为多点模式
            if (spacing(event) > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - prev.x, event.getY()
                        - prev.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float tScale = newDist / dist;
                    matrix.postScale(tScale, tScale, mid.x, mid.y);
                }
            }
            break;
        }
        imgView.setImageMatrix(matrix);
        CheckView();
        return true;
    }

    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void CheckView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR) {
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE) {
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    /**
     * 最小缩放比例，最大为100%
     */
    private void minZoom() {
        minScaleR = Math.min(
                (float) dm.widthPixels / (float) bitmap.getWidth(),
                (float) dm.heightPixels / (float) bitmap.getHeight());
        if (minScaleR < 1.0) {
            matrix.postScale(minScaleR, minScaleR);
        }
    }

    private void center() {
        center(true, true);
    }

    /**
     * 横向、纵向居中
     */
    protected void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imgView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 两点的中点
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    
    
    
    
    public  static  int  computeSampleSize(BitmapFactory.Options options,
            int  minSideLength,  int  maxNumOfPixels) {
            int  initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);     
            int  roundedSize;
            if  (initialSize <=  8 ) {
               roundedSize =  1 ;
               while  (roundedSize < initialSize) {
                   roundedSize <<=  1 ;
               }
           }  else  {
               roundedSize = (initialSize +  7 ) /  8  *  8 ;
           }
       
           return  roundedSize;
      }
       
      private  static  int  computeInitialSampleSize(BitmapFactory.Options options,
               int  minSideLength,  int  maxNumOfPixels) {
           double  w = options.outWidth;
           double  h = options.outHeight;
       
           int  lowerBound = (maxNumOfPixels == - 1 ) ?  1  :
                   ( int ) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
           int  upperBound = (minSideLength == - 1 ) ?  128  :
                   ( int ) Math.min(Math.floor(w / minSideLength),
                   Math.floor(h / minSideLength));
       
           if  (upperBound < lowerBound) {
               // return the larger one when there is no overlapping zone.
               return  lowerBound;
           }
       
           if  ((maxNumOfPixels == - 1 ) &&
                   (minSideLength == - 1 )) {
               return  1 ;
           }  else  if  (minSideLength == - 1 ) {
               return  lowerBound;
           }  else  {
               return  upperBound;
           }
      }  
}
/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sjtu.micx.config.Config;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.futureshop.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 图片浏览、缩放、拖动、自动居中
 */
@SuppressLint("ParserError")
public class ImgBrowser_1 extends Activity implements OnTouchListener {

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    DisplayMetrics dm;
    ImageView imgView;
    Bitmap bitmap;
    private File cache = DataCenter.cache;
    float minScaleR;// 最小缩放比例
    static final float MAX_SCALE = 4f;// 最大缩放比例

    static final int NONE = 0;// 初始状态
    static final int DRAG = 1;// 拖动
    static final int ZOOM = 2;// 缩放
    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;
    private String imgID;

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
//    	ID = intent.getIntExtra("IMG", 0);
         imgID = intent.getStringExtra("IMG");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
        setContentView(R.layout.scale_1);
        imgView = (ImageView) findViewById(R.id.imag_1);// 获取控件
        
        String imgurl;
        if(Config.LabMode){
        	imgurl = "http://192.168.1.120:8080/"+imgID+".jpg";
		 }else{
			 imgurl = "http://"+Config.Server_imgIP+"/"+imgID+".jpg";
		 }
        processThread(imgurl);
        //Parcel imgbyte = this.getIntent().getParcelableExtra("IMG");
       
        // bitmap = Bitmap.CREATOR.createFromParcel(imgbyte);
         //System.out.println(imgbyte);
      
        //bitmap = BitmapFactory.decodeResource(getResources(), this.getIntent().getIntExtra("IMG", 0));// 获取图片资源
//        String imageFile = DataCenter.cache+"/"+ItemManager.getImageByID_local(ID);
//       BitmapFactory.Options opts =  new  BitmapFactory.Options();
//        opts.inJustDecodeBounds =  true ;
//        BitmapFactory.decodeFile(imageFile, opts);
//                      
//        opts.inSampleSize = computeSampleSize(opts, - 1 ,  1920 * 1080 );       
//        opts.inJustDecodeBounds =  false ;
//        bitmap = BitmapFactory.decodeFile(imageFile, opts);
//        
////        try {
////			imgView.setImageURI(getImageURI(DataCenter.images[ID],DataCenter.cache));
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//        imgView.setImageBitmap(bitmap);// 填充控件
        
        
//        imgView.setOnTouchListener(this);// 设置触屏监听
//        dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
//        minZoom();
//        center();
//        imgView.setImageMatrix(matrix);
        }catch(Exception e){
        	System.out.println(e);
        }
    }

	 //声明变量
    private ProgressDialog pd;
    //定义Handler对象
    private Handler handler =new Handler(){
       @SuppressLint("HandlerLeak")
	@Override
       //当有消息发送出来的时候就执行Handler的这个方法
       public void handleMessage(Message msg){
          super.handleMessage(msg);
          //只要执行到这里就关闭对话框
          
          String imageFile = DataCenter.cache+"/"+imgID+".jpg";
          Log.i("IMG", imageFile);
          BitmapFactory.Options opts =  new  BitmapFactory.Options();
           opts.inJustDecodeBounds =  true ;
           BitmapFactory.decodeFile(imageFile, opts);
                         
           opts.inSampleSize = computeSampleSize(opts, - 1 ,  1920 * 1080 );       
           opts.inJustDecodeBounds =  false ;
           bitmap = BitmapFactory.decodeFile(imageFile, opts);
           
//           try {
//   			imgView.setImageURI(getImageURI(DataCenter.images[ID],DataCenter.cache));
//   		} catch (Exception e) {
//   			// TODO Auto-generated catch block
//   			e.printStackTrace();
//   		}
           if(bitmap!=null){
           imgView.setImageBitmap(bitmap);// 填充控件
           imgView.setOnTouchListener(ImgBrowser_1.this);// 设置触屏监听
           dm = new DisplayMetrics();
           getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
           minZoom();
           center();
           imgView.setImageMatrix(matrix);
           }else{
        	   finish();
           }
           
          
          pd.dismiss();
       }
    };
    private void processThread(final String path){
          //构建一个下载进度条
          pd= ProgressDialog.show(ImgBrowser_1.this, "载入", "图片加载中…");
          new Thread(){
             @Override
			public void run(){
                //在新线程里执行长耗时方法
                Uri uri = longTimeMethod( path);
               
                
              
                handler.sendEmptyMessage(0);
             }		
          }.start();
    }
    
	private Uri longTimeMethod(String path) {

    	//DataCenter.data_init();
		try {
			Uri uri = getImageURI(path,cache);
			return uri;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    
	
	
	
	
	/*
	 * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
	 * 这里的path是图片的地址
	 */
	public Uri getImageURI(String path, File cache) throws Exception {
		String name =path.substring(path.lastIndexOf("/"));
		File file = new File(cache, name);
		// 如果图片存在本地缓存目录，则不去服务器下载 
		if (file.exists()) {
			return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
		} else {
			
				// 从网络上获取图片
			path = path.substring(0,path.lastIndexOf("."));
			path = path.substring(0, path.length()-2);
			path = path+name.substring(0, name.lastIndexOf("."))+".jpg";
			Log.i("URI---", path);
			URL url = new URL(path);
			
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[11024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						is.close();
						fos.close();
						// 返回一个URI对象
						return Uri.fromFile(file);
					}
		}
			
			return null;
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
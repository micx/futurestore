package com.sjtu.micx.flip;

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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemSelectedListener;

public class MianActivity extends Activity  implements OnTouchListener{
    /** Called when the activity is first created. */
	//��Ļ�Ŀ��
	public static int screenWidth;
	//��Ļ�ĸ߶�
	public static int screenHeight;
	private MyGallery gallery;
	private boolean isScale  = false;  //�Ƿ�����
	private float  beforeLenght = 0.0f; 	 //���������
	private float afterLenght = 0.0f; 	
	private float currentScale = 1.0f;
	private String imgID,index;
	private int ImgNum=6;
	private static Context contxt;
	private File cache = DataCenter.cache;
	private GalleryAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���ô����ޱ��� ȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);
        
        
       
        
        Intent intent = this.getIntent();
        contxt = this.getApplicationContext();
        
       imgID = intent.getStringExtra("IMG");
       index = intent.getStringExtra("index");
        
		
		
		  String imgurl;
	        if(Config.LabMode){
	        	imgurl = "http://192.168.1.120:8080/"+imgID+"/"+imgID;
			 }else{
				 imgurl = "http://"+Config.Server_imgIP+"/"+imgID+"/"+imgID;
			 }
	        processThread(imgurl);
        
        
        
        
        gallery = (MyGallery) findViewById(R.id.mygallery);
        gallery.setVerticalFadingEdgeEnabled(false);	
        gallery.setHorizontalFadingEdgeEnabled(false);//);// ����view��ˮƽ����ʱ��ˮƽ�߲�������
        
        
         adapter = new GalleryAdapter(this);
//        String[] img = new String[3];
//        img[0] = DataCenter.cache+"/00000000000000000000C001_A.jpg";
//        img[1] = DataCenter.cache+"/00000000000000000000C001.jpg";
//        img[2] = DataCenter.cache+"/00000000000000000000C002.jpg";
//        adapter.images = img;
//        gallery.setAdapter(adapter);
        
        //��ȡ��Ļ�Ĵ�С
        screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    //��������
    private ProgressDialog pd;
    //����Handler����
    private Handler handler =new Handler(){
       @SuppressLint("HandlerLeak")
	@Override
       //������Ϣ���ͳ�����ʱ���ִ��Handler���������
       public void handleMessage(Message msg){
          super.handleMessage(msg);
          //ֻҪִ�е�����͹رնԻ���
          int cnt=0;
          String[] img = new String[20];
          for(int i=0;i<ImgNum;i++){
	          String imageFile = DataCenter.cache+"/"+imgID+index+i+".jpg";
	          Log.i("IMG", imageFile);
	          BitmapFactory.Options opts =  new  BitmapFactory.Options();
	           opts.inJustDecodeBounds =  true ;
	           BitmapFactory.decodeFile(imageFile, opts);
	                         
	           opts.inSampleSize = computeSampleSize(opts, - 1 ,  1920 * 1080 );       
	           opts.inJustDecodeBounds =  false ;
	           Bitmap bitmap = BitmapFactory.decodeFile(imageFile, opts);
	           
	//           try {
	//   			imgView.setImageURI(getImageURI(DataCenter.images[ID],DataCenter.cache));
	//   		} catch (Exception e) {
	//   			// TODO Auto-generated catch block
	//   			e.printStackTrace();
	//   		}
	           if(bitmap!=null){
	        	   
	        	  
	               
	               img[cnt] = imageFile;
	               
//	               adapter.images = img;
//	               gallery.setAdapter(adapter);
	        	   
	        	   
//	        	   imgv = new ImageView(contxt);
//	        	   
//	        	  
//	        	   
//	        	   
//	        	   
//	        	   imgv.setImageBitmap(bitmap);
//	        	   flipper.addView(imgv);
//	        	   
	        	   
	        	   
	        	   cnt++;
	           
	           }else{
	        	  
	           }
	           adapter.len = cnt;
	           adapter.images = img;
               gallery.setAdapter(adapter);
          }
          
          pd.dismiss();
          if(cnt==0)finish();
       }
    };
    private void processThread(final String path){
          //����һ�����ؽ�����
          pd= ProgressDialog.show(MianActivity.this, "����", "ͼƬ�����С�");
          new Thread(){
             @Override
			public void run(){
                //�����߳���ִ�г���ʱ����
            	 Log.i("URL____", path);
            	 for(int i=0;i<ImgNum;i++){
                longTimeMethod( path+index+i+".jpg");
            	 }
               
                
              
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
	 * �������ϻ�ȡͼƬ�����ͼƬ�ڱ��ش��ڵĻ���ֱ���ã������������ȥ������������ͼƬ
	 * �����path��ͼƬ�ĵ�ַ
	 */
	public Uri getImageURI(String path, File cache) throws Exception {
		String name =path.substring(path.lastIndexOf("/"));
		File file = new File(cache, name);
		// ���ͼƬ���ڱ��ػ���Ŀ¼����ȥ���������� 
		if (file.exists()) {
			return Uri.fromFile(file);//Uri.fromFile(path)��������ܵõ��ļ���URI
		} else {
			
				// �������ϻ�ȡͼƬ
//			path = path.substring(0,path.lastIndexOf("."));
//			path = path.substring(0, path.length()-2);
//			path = path+name.substring(0, name.lastIndexOf("."))+".jpg";
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
						// ����һ��URI����
						return Uri.fromFile(file);
					}
		}
			
			return null;
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private class GalleryChangeListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			 currentScale = 1.0f;
			 isScale = false;
			 beforeLenght = 0.0f;
		     afterLenght = 0.0f;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}
    
    
    /**
	 * ���������ľ���
	 */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			beforeLenght = spacing(event);
			if (beforeLenght > 5f) {
				isScale = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			/*�����϶�*/
			if (isScale) {
				afterLenght = spacing(event);
				if (afterLenght < 5f)
					break;
				float gapLenght = afterLenght - beforeLenght;
				if (gapLenght == 0) {
					break;
				} else if (Math.abs(gapLenght) > 5f) {
					float scaleRate = gapLenght / 854;
					Animation myAnimation_Scale = new ScaleAnimation(currentScale, currentScale + scaleRate, currentScale, currentScale + scaleRate, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					myAnimation_Scale.setDuration(100);
					myAnimation_Scale.setFillAfter(true);
					myAnimation_Scale.setFillEnabled(true);
					currentScale = currentScale + scaleRate;
					gallery.getSelectedView().setLayoutParams(new Gallery.LayoutParams((int) (480 * (currentScale)), (int) (854 * (currentScale))));
					beforeLenght = afterLenght;
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			isScale = false;
			break;
		}

		return false;
	}
}
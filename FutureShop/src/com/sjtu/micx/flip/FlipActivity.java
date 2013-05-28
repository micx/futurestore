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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * Androidʵ�����һ���Ч��
 * @author Administrator
 *
 */
public class FlipActivity extends Activity implements OnGestureListener , OnTouchListener{
	
	Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    DisplayMetrics dm;
    ImageView imgView;
    Bitmap bitmap;

    float minScaleR;// ��С���ű���
    static final float MAX_SCALE = 4f;// ������ű���

    static final int NONE = 0;// ��ʼ״̬
    static final int DRAG = 1;// �϶�
    static final int ZOOM = 2;// ����
    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;
    private int ID=0;
	
	
	
	
	private ViewFlipper flipper;
	private GestureDetector detector;
	private String imgID;
	private File cache = DataCenter.cache;
	private ImageView imgv;
	
	private static Context contxt;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flip);
        Intent intent = this.getIntent();
        contxt = this.getApplicationContext();
//    	ID = intent.getIntExtra("IMG", 0);
         imgID = intent.getStringExtra("IMG");
         
        
         
         
        detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);
		
		
		  String imgurl;
	        if(Config.LabMode){
	        	imgurl = "http://192.168.1.120:8080/"+imgID+"/"+imgID;
			 }else{
				 imgurl = "http://"+Config.Server_imgIP+"/"+imgID+"/"+imgID;
			 }
	        processThread(imgurl);
		
//	        flipper.getCurrentView().setOnTouchListener(this);// ���ô�������;
	        
	         
//	         ((ImageView) flipper.getCurrentView()).setImageMatrix(matrix);
		
		
		
		
//		img.setBackgroundResource(R.drawable.one);
		
//		flipper.addView(img);
//		flipper.addView(addTextView(R.drawable.two));
//		flipper.addView(addTextView(R.drawable.three));
//		flipper.addView(addTextView(R.drawable.four));
//		flipper.addView(addTextView(R.drawable.five));
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
          for(int i=0;i<3;i++){
	          String imageFile = DataCenter.cache+"/"+imgID+"_D"+i+".jpg";
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
	        	   imgv = new ImageView(contxt);
	        	   
	        	  
	        	   
	        	   
	        	   
	        	   imgv.setImageBitmap(bitmap);
	        	   flipper.addView(imgv);
	        	   
	        	   
	        	   
	        	   cnt++;
	           
	           }else{
	        	  
	           }
          }
          
          pd.dismiss();
          if(cnt==0)finish();
       }
    };
    private void processThread(final String path){
          //����һ�����ؽ�����
          pd= ProgressDialog.show(FlipActivity.this, "����", "ͼƬ�����С�");
          new Thread(){
             @Override
			public void run(){
                //�����߳���ִ�г���ʱ����
            	 Log.i("URL____", path);
                Uri uri = longTimeMethod( path+"_D0.jpg");
                longTimeMethod( path+"_D1.jpg");
                longTimeMethod( path+"_D2.jpg");
               
                
              
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    private View addTextView(int id) {
		ImageView iv = new ImageView(this);
		iv.setImageResource(id);
		return iv;
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	return this.detector.onTouchEvent(event);
    }
    
    @Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
    
    @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in_flip));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out_flip));
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			this.flipper.showPrevious();
			return true;
			
			
		}
		
		
		
		return false;
	}
    
    @Override
    public void onLongPress(MotionEvent e) {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
    		float distanceY) {
    	// TODO Auto-generated method stub
    	return false;
    }
    
    @Override
    public void onShowPress(MotionEvent e) {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
    	// TODO Auto-generated method stub
    	return false;
    }
    
    
    
    
    /**
     * ����
     * 
     * 
     * ��������
     */
    @Override
	public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // ���㰴��
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            prev.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        // ���㰴��
        case MotionEvent.ACTION_POINTER_DOWN:
            dist = spacing(event);
            // �����������������10�����ж�Ϊ���ģʽ
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
     * ���������С���ű������Զ�����
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
     * ��С���ű��������Ϊ100%
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
     * �����������
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
            // ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
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
     * ����ľ���
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * ������е�
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    
    
    
    
    
}
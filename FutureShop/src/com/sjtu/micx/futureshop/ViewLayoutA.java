/**
*    This is Goods browser page of app.
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.futureshop;
import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtu.micx.UHFReader.Scanner;
import com.sjtu.micx.config.ColorManager;
import com.sjtu.micx.config.Config;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.detail.CommodityActivity;
import com.sjtu.micx.gridview.GridItemAdapter;
import com.sjtu.micx.webservice.WSMethod;

public class ViewLayoutA extends RelativeLayout {
	public static int DIR_1=1,DIR_2=0;
	private ListView listview_2,listview_3;
	private Context contxt;
	private TextView mTxt;
	private int scr_width = DataCenter.Width;			//屏幕宽
	private int scr_height= DataCenter.Height;			//屏幕高
	private List<String> list = new ArrayList<String>(); 
	private ArrayList<String> data ;  
    private Spinner mySpinner; 
    private ArrayAdapter<String> adapter1; 
    private Animation myAnimation;   
    private boolean isStop =true;
	private MyHandler myHandler;
	private ImageButton scanbtn;
	private EditText searchtxt;

	public ViewLayoutA(Context context) {
		super(context);
		contxt = context;
		initLayout(context);
		
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
			
		Background(context,0);
		Second_Dir(context,0);
		Third_Dir(context,0);
		Search(context);
		First_Dir(context);
	}
	private void Background(Context context, int i) {
		// TODO Auto-generated method stub
		LayoutParams relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		ImageView img = new ImageView(context);
		img.setBackgroundResource(R.drawable.bgshadow);
		this.addView(img,relativeParams);	
	}

	private TextView seacrchbtntxt ,scanbtntxt;
	private void Search(Context context) {
		// TODO Auto-generated method stub
		ImageView img = new ImageView(context);
		LayoutParams imgParams  = new LayoutParams(scr_width*30/100, scr_height*30/100);
		img.setBackgroundColor(Color.WHITE);
		this.addView(img,imgParams);
		
		
		searchtxt = new EditText(context);
		LayoutParams txtParams  = new LayoutParams(scr_width*27/100, scr_height*8/100);
		searchtxt.setBackgroundResource(R.drawable.search);
		txtParams.setMargins(scr_width*3/200, scr_height*2/100, 0, 0);
		this.addView(searchtxt,txtParams);
		
		ImageButton searchbtn = new ImageButton(context);
		LayoutParams srhbtnParams  = new LayoutParams(scr_width*13/100, scr_height*8/100);
		srhbtnParams.setMargins(scr_width*3/200, scr_height*10/100, 0, 0);
		searchbtn.setBackgroundColor(ColorManager.MainBgColor);
		searchbtn.setOnTouchListener(searchListener);
		this.addView(searchbtn,srhbtnParams);
		
		
		
		
		
		scanbtn = new ImageButton(context);
		LayoutParams scanbtnParams  = new LayoutParams(scr_width*13/100, scr_height*8/100);
		scanbtnParams.setMargins(scr_width*31/200, scr_height*10/100, 0, 0);
		scanbtn.setBackgroundColor(ColorManager.MainBgColor);
		scanbtn.setOnTouchListener(scanListener);
		this.addView(scanbtn,scanbtnParams);
		
		
		
		seacrchbtntxt = new TextView(context);
		seacrchbtntxt.setText("搜索");
		seacrchbtntxt.setTextColor(Color.WHITE);
		seacrchbtntxt.setGravity(Gravity.CENTER);
		this.addView(seacrchbtntxt,srhbtnParams);
		
		scanbtntxt = new TextView(context);
		scanbtntxt.setText("扫描");
		scanbtntxt.setTextColor(Color.WHITE);
		scanbtntxt.setGravity(Gravity.CENTER);
		this.addView(scanbtntxt,scanbtnParams);
		
		
		myHandler = new MyHandler();
		
			
	}
	
	OnTouchListener searchListener =  new OnTouchListener(){     
         @Override    
         public boolean onTouch(View v, MotionEvent event) {     
                 if(event.getAction() == MotionEvent.ACTION_DOWN){     
                         //更改为按下时的背景图片     
//                 	btntxt.setTextColor(bgcolor);
                 	v.setBackgroundColor(Color.GRAY);
                        // v.setBackgroundResource(R.drawable.pressed);     
                 }else if(event.getAction() == MotionEvent.ACTION_UP){     
                         //改为抬起时的图片     
                 	 v.setBackgroundColor(ColorManager.MainBgColor);	
                    String str = searchtxt.getText().toString().trim();
                    if(str.length()>0){
                    	search_processThread(str);
                    }else{
                    	Toast.makeText(contxt, "搜索内容不能为空",Toast.LENGTH_SHORT).show();
                    }
//                 	 btntxt.setTextColor(Color.WHITE);
//                         v.setBackgroundResource(R.drawable.released);     
                 }     
         	
                 return false;     
         }     
 };  
 
	 private void search_processThread(final String name){
	     //构建一个下载进度条
	     pd= ProgressDialog.show(contxt, "Load", "Loading Data…");
	     new Thread(){
	        @Override
			public void run(){
	           //在新线程里执行长耗时方法
	        	if(name.length()>=1){
	        		search_longTimeMethod(name);
	        		//执行完毕后给handler发送一个空消息 
	        		handler.sendEmptyMessage(0);
	        	}else{
	        		 Message msg = new Message();
	                 Bundle b = new Bundle();// 存放数据
	                 b.putInt("action", 1);
	                 msg.setData(b);
	                 handler.sendMessage(msg);
	        	}
	        }		
	     }.start();
	}
	
	
	private void search_longTimeMethod(String name) { 
		List tmplist,glist=new ArrayList();
		tmplist = WSMethod.getProductByname(name);
		DataCenter.ItemList = tmplist;
		 int i,j=tmplist.size();
		 String[] titles = new String[j];
		 String[] images = new String[j];
		 String[] description = new String[j]; 
		 
		 for(i=0;i<tmplist.size();i++){
			 List tmp = (List) tmplist.get(i);
			 System.out.println("list size="+tmp.size());
			 titles[i] = (String) tmp.get(3);
			 if(titles[i].length()>8)titles[i] = titles[i].substring(0, 8)+"...";
			 images[i] =  (String) tmp.get(0);
			 images[i] = "http://"+Config.Server_imgIP+"/"+images[i]+".jpg";
			 String str = (String)tmp.get(5);
			 int k = (int) Double.parseDouble(str);
			 description[i] = "￥"+k+"";
		 }
		 glist.add(titles);
		 glist.add(images);
		 glist.add(description);	 
		 gadapter = new GridItemAdapter((String[])glist.get(0), (String[])glist.get(1),(String[])glist.get(2),contxt); 
	//	 GridItemAdapter adapter = new GridItemAdapter(DataCenter.titles, DataCenter.images,DataCenter.description,contxt); 
		     
		
	}
	private OnTouchListener scanListener=new OnTouchListener(){

		

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			
			
			 if(event.getAction() == MotionEvent.ACTION_DOWN){     
                 //更改为按下时的背景图片     
				 v.setBackgroundColor(Color.GRAY);
           
			 }else if(event.getAction() == MotionEvent.ACTION_UP){     
                 //改为抬起时的图片     
				 v.setBackgroundColor(ColorManager.MainBgColor);	
			    if(Config.RFID_ENABLE){
					if(isStop){
						isStop = false;
						new ScanThread().start();
						scanbtn.setBackgroundColor(Color.GRAY);
						scanbtntxt.setText("正在扫描...");
					}else{
						isStop = true;
						scanbtn.setBackgroundColor(ColorManager.MainBgColor);
						scanbtntxt.setText("扫描");
					}
				}
			 }     
			
			return false;
		}
	};
	 private void scan_processThread(final String name){
	     //构建一个下载进度条
	     pd= ProgressDialog.show(contxt, "Load", "Loading Data…");
	     new Thread(){
	        @Override
			public void run(){
	           //在新线程里执行长耗时方法
	        	if(name.length()>=1){
	        		scan_longTimeMethod(name);
	        		//执行完毕后给handler发送一个空消息 
	        		handler.sendEmptyMessage(0);
	        	}else{
	        		 Message msg = new Message();
	                 Bundle b = new Bundle();// 存放数据
	                 b.putInt("action", 1);
	                 msg.setData(b);
	                 handler.sendMessage(msg);
	        	}
	        }		
	     }.start();
	}
	
	
	private void scan_longTimeMethod(String name) { 
		List tmplist=new ArrayList(),glist=new ArrayList();
		List temp = WSMethod.getProductById(name);
		if(temp!=null)
		tmplist.add(temp);
		DataCenter.ItemList = tmplist;
		 int i,j=tmplist.size();
		 String[] titles = new String[j];
		 String[] images = new String[j];
		 String[] description = new String[j]; 
		 
		 for(i=0;i<tmplist.size();i++){
			 List tmp = (List) tmplist.get(i);
			 System.out.println("list size="+tmp.size());
			 titles[i] = (String) tmp.get(3);
			 if(titles[i].length()>8)titles[i] = titles[i].substring(0, 8)+"...";
			 images[i] =  (String) tmp.get(0);
			 images[i] = "http://"+Config.Server_imgIP+"/"+images[i]+".jpg";
			 String str = (String)tmp.get(5);
			 int k = (int) Double.parseDouble(str);
			 description[i] = "￥"+k+"";
		 }
		 glist.add(titles);
		 glist.add(images);
		 glist.add(description);	 
		 gadapter = new GridItemAdapter((String[])glist.get(0), (String[])glist.get(1),(String[])glist.get(2),contxt); 
	//	 GridItemAdapter adapter = new GridItemAdapter(DataCenter.titles, DataCenter.images,DataCenter.description,contxt); 
		     
		
	}
	
	
	
	
	
	
	
	
	
	
	private void First_Dir(Context context) {
		// TODO Auto-generated method stub

		/***************************************Spinner***************************************************/
	       // mySpinner = (Spinner)findViewById(R.id.spinner_City); 
	        mySpinner = new Spinner(context);
	        //LayoutParams spinnerParams  = new LayoutParams(scr_width*30/100, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	        LayoutParams spinnerParams  = new LayoutParams(scr_width*31/100+1, scr_height*13/100);
	        spinnerParams.setMargins(0, scr_height*20/100, 0, 0);
	       
	        mySpinner.setLayoutParams(spinnerParams);
	        mySpinner.setBackgroundResource(R.drawable.first_dir);
	        
	       
	        
	        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。 
	      try{ 
	    	  adapter1 = new ArrayAdapter<String>(context,R.layout.spinner, (List) DataCenter.Dirlist_show.get(0)); 
	        //第三步：为适配器设置下拉列表下拉时的菜单样式。 
	        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	        //第四步：将适配器添加到下拉列表上 
	         }catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	        mySpinner.setAdapter(adapter1);  
	        
	        this.addView(mySpinner);
		
	        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中 
	        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){ 
	            @Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
	                // TODO Auto-generated method stub 
	                /* 将所选mySpinner 的值带入myTextView 中*/ 
	               // myTextView.setText("您选择的是："+ adapter1.getItem(arg2)); 
	                /* 将mySpinner 显示*/ 
	                //arg0.setVisibility(View.VISIBLE); 
//	            	ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(contxt, R.layout.second_dir_vlist,(List)DataCenter.Dirlist.get(arg2+1));
//	            	listview_2.setAdapter(adapter_2);
	            	//data = (ArrayList<String>) DataCenter.Dirlist.get(arg2+1);
	            	
	            	DIR_1=arg2+1;
	            	data.clear();
	            	list  = (List<String>) DataCenter.Dirlist_show.get(arg2+1);
	            	for(int i = 0;i < list.size(); i ++){
	            		data.add(list.get(i));
	            	}
	            	adapter_2.notifyDataSetChanged();
	            	//Second_Dir(contxt,arg2);
	            	
	            } 
	            @Override
				public void onNothingSelected(AdapterView<?> arg0) { 
	                // TODO Auto-generated method stub 
	               // myTextView.setText("NONE"); 
	               // arg0.setVisibility(View.VISIBLE); 
	            } 
	        }); 
	        /*下拉菜单弹出的内容选项触屏事件处理*/ 
	        mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){ 
				@Override
				public boolean onTouch(View v, MotionEvent arg1) {
					// TODO Auto-generated method stub
					// v.setVisibility(View.INVISIBLE); 
					return false;
				} 
	        }); 
	        /*下拉菜单弹出的内容选项焦点改变事件处理*/ 
	        mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){ 
	        @Override
			public void onFocusChange(View v, boolean hasFocus) { 
	        // TODO Auto-generated method stub 
	           // v.setVisibility(View.VISIBLE); 
	        } 
	        }); 
		
		/***************************************Spinner***************************************************/
	}
	ArrayAdapter<String> adapter_2;
	 private void Second_Dir(Context context,int arg) {
		
		 
		// TODO Auto-generated method stub
		 /***************************************List View***************************************************/
			listview_2 = new ListView(context);
			LayoutParams listvParams  = new LayoutParams(scr_width*30/100, android.view.ViewGroup.LayoutParams.FILL_PARENT);
			listvParams.setMargins(0, scr_height*29/100, 0, 0);
			data= new ArrayList<String>();
			try{
				list  = (List<String>) DataCenter.Dirlist_show.get(1);
			}catch(Exception e){
				e.printStackTrace();
			}
        	for(int i = 0;i < list.size(); i++){
        		data.add(list.get(i));
        	}
			adapter_2 = new ArrayAdapter<String>(context, R.layout.second_dir_vlist,data);
			//ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(context, R.layout.second_dir_vlist,(List)DataCenter.Dirlist.get(arg+1));
			listview_2.setAdapter(adapter_2);
			//listview_2.setAdapter(adapter);
			
			
			Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.divider);   
			BitmapDrawable bd= new BitmapDrawable(getResources(), bm); 
			listview_2.setDivider(bd);
			listview_2.setClickable(true);
			listview_2.setBackgroundColor(Color.WHITE);
			
			listview_2.setCacheColorHint(0);
			this.addView(listview_2,listvParams);
			
			listview_2.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int id,
						long arg3) {
					// TODO Auto-generated method stub
					//string[] string[] string[]
					DIR_2=id;
					List tmp = (List) DataCenter.Dirlist.get(DIR_1);
					tmp = (List) tmp.get(DIR_2);
					processThread((String) tmp.get(0));
					//List list = Item_Loading(arg2);
//					GridItemAdapter adapter = new GridItemAdapter((String[])list.get(0), (String[])list.get(1),(String[])list.get(2),contxt); 
////					GridItemAdapter adapter = new GridItemAdapter(DataCenter.titles, DataCenter.images,DataCenter.description,contxt); 
//				    gridView.setAdapter(adapter);   
				}
			});
//		 	}
			/***************************************List View***************************************************/
	}
	
		 
		 
		 
		
	
	//声明变量
	    private Button b1;
	    private ProgressDialog pd;
	    
	    GridItemAdapter gadapter;
	    //定义Handler对象
	    private Handler handler =new Handler(){
	       @Override
	       //当有消息发送出来的时候就执行Handler的这个方法
	       public void handleMessage(Message msg){
	          super.handleMessage(msg);
	          //只要执行到这里就关闭对话框
	          
	          Bundle bundle = msg.getData();
	          int act = bundle.getInt("action");
	          switch(act){
	          case 0:
	        	 
	        	  break;
	          case 1: Toast.makeText(contxt, "搜索内容不能为空",Toast.LENGTH_SHORT).show();
	        	  break;
	          case 2:
	        	  break;
	          case 3:
	        	  break;
	          default : 
	        	 
	        		  break;
	          }
	          pd.dismiss();
	          gridView.setAdapter(gadapter); 
	       }
	    };
	   private void processThread(final String id){
	          //构建一个下载进度条
	          pd= ProgressDialog.show(contxt, "Load", "Loading Data…");
	          new Thread(){
	             @Override
				public void run(){
	                //在新线程里执行长耗时方法
	                longTimeMethod(id);
	                //执行完毕后给handler发送一个空消息 
	                handler.sendEmptyMessage(0);
	             }		
	          }.start();
	    }

	  
		private void longTimeMethod(String id) { 
			List tmplist,glist=new ArrayList();
	    	tmplist = WSMethod.getItemByID(id+"");
	    	DataCenter.ItemList = tmplist;
	    	 int i,j=tmplist.size();
			 String[] titles = new String[j];
			 String[] images = new String[j];
			 String[] description = new String[j]; 
			 
			 for(i=0;i<tmplist.size();i++){
				 List tmp = (List) tmplist.get(i);
				 System.out.println("list size="+tmp.size());
				 titles[i] = (String) tmp.get(3);
				 if(titles[i].length()>8)titles[i] = titles[i].substring(0, 8)+"...";
				 images[i] =  (String) tmp.get(0);
				 if(Config.LabMode){
					 images[i] = "http://192.168.1.120:8080/"+images[i]+".jpg";
				 }else{
					 images[i] = "http://"+Config.Server_imgIP+"/"+images[i]+".jpg";
				 }
				 
				 String str = (String)tmp.get(5);
				 int k = (int) Double.parseDouble(str);
				 description[i] = "￥"+k+"";
			 }
			 glist.add(titles);
			 glist.add(images);
			 glist.add(description);	 
			 gadapter = new GridItemAdapter((String[])glist.get(0), (String[])glist.get(1),(String[])glist.get(2),contxt); 
//			 GridItemAdapter adapter = new GridItemAdapter(DataCenter.titles, DataCenter.images,DataCenter.description,contxt); 
			     
	    	
		}
	    
	private LayoutInflater inflater;  
	GridView gridView;
	private void Third_Dir(Context context,int arg) {
		// TODO Auto-generated method stub
	
			/***************************************Grid View***************************************************/
			inflater = LayoutInflater.from(context);   
			this.removeView(gridView); 
			LayoutParams gridvParams  = new LayoutParams(scr_width*68/100, android.view.ViewGroup.LayoutParams.FILL_PARENT);
			gridView = (GridView) inflater.inflate(R.layout.grid_view, null);
			gridView.setLayoutParams(new GridView.LayoutParams(scr_width*70/100, android.view.ViewGroup.LayoutParams.FILL_PARENT));
			gridvParams.setMargins(scr_width*32/100, 0, 0, 0);

			
			DIR_2=0;
			try{
				List tmp = null;
			
				tmp = (List) DataCenter.Dirlist.get(DIR_1);
				tmp = (List) tmp.get(DIR_2);
				processThread((String) tmp.get(0));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		   // GridItemAdapter adapter = new GridItemAdapter(DataCenter.titles, DataCenter.images,DataCenter.description,context); 
		  //  gridView.setAdapter(adapter);   
		    gridView.setDrawingCacheEnabled(true);
		    this.addView(gridView,gridvParams);
		    gridView.setOnItemClickListener(new OnItemClickListener()   
		    {   
		    	@Override  
		    	public void onItemClick(AdapterView<?> parent, View v, int position, long id)   
		    	{   
		    		Intent intent = new Intent();
		    		intent.putExtra("ID", position);
					intent.setClass(contxt, CommodityActivity.class);
					contxt.startActivity(intent);
		    	}  
		    });   
		    
		    /***************************************Grid View***************************************************/
	}

	
	
	
	public class ScanThread extends Thread{
		@Override
		public void run(){
			Scanner.openning();
			if(Scanner.connresult==0)isStop = false;
			else isStop = true;
			while(true){
				if(isStop)break;
				Scanner.inventory();
				//mTxt.setText(Scanner.gEPClist[0]+" ");
				 Message msg = new Message();
//		            Bundle b = new Bundle();// 存放数据
//		            b.putString("color", "我的");
//		            msg.setData(b);
		            myHandler.sendMessage(msg); // 

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Scanner.closing();
			
		}
	}
	class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法,接受数据
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            // 此处可以更新UI
//            Bundle b = msg.getData();
//            String color = b.getString("color");
            if(Scanner.isFoundEPC){
            	isStop = true;
            	scanbtn.setBackgroundColor(ColorManager.MainBgColor);
            	scanbtntxt.setText("扫描");
            	//searchtxt.setText(Scanner.gEPClist[0]+" ");
            	scan_processThread(Scanner.gEPClist[0]);
            }
            

        }
    }
}

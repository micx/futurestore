/**
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.futureshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjtu.micx.URIBitmap.BitmapFact;
import com.sjtu.micx.config.ColorManager;
import com.sjtu.micx.config.UserInfoManager;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.webservice.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewLayoutB extends RelativeLayout{
	private ListView infolistview,recordslistview,noteslistview;
	private Context contxt;
	private TextView mTxt;
	private boolean isStop =true;
	private Button btn;
	private int scr_width = DataCenter.Width;
	private int scr_height = DataCenter.Height - 55;
	private int btnheight  = scr_height*10/100; 
	private int margin	   = scr_height*5/100;
	private int info_width  = scr_width*35/100-margin+5;
	private int info_height = scr_height-btnheight-margin*3;
	private int content_width  = scr_width*65/100-margin*3;
	private int content_height = scr_height-margin*2;
	
	public ViewLayoutB(Context context) {
		super(context);
		contxt = context;
		initLayout(context);
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context) {
		// TODO Auto-generated method stub
		int type = 0;
		backGroudLayout(context,type);		
		customer_info(context,type);
		
			btn_options(context,type);
			Consultant_notes(context,type);
		
		Consumer_records(context,type);
		
	}
	
	private void customer_info(Context context, int type) {
		// TODO Auto-generated method stub
		/*************************************************BackGround*************************************************/
		ImageView bg = new ImageView(context);
		LayoutParams bgParams  = new LayoutParams(info_width+margin*2, scr_height);
		bg.setBackgroundColor(Color.WHITE);
		bg.setLayoutParams(bgParams);
		
		ImageView bg_divider = new ImageView(context);
		LayoutParams bgdParams  = new LayoutParams(2, scr_height);
		bgdParams.setMargins(info_width+margin*2, 0, 0, 0);
		bg_divider.setBackgroundColor(Color.GRAY);
		bg_divider.setLayoutParams(bgdParams);
		
		this.addView(bg);
		//this.addView(bg_divider);
		
		
		/*************************************************BackGround*************************************************/
		 /***************************************List View***************************************************/
		infolistview = new ListView(context);
		LayoutParams listvParams  = new LayoutParams(info_width, info_height);
		listvParams.setMargins(margin, margin, 0, 0);
		SimpleAdapter adapter = new SimpleAdapter(context,getData(),R.layout.customer_info,new String[]{"title","info"},new int[]{R.id.title,R.id.info});	
		infolistview.setAdapter(adapter);
		infolistview.setClickable(true);
		//infolistview.setBackgroundColor(Color.WHITE);
		infolistview.setCacheColorHint(0);
		Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.divider);   
		BitmapDrawable bd= new BitmapDrawable(getResources(), bm); 
		
		infolistview.setDivider(bd);
		this.addView(infolistview,listvParams);		
		infolistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		/***************************************List View***************************************************/	
	}
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 
	     Map<String, Object> map = new HashMap<String, Object>();
	        
	        map.put("title", "姓 名");
	        map.put("info", UserInfoManager.get_user_name());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "性别");
	        map.put("info", UserInfoManager.get_user_gender());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "ID");
	        map.put("info", UserInfoManager.get_user_id());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "类 型");
	        map.put("info", UserInfoManager.get_user_type());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "消费总额");
	        map.put("info", UserInfoManager.get_user_consume_sum());
	        list.add(map);
	        
	        
	        return list;
        
    }
	
	
	private TextView    recordsTxt ;
	private TextView	notesTxt   ;
	private void btn_options(Context context, int type) {
		// TODO Auto-generated method stub
		
		LayoutParams recordsParams;
		if(DataCenter.LoginType == 1){
			ImageButton consultant_notes_btn = new ImageButton(context);
			notesTxt   = new TextView(context);
			notesTxt.setText("笔记");
			notesTxt.setGravity(Gravity.CENTER);
			notesTxt.setTextColor(Color.WHITE);
			LayoutParams notesParams  = new LayoutParams((info_width+margin*2)/2-2, btnheight*4/3);
			recordsParams  = new LayoutParams((info_width+margin*2)/2-2, btnheight*4/3);
			notesParams.setMargins((info_width+margin*2)/2, info_height+margin*2, 0, 0);
			consultant_notes_btn.setBackgroundColor(ColorManager.MainBgColor);
			this.addView(consultant_notes_btn,notesParams);
			this.addView(notesTxt,notesParams);
			consultant_notes_btn.setOnTouchListener(notesListener);
		}else{
			recordsParams = new LayoutParams((info_width+margin*2), btnheight*4/3);
		}
		
		ImageButton consumer_records_btn = new ImageButton(context);
		recordsTxt = new TextView(context);
		recordsTxt.setText("消费记录");
		recordsTxt.setGravity(Gravity.CENTER);
		recordsTxt.setTextColor(Color.WHITE);		
				
		recordsParams.setMargins(1, info_height+margin*2, 0, 0);		
		consumer_records_btn.setBackgroundColor(ColorManager.MainBgColor);		
		this.addView(consumer_records_btn,recordsParams);		
		this.addView(recordsTxt,recordsParams);	
		consumer_records_btn.setOnTouchListener(recordsListener);
	}
	OnTouchListener recordsListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setBackgroundColor(Color.GRAY);

				
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				v.setBackgroundColor(ColorManager.MainBgColor);
				
				processThread(DataCenter.UserID);
				
				show_view(0);
			}
			return false;
		}
		
	};
	
	//声明变量
    private Button b1;
    private ProgressDialog pd;
    
    SimpleAdapter records_adapter;
    //定义Handler对象
    private Handler handler =new Handler(){
       @Override
       //当有消息发送出来的时候就执行Handler的这个方法
       public void handleMessage(Message msg){
          super.handleMessage(msg);
          //只要执行到这里就关闭对话框         
//          Bundle bundle = msg.getData();
//          int act = bundle.getInt("action");
          recordslistview.setAdapter(records_adapter);
          pd.dismiss();
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
		List list;
		
			list = WebService.get_cosume_orders(id);
			DataCenter.Cosume_orders = list;
		
		
		 List orderlist = new ArrayList();
        for(int i=0;i<list.size();i++){
        	List tmplist = (List) list.get(i);
        	HashMap<String, Object> map1 = new HashMap<String, Object>();
        	String imageFile = "/sdcard/Futurestore/images/"+tmplist.get(0)+".jpg";
        	//map1.put("img", imageFile);
        	map1.put("img", BitmapFact.getBitmapOpts(null, 100*100));
        	String str = (String) tmplist.get(3);
        	if(str.length()>=8)str = str.substring(0,8)+"...";
        	map1.put("title", str);
        	map1.put("price", "￥"+tmplist.get(19));
        	map1.put("num", tmplist.get(20));
        	orderlist.add(map1);
        }
        DataCenter.Records_Orders = orderlist;       
		records_adapter = new SimpleAdapter(contxt,DataCenter.Records_Orders,R.layout.records_vlist,new String[]{"title","price","num"},new int[]{R.id.title,R.id.price,R.id.num});
		//records_adapter = new ImageSimpleAdapter(contxt,DataCenter.Orders,R.layout.records_vlist,new String[]{"img","title","price","num"},new int[]{R.id.img,R.id.title,R.id.price,R.id.num});

    	
	}
	
	
	
	
	OnTouchListener notesListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setBackgroundColor(Color.GRAY);
//				notesTxt.setTextColor(bgcolor);
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				v.setBackgroundColor(ColorManager.MainBgColor);
//				notesTxt.setTextColor(Color.WHITE);
//				noteslistview.bringToFront();
				show_view(1);
				
			}
			return false;
		}
		
	};
	
	private void show_view(int tag){
		this.removeView(noteslistview);
		this.removeView(recordslistview);
		if(tag == 0){
			
			this.addView(recordslistview);
		}else{
			this.addView(noteslistview);
			
		}
	}
	private void Consumer_records(Context context, int type) {
		// TODO Auto-generated method stub
		
		/***************************************List View
		 * @param type 
		 * @param context
		 ****************************************************/
		recordslistview = new ListView(context);
		LayoutParams listvParams  = new LayoutParams(content_width, content_height);
		listvParams.setMargins(info_width+margin*3,  margin, 0, 0);
//		ImageSimpleAdapter adapter = new ImageSimpleAdapter(context,DataCenter.Orders,R.layout.records_vlist,new String[]{"img","title","price","num"},new int[]{R.id.img,R.id.title,R.id.price,R.id.num});
		//ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(context, R.layout.second_dir_vlist,(List)DataCenter.Dirlist.get(arg+1));
//		recordslistview.setAdapter(adapter);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.divider);
		BitmapDrawable divider= new BitmapDrawable(getResources(), bm); 
		recordslistview.setDivider(divider);
		//recordslistview.setAdapter(adapter);
		recordslistview.setClickable(true);
		//recordslistview.setBackgroundColor(Color.WHITE);
		recordslistview.setCacheColorHint(0);
		recordslistview.setLayoutParams(listvParams);
		this.addView(recordslistview);		
		recordslistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				Toast.makeText(contxt, arg2+" "+arg3,
//					     Toast.LENGTH_SHORT).show();
//				Intent intent = new Intent();
//	    		intent.putExtra("ID", arg2);
//				intent.setClass(contxt, CommodityActivity.class);
//				contxt.startActivity(intent);
				
			}
		});
		/***************************************List View***************************************************/
	}
	
	private void Consultant_notes(Context context, int type) {
		// TODO Auto-generated method stub
		/***************************************List View***************************************************/
		noteslistview = new ListView(context);
		
		LayoutParams listvParams  = new LayoutParams(content_width, content_height);
		listvParams.setMargins(info_width+margin*3,  margin, 0, 0);
		SimpleAdapter adapter = new SimpleAdapter(context,getrecordsData(),R.layout.customer_records,new String[]{"title","info"},new int[]{R.id.title,R.id.info});	
		noteslistview.setAdapter(adapter);
		noteslistview.setClickable(true);
		//noteslistview.setBackgroundColor(Color.WHITE);
		noteslistview.setCacheColorHint(0);
		Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.divider);   
		BitmapDrawable bd= new BitmapDrawable(getResources(), bm); 
		
		noteslistview.setDivider(bd);
		noteslistview.setLayoutParams(listvParams);
		//this.addView(noteslistview);		
		noteslistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		/***************************************List View***************************************************/	
		
	}
	private List<Map<String, Object>> getrecordsData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 
	     Map<String, Object> map = new HashMap<String, Object>();
	        
	     	map.put("title", "顾客要求");
	        map.put("info", UserInfoManager.get_user_demand());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "客户印象（性格）");
	        map.put("info", UserInfoManager.get_user_impress());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "同来朋友");
	        map.put("info", UserInfoManager.get_user_friends());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "浏览记录、消费习惯");
	        map.put("info", UserInfoManager.get_user_habit());
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "其他");
	        map.put("info", UserInfoManager.get_user_others());
	        list.add(map);
	        
	        
	        return list;
        
    }

	private void backGroudLayout(Context context, int type) {
		// TODO Auto-generated method stub
		LayoutParams relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		ImageView img = new ImageView(context);
		img.setBackgroundResource(R.drawable.bgshadow);
		this.addView(img,relativeParams);
		
	}

}
/**
*    This is Commodity Detail of app.
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.detail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjtu.micx.URIBitmap.BitmapFact;
import com.sjtu.micx.config.Config;
import com.sjtu.micx.config.ItemManager;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.flip.MianActivity;
import com.sjtu.micx.futureshop.R;
import com.sjtu.micx.futureshop.ViewLayoutC;
import com.sjtu.micx.media.ImgBrowser;
import com.sjtu.micx.media.VideoPlay;
import com.sjtu.micx.socket.SocketMethod;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

@SuppressLint("ParserError")
public class CommodityActivity extends Activity {
    /** Called when the activity is first created. */
	private ListView listview_2;
	private ImageView imgView=null,imgBtnPic ,imgBtnVideo ,imgBtnMP3 , imgBtnCart;
	private MediaPlayer mediaPlayer; 
	private Context contxt;
	private int scr_width = DataCenter.Width;			//屏幕宽
	private int scr_height= DataCenter.Height;			//屏幕高
	private int Img_Width 	= scr_width*45/100;			//中间图片宽
	private int Info_Width 	= scr_width*45/100;			//中间图片宽
	private int margin		= (scr_height*3/100);//内容间隔
	private int Height		= (scr_height-margin-55);	//显示部分高度
	private int btnwidth = scr_width*25/100;
	private int btnheight = scr_height*13/100;
	private RelativeLayout layout ;
	private int ID = 0;
	private String filename;
	private int position=0;
	private int bgcolor = Color.rgb(0xb8, 0x3d, 0x24);
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	Intent intent = this.getIntent();
    	ID = intent.getIntExtra("ID", 0);
    	data_init();
    	
    	super.onCreate(savedInstanceState);
    	layout = new RelativeLayout(this);
    	layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	setContentView(layout);
    	mediaPlayer = new MediaPlayer();  
    	
    	ViewLayoutA(this);
    }       
    private void data_init(){
    	filename = "/Futurestore/mp3/"+ItemManager.getInfoByID_num(ID, 0)+".mp3";
    }
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	
		super.onDestroy();
	}
	private OnClickListener loginListener=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			
		}
	};
	
	public void ViewLayoutA(Context context) {
		initLayout(context,0);
		contxt = context;
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context,int type) {
		// TODO Auto-generated method stub		
		backGroudLayout(context,type);		
		imgLayout(context,type);			
		Info_List(context,type);
		Media_Btn(context,type);
	}
	
	
	
	private void Media_Btn(Context context, int type) {
		// TODO Auto-generated method stub
		
		final ImageButton imgBtnPic = new ImageButton(context);
		final ImageButton imgBtnVideo = new ImageButton(context);
		final ImageButton imgBtnMP3 = new ImageButton(context);
		final ImageButton imgBtnCart = new ImageButton(context);
		
		imgBtnPic.setBackgroundColor(bgcolor);
		imgBtnPic.setImageResource(R.drawable.pic_1);
		imgBtnVideo.setBackgroundColor(bgcolor);
		imgBtnVideo.setImageResource(R.drawable.video_1);
		imgBtnMP3.setBackgroundColor(bgcolor);
		imgBtnMP3.setImageResource(R.drawable.music_1);
		imgBtnCart.setBackgroundColor(bgcolor);
		imgBtnCart.setImageResource(R.drawable.cart_1);
		
		LayoutParams picParams  = new LayoutParams(btnwidth, btnheight);
		LayoutParams videoParams  = new LayoutParams(btnwidth, btnheight);
		LayoutParams MP3Params  = new LayoutParams(btnwidth, btnheight);
		LayoutParams cartParams  = new LayoutParams(btnwidth, btnheight);
		
		picParams.setMargins(0, scr_height-btnheight-margin, 0, 0);
		videoParams.setMargins(btnwidth, scr_height-btnheight-margin, 0, 0);
		MP3Params.setMargins(btnwidth*2, scr_height-btnheight-margin, 0, 0);
		cartParams.setMargins(btnwidth*3, scr_height-btnheight-margin, 0, 0);
		
		layout.addView(imgBtnPic,	picParams);
		layout.addView(imgBtnVideo,	videoParams);
		layout.addView(imgBtnMP3,	MP3Params);
		layout.addView(imgBtnCart,	cartParams);		
		
		imgBtnCart.setOnTouchListener(new OnTouchListener(){     
            @Override    
            public boolean onTouch(View v, MotionEvent event) {     
                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
                            //更改为按下时的背景图片     
                    	v.setBackgroundColor(Color.WHITE);
                    	imgBtnCart.setImageResource(R.drawable.cart_0);
                           // v.setBackgroundResource(R.drawable.pressed);     
                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
                            //改为抬起时的图片     
                    	v.setBackgroundColor(bgcolor);
                    	imgBtnCart.setImageResource(R.drawable.cart_1);
                    	
                    	int i = (int) Double.parseDouble(ItemManager.getInfoByID_num(ID, 4));
                    	if(i == 0){
                    		Toast.makeText(getApplicationContext(), "抱歉,该商品库存不足,请选购其他商品",Toast.LENGTH_SHORT).show();
                    	}
                    	else{
                    		cart_dialog();
                    	}
                         // v.setBackgroundResource(R.drawable.released);     
                    }     
                    return false;     
            }     
    }); 
		
		imgBtnPic.setOnTouchListener(new OnTouchListener(){     
            @Override    
            public boolean onTouch(View v, MotionEvent event) {     
                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
                            //更改为按下时的背景图片     
                    	v.setBackgroundColor(Color.WHITE);
                    	imgBtnPic.setImageResource(R.drawable.pic_0);
                           // v.setBackgroundResource(R.drawable.pressed);     
                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
                            //改为抬起时的图片     
                    	v.setBackgroundColor(bgcolor);
                    	imgBtnPic.setImageResource(R.drawable.pic_1);
                    	Intent intent = new Intent(contxt,ImgBrowser.class);
            			intent.putExtra("IMG", ID);
            			contxt.startActivity(intent);
                           // v.setBackgroundResource(R.drawable.released);     
                    }     
                    return false;     
            }     
    }); 
		imgBtnVideo.setOnTouchListener(new OnTouchListener(){     
            @Override    
            public boolean onTouch(View v, MotionEvent event) {     
                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
                            //更改为按下时的背景图片     
                    	v.setBackgroundColor(Color.WHITE);
                    	imgBtnVideo.setImageResource(R.drawable.video_0);
                           // v.setBackgroundResource(R.drawable.pressed);     
                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
                            //改为抬起时的图片     
                    	v.setBackgroundColor(bgcolor);
                    	imgBtnVideo.setImageResource(R.drawable.video_1);
                    	video_dialog();
                    	
                           // v.setBackgroundResource(R.drawable.released);     
                    }     
                    return false;     
            }     
    }); 
		imgBtnMP3.setOnTouchListener(new OnTouchListener(){     
            @Override    
            public boolean onTouch(View v, MotionEvent event) {     
                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
                            //更改为按下时的背景图片     
                    	v.setBackgroundColor(Color.WHITE);
                    	imgBtnMP3.setImageResource(R.drawable.music);
                           // v.setBackgroundResource(R.drawable.pressed);     
                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
                            //改为抬起时的图片     
                    	
                    	if(mediaPlayer.isPlaying()){
                    		
                    		v.setBackgroundColor(bgcolor);
                    		imgBtnMP3.setImageResource(R.drawable.music_1);
                    		position = mediaPlayer.getCurrentPosition();
                    		mediaPlayer.stop();
                    		
                    	}else {
                    		v.setBackgroundColor(bgcolor);
                    		imgBtnMP3.setImageResource(R.drawable.music_2);
                    		try {
								play();
								mediaPlayer.seekTo(position);  
				                position = 0; 
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                    	}
                           // v.setBackgroundResource(R.drawable.released);     
                    }     
                    return false;     
            }     
    }); 	
	}
	protected void video_dialog() {
		  AlertDialog.Builder builder = new Builder(contxt);
		  builder.setMessage("选择本地/远程播放视频？");
		  builder.setTitle("提示");
		  
		  
		  
		  
		  builder.setPositiveButton("本地", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    Intent intent = new Intent(contxt,VideoPlay.class);
		    intent.putExtra("video", ItemManager.getInfoByID_num(ID, 0));
			contxt.startActivity(intent);
		    //加入购物车
		    //Toast.makeText(getApplicationContext(), "商品已加入购物车",Toast.LENGTH_SHORT).show();
		   }
		  });
		  builder.setNegativeButton("远程", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    longprocessThread(Config.TV1_IP,ItemManager.getInfoByID_num(ID, 0)+".mp4");
		    longprocessThread(Config.TV2_IP,ItemManager.getInfoByID_num(ID, 0)+".mp4");
		    
		   //取消加入购物车
//		    Toast.makeText(getApplicationContext(), "请观看大屏幕视频"+ItemManager.getInfoByID_num(ID, 0),Toast.LENGTH_SHORT).show();
		    Toast.makeText(getApplicationContext(), "请观看大屏幕视频",Toast.LENGTH_SHORT).show();

		   }
		  });
		  builder.setNeutralButton("停止", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			    longprocessThread(Config.TV1_IP,"stop");
			    longprocessThread(Config.TV2_IP,"stop");
			    //加入购物车
			    //Toast.makeText(getApplicationContext(), "商品已加入购物车",Toast.LENGTH_SHORT).show();
			   }
			  });
		  
		  
		  
		  
		  builder.create().show();
	}
	private void longprocessThread(final String address,final String msg){
        //构建一个下载进度条
        new Thread(){
           @Override
		public void run(){
              //在新线程里执行长耗时方法
              //执行完毕后给handler发送一个空消息
        	   try {
				SocketMethod.sendMsg(address, msg);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }		
        }.start();
  }
	
	
	
	protected void cart_dialog() {
		  AlertDialog.Builder builder = new Builder(contxt);
		  builder.setMessage("确定将商品放入购物车？");
		  builder.setTitle("提示");
		  builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    //加入购物车
		    String str = ItemManager.getInfoByID_num(ID, 3);
		    HashMap<String, Object> tmpmap;
		    boolean tag = false;
		    for(int i=0;i<DataCenter.Orders.size();i++){
		    	tmpmap=(HashMap<String, Object>) DataCenter.Orders.get(i);
		    	if(tmpmap.get("title").toString().contains(str))tag = true;
		    }
		    if(!tag){
			    HashMap<String, Object> map1 = new HashMap<String, Object>();
	        	String imageFile = "/sdcard/Futurestore//images/"+ItemManager.getImageByID_local(ID);
	        	//map1.put("img", imageFile);
	        	map1.put("img", BitmapFact.getBitmapOpts(imageFile, 100*100));
	        	
	        	if(str.length()>8)str = str.substring(0 , 8)+"...";
	        	map1.put("title", str);
	        	map1.put("price", (int)Double.parseDouble(ItemManager.getInfoByID_num(ID, 5)));
	        	map1.put("num", "1");
	        	map1.put("id", ItemManager.getInfoByID_num(ID, 0));
	        	DataCenter.Orders.add(map1);
	        	DataCenter.OrderList.add(DataCenter.ItemList.get(ID));
	        	ViewLayoutC.UpdateCartList();
			    Toast.makeText(getApplicationContext(), "商品已加入购物车",Toast.LENGTH_SHORT).show();
		    }else{
		    	Toast.makeText(getApplicationContext(), "商品已在购物车，请在购物车修改数量",Toast.LENGTH_SHORT).show();
		    }
		    
		   }
		  });
		  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   //取消加入购物车
		   }
		  });
		  builder.create().show();
	}
	
	 private void play() throws IOException {  
	        File audioFile = new File(Environment.getExternalStorageDirectory(),filename);  
	        mediaPlayer.reset();  
	        mediaPlayer.setDataSource(audioFile.getAbsolutePath());  
	        mediaPlayer.prepare();  
	        mediaPlayer.start();//播放  
	    } 
	
	 private void Info_List(Context context,int arg) {
				 
			// TODO Auto-generated method stub
			 /***************************************List View***************************************************/
				listview_2 = new ListView(context);
				LayoutParams listvParams  = new LayoutParams(Info_Width, scr_height-btnheight-margin*3);
				listvParams.setMargins(Img_Width+margin*3, margin*2, 0, 0);
				SimpleAdapter adapter = new SimpleAdapter(context,getData(),R.layout.detail_vlist,new String[]{"title","info","info1"},new int[]{R.id.title_0,R.id.info_0,R.id.info_1});	
				listview_2.setAdapter(adapter);
				listview_2.setClickable(true);
				//listview_2.setBackgroundColor(Color.WHITE);
				listview_2.setCacheColorHint(0);

				Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.divider);   
				BitmapDrawable bd= new BitmapDrawable(getResources(), bm); 
				
				listview_2.setDivider(bd);
				layout.addView(listview_2,listvParams);		
				listview_2.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						if(arg2 == 1){
							Intent intent = new Intent(contxt,MianActivity.class);
	            			intent.putExtra("IMG", ItemManager.getInfoByID_num(ID, 0));
	            			intent.putExtra("index", "_M");
	            			contxt.startActivity(intent);
						}else if(arg2 == 2){
							Intent intent = new Intent(contxt,MianActivity.class);
	            			intent.putExtra("IMG", ItemManager.getInfoByID_num(ID, 0));
	            			intent.putExtra("index", "_A");
	            			contxt.startActivity(intent);
						}else if(arg2 == 3){
							Intent intent = new Intent(contxt,MianActivity.class);
	            			intent.putExtra("IMG", ItemManager.getInfoByID_num(ID, 0));
	            			intent.putExtra("index", "_D");
	            			contxt.startActivity(intent);
						}
						
						
//				    	Toast.makeText(getApplicationContext(), arg2+"-"+arg3,Toast.LENGTH_SHORT).show();

					}
				});
				/***************************************List View***************************************************/
		}
	 private List<Map<String, Object>> getData() {
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 
	     Map<String, Object> map = new HashMap<String, Object>();
	        
	        map.put("title", "名 称");
	        map.put("info", ItemManager.getInfoByID_num(ID, 3));
	        list.add(map);
	 
//	        map = new HashMap<String, Object>();
//	        map.put("title", "库 存");
//	        map.put("info", ItemManager.getInfoByID_num(ID, 4));
//	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "价 格");
	        int price = (int) Double.parseDouble(ItemManager.getInfoByID_num(ID, 5));
	       
	        map.put("info", price);
	        map.put("info1", "详情点击");
	        list.add(map);
	 
	        map = new HashMap<String, Object>();
	        map.put("title", "作者");
	        map.put("info", ItemManager.getInfoByID_num(ID, 9));
	        map.put("info1", "详情点击");
	        list.add(map);
	        
	        map = new HashMap<String, Object>();
	        map.put("title", "描 述");
	        map.put("info", ItemManager.getInfoByID_num(ID, 7));
	        map.put("info1", "详情点击");
	        list.add(map);  
	        return list;
	    }
		 
	
	Bitmap bm = null;
	int url=0;
	String videonum;
	private void imgLayout(Context context, int type) {
		// TODO Auto-generated method stub	
		
		switch(type){
		case 0:
			url = R.drawable.three;
			videonum = 1234+"";
			bm =DataCenter.getImg(getResources(),R.drawable.three);
			break;
		case 1:
			url = R.drawable.two;
			videonum = 123+"";
			bm=DataCenter.getImg(getResources(),R.drawable.two);
			break;
		}
		/**********************************中间图片显示层********************************************/
		layout.removeView(imgView);
		LayoutParams bitmapParams=new LayoutParams(Img_Width, scr_height-btnheight-2*margin);
		imgView  = new ImageView(context);
		//imgView.setImageBitmap(bm);
		try {
//			List tmplist = (List) DataCenter.ItemList.get(ID);
			imgView.setImageURI(getImageURI(ItemManager.getImageByID_web(ID),DataCenter.cache));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imgView.setBackgroundColor(Color.argb(50, 0xff, 0xff, 0xff));
		imgView.setFadingEdgeLength(10);
		bitmapParams.setMargins(margin, margin, 0, 0);	
		layout.addView(imgView,bitmapParams);
		//imgView.setOnClickListener(imgListener);
		/******************************************************************************/	
	}
	
	
	private void backGroudLayout(Context context, int type) {
		// TODO Auto-generated method stub
		/**********************************背景层********************************************/
		LayoutParams relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		ImageView bg = new ImageView(context);
		bg.setImageResource(R.drawable.commoditybg);
		layout.addView(bg,relativeParams);
		layout.setBackgroundColor(Color.GRAY);
		/******************************************************************************/
	}
	
	public Uri getImageURI(String path, File cache) throws Exception {
		String name =path.substring(path.lastIndexOf("/"));
		File file = new File(cache, name);
		// 如果图片存在本地缓存目录，则不去服务器下载 
		if (file.exists()) {
			return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
		} else {
			// 从网络上获取图片
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (conn.getResponseCode() == 200) {

				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
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
}
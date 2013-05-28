/**
*    This is MainActivity of app.
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.futureshop;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.sjtu.micx.URIBitmap.ImageSimpleAdapter;
import com.sjtu.micx.config.ColorManager;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.webservice.WSMethod;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewLayoutC extends RelativeLayout{
	private ImageView imgView=null;
	private Context contxt;
	private int scr_width = DataCenter.Width;			//屏幕宽
	private int scr_height= DataCenter.Height;			//屏幕高
	private int Navi_Width 	= scr_width*22/100;			//左边导航宽
	private int Img_Width 	= scr_width*46/100;			//中间图片宽
	private int Info_Width 	= scr_width*30/100;			//右边信息宽
	private int margin_left = (int) (scr_width*1.5/100);//三个大模块直接的间隔
	private int margin_top 	= (scr_width*4/100);	//顶部间隔
	private int margin		= (int) (scr_width*1.25/100);//内容间隔
	private int Btn_Height	= (scr_height*10/100);	//按钮高
	private int Info_Height	= (scr_height*10/100);	//信息栏高
	private int Height		= (scr_height*72/100);	//显示部分高度
	private ListView cart_listview;
	private static ImageSimpleAdapter adapter;
	private static int total=0;
	private static TextView counttext;
	
	public ViewLayoutC(Context context) {
		super(context);
		initLayout(context,0);
		contxt = context;
		// TODO Auto-generated constructor stub
	}

	private void initLayout(Context context,int type) {
		// TODO Auto-generated method stub		
		backGroudLayout(context,type);		
		Cart_List(context,type);	
		settlement(context,type);
	}
	private void backGroudLayout(Context context, int type) {
		// TODO Auto-generated method stub
		/**********************************背景层********************************************/
		LayoutParams relativeParams=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		ImageView bg = new ImageView(context);
		bg.setBackgroundResource(R.drawable.commoditybg);
		this.addView(bg,relativeParams);
		//this.setBackgroundColor(Color.WHITE);
		
		/******************************************************************************/
	}
	public static void UpdateCartList(){
		adapter.notifyDataSetChanged();
		int i,j=DataCenter.Orders.size();
		int k,l;
		total = 0;
		for(i=0;i<j;i++){
			//Double.parseDouble(string)
			k = (int)Double.parseDouble(DataCenter.Orders.get(i).get("price").toString().replace("￥", ""));
			l = (int)Double.parseDouble(DataCenter.Orders.get(i).get("num").toString());
			total += k*l; 
		}
		counttext.setText("总额："+total);
	}
	 private void Cart_List(Context context,int arg) {

			// TODO Auto-generated method stub
			 /***************************************List View***************************************************/
		 
		 		ImageView carttitle = new ImageView(context);
		 		LayoutParams cartParams  = new LayoutParams(scr_width*90/100, scr_height*10/100);
		 		cartParams.setMargins(scr_width*5/100, 0, 0, 0);
		 		carttitle.setBackgroundColor(Color.rgb(0x92,0x89,0x7a));
				this.addView(carttitle,cartParams);
				
				TextView goods = new TextView(context);
				LayoutParams goodsParams  = new LayoutParams(scr_width*20/100, scr_height*10/100);
				goodsParams.setMargins(scr_width*18/100+30, 0, 0, 0);
				goods.setText("商品");
				goods.setTextColor(Color.WHITE);
				goods.setGravity(Gravity.CENTER_VERTICAL);
				goods.setTextSize(25);
				this.addView(goods,goodsParams);
				
				TextView prices = new TextView(context);
				LayoutParams pricesParams  = new LayoutParams(scr_width*20/100, scr_height*10/100);
				pricesParams.setMargins(scr_width*45/100+25, 0, 0, 0);
				prices.setText("价格");
				prices.setTextColor(Color.WHITE);
				prices.setGravity(Gravity.CENTER_VERTICAL);
				prices.setTextSize(25);
				this.addView(prices,pricesParams);
				
				TextView nums = new TextView(context);
				LayoutParams numsParams  = new LayoutParams(scr_width*20/100, scr_height*10/100);
				numsParams.setMargins(scr_width*70/100, 0, 0, 0);
				nums.setText("数量");
				nums.setTextColor(Color.WHITE);
				nums.setGravity(Gravity.CENTER_VERTICAL);
				nums.setTextSize(25);
				this.addView(nums,numsParams);
				

		 
				cart_listview = new ListView(context);
				LayoutParams listvParams  = new LayoutParams(scr_width*90/100, scr_height*90/100-110);
				listvParams.setMargins(scr_width*5/100,  scr_height*10/100, 0, 0);
				adapter = new ImageSimpleAdapter(context,DataCenter.Orders,R.layout.cart_vlist,new String[]{"img","title","price","num"},new int[]{R.id.img,R.id.title,R.id.price,R.id.num});
				//ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(context, R.layout.second_dir_vlist,(List)DataCenter.Dirlist.get(arg+1));
				cart_listview.setAdapter(adapter);
				Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.divider);
				BitmapDrawable divider= new BitmapDrawable(getResources(), bm); 
				cart_listview.setDivider(divider);
				//cart_listview.setAdapter(adapter);
				cart_listview.setClickable(true);
				//cart_listview.setBackgroundColor(Color.WHITE);
				cart_listview.setCacheColorHint(0);
				this.addView(cart_listview,listvParams);		
				cart_listview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub\
						
					}
				});
				/***************************************List View
				 * @param type 
				 * @param context ***************************************************/
	 }
	 private TextView btntxt;
	 private void settlement(Context context, int type){
		 ImageView bottom = new ImageView(context);
		 LayoutParams botmParams  = new LayoutParams(scr_width, 55);
		 botmParams.setMargins(0, scr_height-110, 0, 0);
		 bottom.setBackgroundColor(ColorManager.MainBgColor);
		 this.addView(bottom,botmParams);
		 
		 counttext = new TextView(context);
		 LayoutParams txtParams  = new LayoutParams(scr_width*55/100, 55);
		 txtParams.setMargins(scr_width*12/100, scr_height-110, 0, 0);
		 counttext.setText("总额：");
		 counttext.setTextSize(scr_height*6/100);
		 counttext.setTextColor(Color.WHITE);
		 counttext.setGravity(Gravity.CENTER_VERTICAL);
		 this.addView(counttext,txtParams);
		 
		 ImageButton btn = new ImageButton(context);
		 btntxt = new TextView(context);
		 ImageView bgdivider = new ImageView(context);
		 
		 btntxt.setText("结算");
		 btntxt.setTextSize(scr_height*6/100);
		 btntxt.setTextColor(Color.WHITE);
		 btntxt.setGravity(Gravity.CENTER);
		 LayoutParams btnParams  = new LayoutParams(scr_width*30/100,  55);
		 btnParams.setMargins(scr_width*70/100, scr_height-110, 0, 0);
		 
		 LayoutParams bgdParams  = new LayoutParams(1,  55);
		 bgdParams.setMargins(scr_width*70/100-1, scr_height-110, 0, 0);
		 bgdivider.setBackgroundColor(Color.WHITE);
		 btn.setBackgroundColor(ColorManager.MainBgColor);		 
		 this.addView(btn,btnParams);
		 this.addView(btntxt,btnParams);
		 this.addView(bgdivider,bgdParams);
		 btn.setOnTouchListener(new OnTouchListener(){     
	            @Override    
	            public boolean onTouch(View v, MotionEvent event) {     
	                    if(event.getAction() == MotionEvent.ACTION_DOWN){     
	                            //更改为按下时的背景图片     
//	                    	btntxt.setTextColor(bgcolor);
	                    	v.setBackgroundColor(Color.GRAY);
	                           // v.setBackgroundResource(R.drawable.pressed);     
	                    }else if(event.getAction() == MotionEvent.ACTION_UP){     
	                            //改为抬起时的图片     
	                    	 summbit_dialog();
	                    	 v.setBackgroundColor(ColorManager.MainBgColor);	
//	                    	 btntxt.setTextColor(Color.WHITE);
//	                            v.setBackgroundResource(R.drawable.released);     
	                    }     
	            	
	                    return false;     
	            }     
	    });  
		 UpdateCartList();
	 }
	 
	 protected void summbit_dialog() {
		  AlertDialog.Builder builder = new Builder(contxt);
		  builder.setMessage("提交订单？");
		  builder.setTitle("提示");
		  builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();  
		    //加入购物车
		    processThread();
		    //use_id;emp_id;order_date;order_price;number;prod_id;price;quantity;prod_id;price;quantity;
		    //Toast.makeText(contxt, "订单已提交",Toast.LENGTH_SHORT).show();
		   }
		  });
		  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   //取消加入购物车
//		    Toast.makeText(contxt, "。。。",Toast.LENGTH_SHORT).show();
		   }
		  });
		  builder.create().show();
	}
	 
	//声明变量
	    private Button b1;
	    private ProgressDialog pd;
	    
	    ImageSimpleAdapter records_adapter;
	    //定义Handler对象
	    private Handler handler =new Handler(){
	       @Override
	       //当有消息发送出来的时候就执行Handler的这个方法
	       public void handleMessage(Message msg){
	          super.handleMessage(msg);
	          //只要执行到这里就关闭对话框         
//	          Bundle bundle = msg.getData();
//	          int act = bundle.getInt("action");
	          pd.dismiss();
	          if(msg.arg1 == 1){
	        	  Toast.makeText(contxt, "订单提交成功",Toast.LENGTH_SHORT).show();
	        	  ViewLayoutC.UpdateCartList();
	          }else if(msg.arg1 == 2){
	        	  Toast.makeText(contxt, "订单提交失败，请重新提交订单",Toast.LENGTH_SHORT).show();
	          }else{
	        	  Toast.makeText(contxt, "购物车为空，请选购商品",Toast.LENGTH_SHORT).show();
	          }
	          
	          
	       }
	    };
	   private void processThread(){
	          //构建一个下载进度条
	          pd= ProgressDialog.show(contxt, "Load", "Loading Data…");
	          new Thread(){
	             @Override
				public void run(){
	            	 Message msg = new Message();
	            	 
	                //在新线程里执行长耗时方法
	            	 int ret = longTimeMethod();
	                
	                	msg.arg1 = ret;
	                	handler.sendMessage(msg);
	               
	                //执行完毕后给handler发送一个空消息 
	                
	             }		
	          }.start();
	    }

	   public String getCurrentDate(){
			Date now = new Date(); 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = dateFormat.format( now ); 
			return str;
			
		}
		private int longTimeMethod() {
				String str="";
			
				str+=DataCenter.UserID+";";
				str+=DataCenter.EmployeID+";";
				str+=getCurrentDate()+";";
				str+=total+";";
				str+=DataCenter.Orders.size()+";";
				
				HashMap<String, Object> map;      
		        List orderlist = new ArrayList();
		        int i,n = DataCenter.Orders.size();
		        
		        if(n==0)return -1;//无货物
		        
		        for(i=0;i<n;i++){
		        	map = (HashMap<String, Object>) DataCenter.Orders.get(i);
		        	str+=map.get("id")+";";
		        	str+=map.get("price")+";";
		        	str+=map.get("num")+";";
		        }
		        boolean ret = WSMethod.updateOrder(str);
		        if(ret){
			        DataCenter.OrderList.clear();
			        DataCenter.Orders.clear();
			        return 1;//提交订单成功
		        }else{
		        	return 2;//提交订单失败
		        }
		        	
		        
		}
	
	 
}

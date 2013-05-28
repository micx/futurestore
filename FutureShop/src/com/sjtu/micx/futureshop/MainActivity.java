/**
*    This is MainActivity of app.
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.futureshop;

import java.io.File;
import java.util.List;

import com.sjtu.micx.UHFReader.Scanner;
import com.sjtu.micx.config.ColorManager;
import com.sjtu.micx.config.Config;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.db.DBAdapter;
import com.sjtu.micx.webservice.WSMethod;
import com.sjtu.micx.webservice.WebService;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private VflipperLayout viewfilpper;
	private ImageButton loginbtn;
	private ImageButton scanbtn;
	private TextView loginbtntxt,scanbtntxt;
//	private EditText passwd;
	private TextView EPCtxtview;
	private boolean isStop = true;
	private MyHandler myHandler;
	private Context contxt;
	private static final int ITEM = Menu.FIRST; 
	private static final int ITEM2 = Menu.FIRST + 1;  
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	        // TODO Auto-generated method stub
	        menu.add(0, ITEM, 1, "����");
	        menu.add(0, ITEM2, 2, "����");
	        return true;
	    }
	 
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		   switch (item.getItemId()) { 
		   case ITEM: 
			   
			   settingdialog();
		     break; 
		 
		   case ITEM2: 
			   //Toast.makeText(getApplicationContext(), "�����2",Toast.LENGTH_SHORT).show(); 
		     break; 
		   } 
		   return true; 
		 
		} 
	 protected void settingdialog() {
			
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.activity_settings_dialog,(ViewGroup) findViewById(R.id.dialog));
			final EditText ServerIP = (EditText) layout.findViewById(R.id.guideID);
			final EditText TV1_IP = (EditText) layout.findViewById(R.id.customID);
			final EditText TV2_IP = (EditText) layout.findViewById(R.id.passwd);
			ServerIP.setText(Config.Server_IP);
			TV1_IP.setText(Config.TV1_IP);
			TV2_IP.setText(Config.TV2_IP);
			  AlertDialog.Builder builder = new Builder(MainActivity.this);
			  builder.setView(layout);
			  builder.setTitle("����");
			  builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			    dbAdapter.updateTitle(1, ServerIP.getText().toString(),TV1_IP.getText().toString(), TV2_IP.getText().toString());
			    Config.Server_IP = ServerIP.getText().toString();
				Config.TV1_IP = TV1_IP.getText().toString();
				Config.TV2_IP = TV2_IP.getText().toString();
//			    DataCenter.LoginType = 0;
//			    loginprocessThread(guideID.getText().toString(),customID.getText().toString(),passwd.getText().toString());
			   }
			  });
			  builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
//			    DataCenter.LoginType = 1;
//			    loginprocessThread(guideID.getText().toString(),customID.getText().toString(),passwd.getText().toString());
			   }
			  });
			  builder.create().show();
		 }
	 
	 
	 
	 
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	dbinit();
    	
    	
    	
    	DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		DataCenter.Height = dm.heightPixels;
		DataCenter.Width = dm.widthPixels;
    	
        super.onCreate(savedInstanceState);
        
        
        RelativeLayout lLayout = new RelativeLayout(this);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(lLayout);	
        contxt = this.getBaseContext();
        goodNet();

        loginbtntxt =new TextView(contxt);
        scanbtntxt  =new TextView(contxt);
        loginbtn	= new ImageButton(contxt);
        scanbtn		= new ImageButton(contxt);
//        passwd		= new EditText(contxt);
        EPCtxtview  = new TextView(contxt); 
        loginbtntxt.setText("�ֶ���¼");
        loginbtntxt.setGravity(Gravity.CENTER);
        loginbtntxt.setTextColor(Color.WHITE);
        scanbtntxt.setText("ɨ���¼");
        scanbtntxt.setGravity(Gravity.CENTER);
        scanbtntxt.setTextColor(Color.WHITE);
        
        RelativeLayout.LayoutParams EPCtxtviewParams = new RelativeLayout.LayoutParams(DataCenter.Width/3,DataCenter.Height/10);
        EPCtxtviewParams.setMargins(DataCenter.Width/2, DataCenter.Height*7/30, 0, 0);
        EPCtxtview.setLayoutParams(EPCtxtviewParams);
        
         RelativeLayout.LayoutParams passwdParams = new RelativeLayout.LayoutParams(DataCenter.Width/3,DataCenter.Height/10);
        passwdParams.setMargins(DataCenter.Width/2, DataCenter.Height*10/30, 0, 0);
//        passwd.setLayoutParams(passwdParams);
        
         RelativeLayout.LayoutParams scanbtnParams = new RelativeLayout.LayoutParams(DataCenter.Width/7+15,DataCenter.Height/10);
        scanbtnParams.setMargins(DataCenter.Width/2, DataCenter.Height*13/30, 0, 0);
        scanbtn.setLayoutParams(scanbtnParams);
        scanbtn.setBackgroundColor(ColorManager.MainBgColor);
        RelativeLayout.LayoutParams loginbtnParams = new RelativeLayout.LayoutParams(DataCenter.Width/7+15,DataCenter.Height/10);
        loginbtnParams.setMargins(DataCenter.Width/2+DataCenter.Width*4/21-15, DataCenter.Height*13/30, 0, 0);
        loginbtn.setLayoutParams(loginbtnParams);
        loginbtn.setBackgroundColor(ColorManager.MainBgColor);
        
        lLayout.setBackgroundResource(R.drawable.bg);
        loginbtntxt.setLayoutParams(loginbtnParams);
        scanbtntxt.setLayoutParams(scanbtnParams);
        
        lLayout.addView(loginbtn);
        lLayout.addView(scanbtn);
//        lLayout.addView(passwd);
        lLayout.addView(EPCtxtview);
        lLayout.addView(loginbtntxt);
        lLayout.addView(scanbtntxt);
        
//        passwd.setText("123");
        //EPCtxtview.setText("EPC");
        loginbtn.setOnTouchListener(loginListener);
        scanbtn.setOnTouchListener(scanbtnListener);
        myHandler = new MyHandler();
    }   
    DBAdapter dbAdapter;
    @SuppressLint("ParserError")
	private void dbinit() {
		// TODO Auto-generated method stub
    	/*
		 * create and open DB
		 */
    	dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
		/*
		 * insert books to DB
		 */
		long id;
		Cursor c = dbAdapter.getTitle(1);
		
		if (!c.moveToFirst())
		{
			id = dbAdapter.insertTitle(
					"192.168.10.120",
					"192.168.10.125",
					"192.168.10.126");
			
			Config.Server_IP = "192.168.10.120";
			Config.Server_imgIP = Config.Server_IP;
			Config.TV1_IP = "192.168.10.125";
			Config.TV2_IP = "192.168.10.126";
		}else{
			
			
			 int isbnColumn = c.getColumnIndex(DBAdapter.KEY_ISBN);
			 int nameColumn = c.getColumnIndex(DBAdapter.KEY_TITLE);
			 int publishColumn = c.getColumnIndex(DBAdapter.KEY_PUBLISHER);
			 int idColumn = c.getColumnIndex(DBAdapter.KEY_ROWID);
//			 mList.add(c.getString(idColumn)+"    "++"    "+c.getString(nameColumn)+"    "+c.getString(publishColumn));
			Config.Server_IP = c.getString(isbnColumn);
			Config.Server_imgIP = Config.Server_IP;
			Config.TV1_IP = c.getString(nameColumn);
			Config.TV2_IP = c.getString(publishColumn);
			
		}
		if(Config.LabMode){
			Config.URL ="http://192.168.1.120:8000/Service1.asmx";
		}else{
			Config.URL ="http://"+Config.Server_IP+":8000/WebService/Service1.asmx"; 
		}
	
		
		
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	isStop = true;
		super.onDestroy();
	}
    
    protected void NetTest_dialog() {
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		  builder.setMessage("�����������ӣ�");
		  builder.setTitle("����������");
		  builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    Intent intent=null;
            //�ж��ֻ�ϵͳ�İ汾  ��API����10 ����3.0�����ϰ汾 
            if(android.os.Build.VERSION.SDK_INT>10){
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            }else{
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            MainActivity.this.startActivity(intent);
            finish();
		   }
		  });
		  builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    finish();

		   }
		  });
		  builder.create().show();
	}
	
    
    public boolean goodNet()
    {  
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();    
        if (networkinfo == null || !networkinfo.isAvailable()) {    
//            new AlertDialog.Builder(this).setMessage("û�п���ʹ�õ�����").setPositiveButton("Ok", null).show();
        	NetTest_dialog();
            return false;  
        }else{
//        	new AlertDialog.Builder(this).setMessage("������������ʹ��").setPositiveButton("Ok", null).show();
	        processThread();
	        return true; 
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //��������
    private ProgressDialog pd;
    //����Handler����
    private Handler handler =new Handler(){
       @Override
       //������Ϣ���ͳ�����ʱ���ִ��Handler���������
       public void handleMessage(Message msg){
          super.handleMessage(msg);
          //ֻҪִ�е�����͹رնԻ���
          pd.dismiss();
       }
    };
    private void processThread(){
          //����һ�����ؽ�����
          pd= ProgressDialog.show(MainActivity.this, "����", "��������С�");
          new Thread(){
             @Override
			public void run(){
                //�����߳���ִ�г���ʱ����
//                longTimeMethod();
                handler.sendEmptyMessage(0);
             }		
          }.start();
    }
    
	private void longTimeMethod() {

    	//DataCenter.data_init();
    	WebService.DIR_init();
		   DataCenter.cache = new File(Environment.getExternalStorageDirectory()+"/Futurestore", "images");
		   if(!DataCenter.cache.exists()){
			   DataCenter.cache.mkdirs();
			           }			
	}
    
    
   
    
    
	 OnTouchListener loginListener=new OnTouchListener(){


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setBackgroundColor(Color.GRAY);

			}else if(event.getAction() == MotionEvent.ACTION_UP){
				v.setBackgroundColor(ColorManager.MainBgColor);
//				String pswd = passwd.getText().toString();
				//loginprocessThread(pswd);	
				dialog("");
			}
			
			return false;
		}
	};
	protected void dialog(String id) {
		
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.activity_main_dialog,(ViewGroup) findViewById(R.id.dialog));
		final EditText guideID = (EditText) layout.findViewById(R.id.guideID);
		final EditText customID = (EditText) layout.findViewById(R.id.customID);
		final EditText passwd = (EditText) layout.findViewById(R.id.passwd);
		guideID.setText(id);
		if(Config.LabMode){
			guideID.setText("13671787881");
			customID.setText("00000000001");
			passwd.setText("09280704");
		}
		  AlertDialog.Builder builder = new Builder(MainActivity.this);
		  builder.setView(layout);
		  builder.setTitle("��¼");
		  builder.setPositiveButton("�ͻ���¼", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    DataCenter.LoginType = 0;
		    loginprocessThread(guideID.getText().toString(),customID.getText().toString(),passwd.getText().toString());
		   }
		  });
		  builder.setNegativeButton("���ʵ�¼", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    DataCenter.LoginType = 1;
		    loginprocessThread(guideID.getText().toString(),customID.getText().toString(),passwd.getText().toString());
		   }
		  });
		  builder.create().show();
	 }
	
	
	
	
	
    //����Handler����

	private Handler loginhandler =new Handler(){
       @Override
       //������Ϣ���ͳ�����ʱ���ִ��Handler���������
       public void handleMessage(Message msg){
          super.handleMessage(msg);
          //ֻҪִ�е�����͹رնԻ���
          Bundle b = msg.getData();
          int act = b.getInt("act");
          if(act == 1 )
          Toast.makeText(getApplicationContext(), "�������",Toast.LENGTH_SHORT).show();
          else if(act == 2 )
              Toast.makeText(getApplicationContext(), "�˺Ų�����",Toast.LENGTH_SHORT).show();
          pd.dismiss();
       }
    };
    private void loginprocessThread(final String guideID, final String customID, final String paswd){
          //����һ�����ؽ�����
          pd= ProgressDialog.show(MainActivity.this, "��¼", "��¼�С�");
          new Thread(){
             @Override
			public void run(){
                //�����߳���ִ�г���ʱ����
            	 loginlongTimeMethod(guideID,customID,paswd);
                //ִ����Ϻ��handler����һ������Ϣ
                loginhandler.sendEmptyMessage(0);
             }		
          }.start();
    }
	private void loginlongTimeMethod(String guideID, String customID, String paswd) {
		
			if(guideID.contentEquals("0")){//�ͻ�ɨ�迨��¼
				List list = WSMethod.getUserById(customID);
				if(list!=null&&list.size()>0){
					DataCenter.userInfoList =list;
					DataCenter.UserID = customID;
					DataCenter.EmployeID = guideID;
					longTimeMethod();
					Intent intent = new Intent(MainActivity.this,NavigationActivity.class);
					startActivity(intent);
	//				finish();
				}else{
					 Message msg = new Message();
			         Bundle b = new Bundle();// �������
			         b.putInt("act", 2);
			         msg.setData(b);
			         MainActivity.this.loginhandler.sendMessage(msg);
				}
			}else{//�ͻ����ʰ�æ��¼
				List guideList = WSMethod.getEmpById(guideID);
				if(guideList!=null&&guideList.size()>0){	
					String password = (String) guideList.get(2);
					if(paswd.contentEquals(password)){	
						List list = WSMethod.getUserById(customID);
						if(list!=null){
							DataCenter.userInfoList =list;
							DataCenter.UserID = customID;
							DataCenter.EmployeID = (String) list.get(1);
							longTimeMethod();
							Intent intent = new Intent(MainActivity.this,NavigationActivity.class);
							startActivity(intent);
			//				finish();
						}else{
							 Message msg = new Message();
					         Bundle b = new Bundle();// �������
					         b.putInt("act", 2);
					         msg.setData(b);
					         MainActivity.this.loginhandler.sendMessage(msg);
						}
					}else{
						//�������
						 Message msg = new Message();
				         Bundle b = new Bundle();
				         b.putInt("act", 1);
				         msg.setData(b);
				         MainActivity.this.loginhandler.sendMessage(msg);
					}
				}else{
					//�����˺Ų�����
					 Message msg = new Message();
			         Bundle b = new Bundle();// �������
			         b.putInt("act", 2);
			         msg.setData(b);
			         MainActivity.this.loginhandler.sendMessage(msg);
					
				}
			}
	}
	
	OnTouchListener scanbtnListener=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setBackgroundColor(Color.GRAY);

			}else if(event.getAction() == MotionEvent.ACTION_UP){
				
				if(Config.RFID_ENABLE){
					if(isStop){
						isStop = false;
						new ScanThread().start();
						scanbtntxt.setText("����ɨ��...");
						v.setBackgroundColor(Color.GRAY);
					}else{
						isStop = true;
						scanbtntxt.setText("��ʼ��¼");
						v.setBackgroundColor(ColorManager.MainBgColor);
					}
				}
			}
			
			
			
			return false;
		}
	};
	
	public class ScanThread extends Thread{
		@Override
		public void run(){
			Scanner.openning();
			if(Scanner.connresult==0)isStop = false;
			else isStop = true;
			while(true){
				if(isStop)break;
				Scanner.inventory();
				 Message msg = new Message();
//		            Bundle b = new Bundle();// �������
//		            b.putString("color", "�ҵ�");
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
        // ���������д�˷���,��������
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            // �˴����Ը���UI
//            Bundle b = msg.getData();
//            String color = b.getString("color");
            //EPCtxtview.setText(Scanner.gEPClist[0]+" ");
            String IDstr = Scanner.gEPClist[0].toString().trim();
            if(IDstr.length()>0){
            	isStop = true;
				scanbtntxt.setText("��ʼɨ��");
				scanbtntxt.setBackgroundColor(ColorManager.MainBgColor);
				if(IDstr.charAt(0) == '1')		//�ͻ���¼	
				{
					DataCenter.LoginType = 0;
//					loginprocessThread("0","1","0");
					IDstr = IDstr.substring(1, 12);
					loginprocessThread("0",IDstr,"0");
				}else{
					IDstr = IDstr.substring(1, 12);
					dialog(IDstr);//���ʵ�¼
				}
				
			}
        }
    }
}
/**
*    This is Navigation of app.
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.futureshop;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class NavigationActivity extends Activity {
    /** Called when the activity is first created. */
	private VflipperLayout viewfilpper;
	private Context contxt;
	private Time  time;
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	time=new Time();
    	contxt = this.getBaseContext();
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewfilpper=new VflipperLayout(this);
        setContentView(viewfilpper);
        new Thread( new Runnable() {     
            @Override
			public void run() {     
                 while(true){
                	 flag = 0;
                	 try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                 }
             }            
        }).start();
    }	 
    private int flag = 0;
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		 if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
            //do something...
			 flag++;
			 if(flag == 2)finish();
			 else{
				 Toast.makeText(getApplicationContext(), "�ٰ�һ�η��ؼ��˳�����",Toast.LENGTH_SHORT).show();
			 }
             return true;
         }
		
		return super.onKeyUp(keyCode, event);
	}
	protected void back_dialog() {
		  AlertDialog.Builder builder = new AlertDialog.Builder(contxt);
		  builder.setMessage("ѡ�񱾵�/Զ�̲�����Ƶ��");
		  builder.setTitle("��ʾ");
		  builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    finish();
		    //���빺�ﳵ
		    //Toast.makeText(getApplicationContext(), "��Ʒ�Ѽ��빺�ﳵ",Toast.LENGTH_SHORT).show();
		   }
		  });
		  builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   //ȡ�����빺�ﳵ
		   }
		  });
		  builder.create().show();
	}
 
}
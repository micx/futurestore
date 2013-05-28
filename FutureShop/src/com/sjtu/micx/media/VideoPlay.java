/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.media;

import java.io.File;

import com.sjtu.micx.futureshop.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlay extends Activity  implements OnCompletionListener
	{
		private Context contxt;
		private VideoView videoView;
		private  String ID;
		private MyHandler myHandler;
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			contxt = this.getBaseContext();
			Intent intent = this.getIntent();
	    	ID = intent.getStringExtra("video");
			
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.videoplay);
			myHandler = new MyHandler();
			new VideoPlayer().run();
		}
		
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}

     	public class VideoPlayer extends Thread{
			@Override
			public void run()
     		{
				videoView = (VideoView) findViewById(R.id.videoView1);
				videoView.setOnCompletionListener(VideoPlay.this);
				try{
					String dir = Environment.getExternalStorageDirectory()+"/Futurestore/video/";//"file:///sdcard/Futurestore/video/";
					String name = ID+".mp4";//"606005.mp4";
					
					name = name.replace("wav","mp4");
					
					File file = new File(dir, name);
					// ���ͼƬ���ڱ��ػ���Ŀ¼����ȥ���������� 
					if (!file.exists()){
						 Message msg = new Message();
				            Bundle b = new Bundle();// �������
				            b.putString("name", name);
				            msg.setData(b);
				            VideoPlay.this.myHandler.sendMessage(msg);
					}else{
						videoView.setVideoURI(Uri.parse(dir+name));
						//videoView.setVideoPath("android.resource://com.homer/"+R.raw.test);
						videoView.setMediaController(new MediaController(VideoPlay.this));
						videoView.start();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			
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
                String name = msg.getData().getString("name");
                 video_dialog(name);

             }
         }
     	
     	
     	
     	protected void video_dialog(String name) {
  		  AlertDialog.Builder builder = new Builder(VideoPlay.this);
  		  builder.setMessage("�Ҳ�����Ƶ�ļ�:"+name);
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
//  		  builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
//  		   @Override
//  		   public void onClick(DialogInterface dialog, int which) {
//  		    dialog.dismiss();
//  		   //ȡ�����빺�ﳵ
//  		   // Toast.makeText(getApplicationContext(), "��ۿ�����Ļ��Ƶ",Toast.LENGTH_SHORT).show();
//  		   }
//  		  });
  		  builder.create().show();
  	}
     	
     	
     	
     	
		@Override
		public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			//if(arg0.getCurrentPosition() == arg0.getDuration())
			this.finish();
			
		}
	
}

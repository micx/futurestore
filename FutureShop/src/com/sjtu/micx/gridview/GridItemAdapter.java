/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.gridview;

  
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtu.micx.URIBitmap.BitmapFact;
import com.sjtu.micx.URIBitmap.Contact;
import com.sjtu.micx.URIBitmap.ContactService;
import com.sjtu.micx.config.Config;
import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.futureshop.R;
  
public class GridItemAdapter extends BaseAdapter  
{  
  
    private LayoutInflater inflater;   
    private List<GridItem> gridItemList;   
    protected static final int SUCCESS_GET_IMAGE = 0;
    private Context context;
    private List<Contact> contacts;
    private File cache = DataCenter.cache;

   
    public GridItemAdapter(String[] titles, String[] images,String[] description, Context context)   
    {   
        super();   
        gridItemList = new ArrayList<GridItem>();   
        inflater = LayoutInflater.from(context);   
        for (int i = 0; i < images.length; i++)   
        {   
            GridItem picture = new GridItem(titles[i], images[i],description[i]);   
            gridItemList.add(picture);   
        }   
    }   
    
    @Override  
    public int getCount( )  
    {  
        if (null != gridItemList)   
        {   
            return gridItemList.size();   
        }   
        else  
        {   
            return 0;   
        }   
    }  
  
    @Override  
    public Object getItem( int position )  
    {  
        return gridItemList.get(position);   
    }  
  
    @Override  
    public long getItemId( int position )  
    {  
        return position;   
    }  
    
    List<Integer> lstPosition=new ArrayList<Integer>();
	List<View> lstView=new ArrayList<View>();
	List<Integer> lstTimes= new ArrayList<Integer>();
	long startTime=0; 
	ViewHolder viewHolder;  
    @Override  
    public View getView( int position, View convertView, ViewGroup parent )  
    {  
    	/*************************************************************************************
    	 *  ViewCache 
    	 ************************************************************************************/
    	if(Config.VIEW_CACHE){
	    	startTime=System.nanoTime();
	    	if (lstPosition.contains(position) == false) {
	    		if(lstPosition.size()>75)//�������û����Item����
	    		{
		    		lstPosition.remove(0);//ɾ����һ��
		    		lstView.remove(0);//ɾ����һ��
	    		}
	    	          
	    		convertView = inflater.inflate(R.layout.grid_item, null);     
	    		TextView  title = (TextView) convertView.findViewById(R.id.title);   
	    		ImageView image = (ImageView) convertView.findViewById(R.id.image);  
	    		TextView  time  = (TextView) convertView.findViewById(R.id.description); 
	    		TextPaint paint =  time.getPaint();  
	    		paint.setFakeBoldText(true);
	    		convertView.setTag(viewHolder);         
	    	    title.setText(gridItemList.get(position).getTitle());  
	    	    time.setText(gridItemList.get(position).getTime()); 
	
	    	    // �첽�ļ���ͼƬ (�̳߳� + Handler ) ---> AsyncTask
	    	    ContactService service = new ContactService();
	    	    AsyncImageTask task = new AsyncImageTask(service, image);
	    	    task.execute(gridItemList.get(position).getImageId());
	    	    convertView.setLayoutParams(new GridView.LayoutParams(LayoutParams.FILL_PARENT, 190));//GridView.LayoutParams.FILL_PARENT
	    	        
	    		lstPosition.add(position);//���������
	    		lstView.add(convertView);//���������
	    	} else {
	    		convertView = lstView.get(lstPosition.indexOf(position));
	    	}
	    	
	    	int endTime=(int) (System.nanoTime()-startTime);
	    	lstTimes.add(endTime);
	    	if(lstTimes.size()==14)
	    	{
	    		int total=0;
	    		for(int i=0;i<lstTimes.size();i++)
	    			total=total+lstTimes.get(i);
	    		Log.i("10��������ʱ�䣺" +total/1000 +" ��s", "�����ڴ棺"+Runtime.getRuntime().totalMemory()/1024 +" KB");
	    		lstTimes.clear();
	    	}
    	}else{
    	/*************************************************************************************
    	 *  ViewHolder 
    	 ************************************************************************************/
    		 if (convertView == null)   
 	        {   
 	            convertView = inflater.inflate(R.layout.grid_item, null);   
 	            viewHolder = new ViewHolder();   
 	            viewHolder.title = (TextView) convertView.findViewById(R.id.title);   
 	            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);  
 	            viewHolder.time = (TextView) convertView.findViewById(R.id.description); 
 			    TextPaint paint =  viewHolder.time.getPaint();  
 			    paint.setFakeBoldText(true);
 	            convertView.setTag(viewHolder);   
 	        } else  
 	        {   
 	            viewHolder = (ViewHolder) convertView.getTag();   
 	        }   
 	       
 	        viewHolder.title.setText(gridItemList.get(position).getTitle());  
 	        viewHolder.time.setText(gridItemList.get(position).getTime()); 
 	        
 	        //Bitmap bit = BitmapFact.returnBitMapFromSD(gridItemList.get(position).getImageId());  
// 	        Bitmap bit = BitmapFact.returnBitMapFromURI(gridItemList.get(position).getImageId());  
// 	        viewHolder.image.setImageBitmap(bit);   
 	     
 	        // �첽�ļ���ͼƬ (�̳߳� + Handler ) ---> AsyncTask
 	        ContactService service = new ContactService();
 	        AsyncImageTask task = new AsyncImageTask(service, viewHolder.image);
 	        task.execute(gridItemList.get(position).getImageId());
 	        convertView.setLayoutParams(new GridView.LayoutParams(LayoutParams.FILL_PARENT, 190));//GridView.LayoutParams.FILL_PARENT
    		
    	}
        return convertView;   
    }  
    private final class AsyncImageTask extends AsyncTask<String, Integer, Bitmap> {

		private ContactService service;
		private ImageView iv_header;

		public AsyncImageTask(ContactService service, ImageView iv_header) {
			this.service = service;
			this.iv_header = iv_header;
		}

		// ��̨���е����߳����߳�
		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				//return 
						Uri uri = service.getImageURI(params[0], cache);
				String imageFile =  uri.getPath();
			    return BitmapFact.getBitmapOpts(imageFile, 164*164);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		// ���������ui�߳���ִ��
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap); 
			// ���ͼƬ�İ�
			if (iv_header != null && bitmap != null) {
				iv_header.setImageBitmap(bitmap);
				//iv_header.setImageURI(result);
			}
		}
	}
    
}




/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.URIBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class BitmapFact {

	 public final static Bitmap returnBitMapFromURI(String url) {   
		   URL myFileUrl = null;   
		   Bitmap bitmap = null;   
		   
		   try {
		    myFileUrl = new URL(url);    
		    HttpURLConnection conn;
		  
		    conn = (HttpURLConnection) myFileUrl.openConnection();
//		    conn.setConnectTimeout(5000);
//		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);   
		    conn.connect(); 
		    InputStream is = conn.getInputStream();   
		    bitmap = BitmapFactory.decodeStream(is);  
		    
		   } catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   }  catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   }
		   return bitmap;  
	 }
	 
	 public final static Bitmap returnBitMapFromSD(String url){
	        String imagepath = Environment.getExternalStorageDirectory()+"/Futurestore/images/"+url;
	        Bitmap bit = BitmapFactory.decodeFile(imagepath);
	        return bit;	 
	 } 
	 
	 public static Bitmap getBitmapOpts(String imageFile,int pix){
		 BitmapFactory.Options opts =  new  BitmapFactory.Options();
		    opts.inJustDecodeBounds =  true ;
		    BitmapFactory.decodeFile(imageFile, opts);
		    opts.inSampleSize = computeSampleSize(opts, - 1 ,  pix );       
		    opts.inJustDecodeBounds =  false ;
		    Bitmap bitmap = BitmapFactory.decodeFile(imageFile, opts);
			return bitmap;
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
	
}

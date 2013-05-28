/**
*    This is DataCenter of the app
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sjtu.micx.URIBitmap.BitmapFact;

public class DataCenter {
	
	public static int LoginType = 1;
	
	public static String UserID=0+"";	
	public static String EmployeID=1+"";
	
	public static List userInfoList;
	public static File cache;
	/*******************************Ŀ¼list ***********************************
	 * Dirlist[0]  һ��Ŀ¼
	 *  Dirlist[1...N] ����Ŀ¼  
	 */
	public static List<?> Dirlist;
	public static List   ItemList;
	public static List<?> Dirlist_show;
	/*******************************Ŀ¼list****************************************/
	
	public static List Cosume_orders;
	
	public static boolean isScan = false;
	
	public static int Width = 0;
	public static int Height = 0;
	/*******************************grid view ��ƷͼƬǽ****************************/
	public static String[] titles;
	public static String[] description;
	public static String[] images;
	/******************************grid view ��ƷͼƬǽ*****************************/
	
	/*******************************��Ʒ��ϸ��Ϣ************************************/
	public static List<Map<String, Object>> Goodsdata;
	/******************************��Ʒ��ϸ��Ϣ*************************************/
	
	public static List<Map<String, Object>> Orders = new ArrayList() ;
	public static List OrderList = new ArrayList();

	public static List Records_Orders;
	
	public static Bitmap getImg(Resources res,int id){
		Bitmap bm=BitmapFactory.decodeResource(res, id);
		return bm;
	}

	public static void data_init(){
		titles		= new String[]   
		    { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"};   
		    //ͼƬ�ĵڶ�������    
		description	= new String[]   
		    { "1000", "2000", "3000", "23000", "5000", "12000", "999","99", "19999", "2500", "245999", "1", "1121", "56666"};    
		images		= new String[]{ 
				"http://pic4.nipic.com/20090824/2885015_153508005349_2.jpg",
				"http://hiphotos.baidu.com/136350177/pic/item/55c74803f4027dad08fa9383.jpg",
				"http://pic3.bbzhi.com/jingwubizhi/gaoqingqichebizhi160/jing_car_183869_2.jpg",
				"http://imgsrc.baidu.com/forum/pic/item/46e7f21be20b8c83af6e752b.jpg",
				"http://hiphotos.baidu.com/xrzkx/pic/item/39d5714208615d32530ffe61.jpg",
				"http://hiphotos.baidu.com/xrzkx/pic/item/74d2610c2987596897ca6b49.jpg",
				"http://hiphotos.baidu.com/xiaohuogetout/pic/item/15edebe4c11a48afb8382036.jpg",
				"http://hiphotos.baidu.com/agp%CF%D4%BF%A8/pic/item/14c94d2778ab337f8b82a146.jpg",
				"http://hiphotos.baidu.com/%C1%CB%CE%DE%CB%BC/pic/item/e319ccea710d6d7e2df534df.jpg",
				"http://hiphotos.baidu.com/pupupi2009/pic/item/21ed6a0b4ceb74a56a60fb9b.jpg",
				"http://hiphotos.baidu.com/136350177/pic/item/01c46a3925b9e6b23b87ce77.jpg",
				"http://hiphotos.baidu.com/%D6%D0%B9%FA%D3%B0%CF%EC%C1%A6%CD%F8/pic/item/b11ba7d78cd53f45abec9a3f.jpg",
				"http://hiphotos.baidu.com/xrzkx/pic/item/39d5714208615d32530ffe61.jpg",
				"http://192.168.1.120:8080/2.jpg"
		                    }; 
		
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				 
		        Map<String, Object> map = new HashMap<String, Object>();
		        map.put("title", "Ʒ��:");
		        map.put("info", "���ӵ�����");
		        list.add(map);
		 
		        map = new HashMap<String, Object>();
		        map.put("title", "�ο��۸�:");
		        map.put("info", "2500����");
		        list.add(map);
		 
		        map = new HashMap<String, Object>();
		        map.put("title", "���ͳߴ磺");
		        map.put("info", "4462mm*1998mm*1204mm");
		        list.add(map);
		        
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "��������:");
		        map.put("info", "8.0T W16");
		        list.add(map);
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "���ʱ��:");
		        map.put("info", "407km/h");
		        list.add(map);
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "������ʽ:");
		        map.put("info", "��������");
		        list.add(map);
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "����ʱ��:");
		        map.put("info", "2.5�루0-100km/h��");
		        list.add(map);
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "�ƶ���ʽ:");
		        map.put("info", "�մ�ͨ����ʽ");
		        list.add(map);
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "���:");
		        map.put("info", "2710mm");
		        list.add(map);
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "�����ݻ�:");
		        map.put("info", "100L");
		        list.add(map);
		        
		        
		        map = new HashMap<String, Object>();
		        map.put("title", "Ʒ�Ʒ�չ");
		        map.put("info", "\tBugatti Veyron�����ӵ����������й��г��ϱ���ʽ����Ϊ�����ӵ���������\n" +
		        		"\t��Ҫ���ɣ������ǳ����ڳ������г��ֵ��ǲ��ٶ����ޣ�������ԭ��ͨ�����������ӵ�������.\n" +
		        		"\t���ӵϣ��������Ϊ����һ��������ܳ���Ʒ�ƣ�Ҳ��������Ϊֻ�������������ܳ����ȳ��ԵĹ��Ҳ���������ֿ񱩻�����");
		        list.add(map);
		        Goodsdata = list;
		        int i,j,k;
		        List orderlist = new ArrayList();
		        for(i=0;i<10;i++){
		        	HashMap<String, Object> map1 = new HashMap<String, Object>();
		        	String imageFile = "/sdcard/Futurestore//images/"+images[i].substring(images[i].lastIndexOf("/"));
		        	//map1.put("img", imageFile);
		        	map1.put("img", BitmapFact.getBitmapOpts(imageFile, 100*100));
		        	map1.put("title", titles[i]);
		        	map1.put("price", description[i]);
		        	map1.put("num", "1");
		        	orderlist.add(map1);
		        }
		        Orders = orderlist;       
	}
 
}

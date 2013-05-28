package com.sjtu.micx.config;

import java.util.List;

import com.sjtu.micx.data.DataCenter;

public class ItemManager {
	
	public static String getImageByID_web(int ID){
		
		List tmplist = (List) DataCenter.ItemList.get(ID);
		return "http://"+Config.Server_imgIP+"/"+tmplist.get(0)+".jpg";
		
		
	}
	public static String getImageByID_local(int ID){
		
		List tmplist = (List) DataCenter.ItemList.get(ID);
		return tmplist.get(0)+".jpg";	
	}
	public static String getInfoByID_num(int ID,int num){
		
		List tmplist = (List) DataCenter.ItemList.get(ID);
		return (String) tmplist.get(num);	
	}
	public static String getvideoByID(int ID){
		
		List tmplist = (List) DataCenter.ItemList.get(ID);
		return (String) tmplist.get(10);	
	}

}

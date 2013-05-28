package com.sjtu.micx.config;

import com.sjtu.micx.data.DataCenter;

public class UserInfoManager {
	public static String get_user_id(){
		
		return (String) DataCenter.userInfoList.get(0);
		
	}
	public static String get_user_chinaid(){
		
		return (String) DataCenter.userInfoList.get(2);
		
	}
	public static String get_user_name(){
	
		try{
			return (String) DataCenter.userInfoList.get(3);
		}catch(Exception e){
			e.printStackTrace();
		}return "无数据";
	
	}
	public static String get_user_gender(){
	
		String str = (String) DataCenter.userInfoList.get(4);
		if(str.contains("1"))
			return "男";
			return "女";
	}
	public static String get_user_type(){
	
		return (String) DataCenter.userInfoList.get(5);
	
	}
	public static String get_user_consume_sum(){
//		return "1234123412341234";
		return (String) DataCenter.userInfoList.get(14);
	
	}	
	public static String get_user_demand(){
//		return "1234123412341234";
		return (String) DataCenter.userInfoList.get(16);
	
	}
	public static String get_user_impress(){
//		return "1234123412341234";
		return (String) DataCenter.userInfoList.get(17);
	
	}
	public static String get_user_friends(){
//		return "1234123412341234";
		return (String) DataCenter.userInfoList.get(18);
	
	}
	public static String get_user_habit(){
//		return "1234123412341234";
		return (String) DataCenter.userInfoList.get(19);
	
	}
	public static String get_user_others(){
//		return "1234123412341234";
		return (String) DataCenter.userInfoList.get(20);
	
	}
	
}

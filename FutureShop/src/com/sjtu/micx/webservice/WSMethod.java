package com.sjtu.micx.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import com.sjtu.micx.config.Config;

public class WSMethod {
	
	
		private static String NAMESPACE ="http://tempuri.org/";  
		// WebServiceµÿ÷∑ 
		private static String URL = Config.URL;
		//private static String URL ="http://"+Config.Server_IP+":8000/WebService/Service1.asmx";   
		//private static String URL ="http://192.168.1.120:8000/Service1.asmx";
		
//		private static String URL ="http://"+Config.Server_IP;
		//private static String URL ="http://192.168.1.120:8000/WebService/Service1.asmx";
		
		
		
		private static String METHOD_NAME ="getWeatherbyCityName";  
		private static String SOAP_ACTION ="http://WebXml.com.cn/getWeatherbyCityName";  
		//WebServiceµÿ÷∑  
		private String weatherToday="start";  
		private static SoapObject detail;
		private static String Msg;
		
		/*
		 * List[][]
		 * list	0		prod_type_big_id,prod_type_big
		 * list	1		prod_type_big_id,prod_type_big
		 * ...
		 */
		public static List getFirstDir() {  
			List list=new ArrayList();
			METHOD_NAME ="getBigType";  
			SOAP_ACTION ="http://tempuri.org/getBigType";  
			try {  
				System.out.println("rpc------");  
				SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
				System.out.println("rpc"+ rpc);  

				AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
				ht.debug =true;  
				SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = rpc;  		
				envelope.dotNet =true;  
				envelope.setOutputSoapObject(rpc);  
				ht.call(SOAP_ACTION, envelope);  
				SoapObject result = (SoapObject) envelope.bodyIn;		
				detail = (SoapObject) result.getProperty("getBigTypeResult");  
				Msg = parseData(detail);

				
				int i;
				String[] strlen=Msg.split(";");
				for(i=0;i<strlen.length;i++ ){
					List templist=new ArrayList();
					String IDStr=strlen[i].split("`")[0];
					String ItemStr=strlen[i].split("`")[1];
					templist.add(IDStr);
					templist.add(ItemStr);
					list.add(templist);
				}
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
			return list;
		}  
	

		/*
		 * List[][]
		 * list	0		prod_type_small_id,prod_type_big_id,prod_type_small
		 * list	1		prod_type_small_id,prod_type_big_id,prod_type_small
		 * ...
		 */
		public static List getSecondDir(String ID) {  
			List list=new ArrayList();
			METHOD_NAME ="getSmallType";  
			SOAP_ACTION ="http://tempuri.org/getSmallType"; 
			try {  
				System.out.println("rpc------");  
				SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
				System.out.println("rpc"+ rpc);  
				rpc.addProperty("BigId", ID); 
				AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
				ht.debug =true;  
				SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = rpc;  
				envelope.dotNet =true;  
				envelope.setOutputSoapObject(rpc);  
				ht.call(SOAP_ACTION, envelope);  
				SoapObject result = (SoapObject) envelope.bodyIn;
				detail = (SoapObject) result.getProperty("getSmallTypeResult");  
				Msg = parseData(detail);
				
				int i;
				
				String[] strlen=Msg.split(";");
				for(i=0;i<strlen.length;i++ ){
					List templist=new ArrayList();
					String ID_2Str=strlen[i].split("`")[0];
					String ID_1Str=strlen[i].split("`")[1];
					String ItemStr=strlen[i].split("`")[2];
					templist.add(ID_2Str);
					templist.add(ID_1Str);
					templist.add(ItemStr);
					list.add(templist);
				}
				
			} catch (Exception e) {  
				e.printStackTrace();  
			}
			 return list;
		}  
		
		/* List[]
		 * 0	prod_id
		 * 1	prod_type_small_id
		 * 2	prod_type_big_id
		 * 3	prod_name
		 * 4	prod_num
		 * 5	prod_sell_price
		 * 6	prod_least_price
		 * 7	prod_desc
		 * 8	prod_check
		 * 9	prod_spec
		 * 10	prod_zone
		 * 11	prod_audio_name
		 * 12	prod_audio_path
		 * 13	prod_video_name
		 * 14	prod_video_path
		 * 15	prod_video_big_name
		 * 16	prod_video_big_path
		 * 17	prod_cert
		 * 18	prod_other
		 */
		public static List getItemByID(String ID) {  
			List list=new ArrayList();
			METHOD_NAME ="getProductByStyle";  
			SOAP_ACTION ="http://tempuri.org/getProductByStyle"; 
			try {  
				System.out.println("rpc------");  
				SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
				System.out.println("rpc"+ rpc);  
				rpc.addProperty("style", ID); 
				AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
				ht.debug =true;  
				SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = rpc;  
				envelope.dotNet =true;  
				envelope.setOutputSoapObject(rpc);  
				ht.call(SOAP_ACTION, envelope);  
				SoapObject result = (SoapObject) envelope.bodyIn;
				detail = (SoapObject) result.getProperty("getProductByStyleResult");  
				Msg = parseData(detail);
				
				int i,j;
				
				String[] strlen=Msg.split(";");
				for(i=0;i<strlen.length;i++ ){
					String[] substr=strlen[i].split("`");
					if(substr.length<2)break;
					List templist=new ArrayList();
					for(j=0;j<substr.length;j++){
						templist.add(substr[j]);
					}
					list.add(templist);
				}
				return list;
				
			} catch (Exception e) {  
				e.printStackTrace();  
			}
			 return list;
		}  
			/* List [][]
			 * 0	prod_id
			 * 1	prod_type_small_id
			 * 2	prod_type_big_id
			 * 3	prod_name
			 * 4	prod_num
			 * 5	prod_sell_price
			 * 6	prod_least_price
			 * 7	prod_desc
			 * 8	prod_check
			 * 9	prod_zone
			 * 10	prod_audio_name
			 * 11	prod_audio_path
			 */
	public static List getProductByname(String name) {  
		List list=new ArrayList();
		METHOD_NAME ="getProductByName";  
		SOAP_ACTION ="http://tempuri.org/getProductByName"; 
		try {  
			System.out.println("rpc------");  
			SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
			System.out.println("rpc"+ rpc);  
			rpc.addProperty("name", name); 
			AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
			ht.debug =true;  
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			envelope.dotNet =true;  
			envelope.setOutputSoapObject(rpc);  
			ht.call(SOAP_ACTION, envelope);  
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = (SoapObject) result.getProperty("getProductByNameResult");  
			Msg = parseData(detail);
			
			int i,j;
			
			String[] strlen=Msg.split(";");
			for(i=0;i<strlen.length;i++ ){
				String[] substr=strlen[i].split("`");
				if(substr.length<2)break;
				List templist=new ArrayList();
				for(j=0;j<substr.length;j++){
					templist.add(substr[j]);
				}
				list.add(templist);
			}
			return list;
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		 return list;
	} 
	
	/*
	 * 0	prod_id
	 * 1	prod_type_small_id
	 * 2	prod_type_big_id
	 * 3	prod_name
	 * 4	prod_num
	 * 5	prod_sell_price
	 * 6	prod_least_price
	 * 7	prod_desc
	 * 8	prod_check
	 * 9	prod_spec
	 * 10	prod_zone
	 * 11	prod_audio_name
	 * 12	prod_audio_path
	 * 13	prod_video_name
	 * 14	prod_video_path
	 * 15	prod_video_big_name
	 * 16	prod_video_big_path
	 * 17	prod_cert
	 * 18	prod_other
	 */
	static String PrdtKey[]  = {
			"prod_id",
			"prod_type_small_id",
			"prod_type_big_id",
			"prod_name",
			"prod_num",
			"prod_sell_price",
			"prod_least_price",
			"prod_desc",
			"prod_check",
			"prod_spec",
			"prod_zone",
			"prod_audio_name",
			"prod_audio_path",
			"prod_video_name",
			"prod_video_path",
			"prod_video_big_name",
			"prod_video_big_path",
			"prod_cert",
			"prod_other"	
	};
	public static List getProductById(String name) {  
		List list=new ArrayList();
		METHOD_NAME ="getProductById";  
		SOAP_ACTION ="http://tempuri.org/getProductById"; 
		try {  
			System.out.println("rpc------");  
			SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
			System.out.println("rpc"+ rpc);  
			rpc.addProperty("id", name); 
			AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
			ht.debug =true;  
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			envelope.dotNet =true;  
			envelope.setOutputSoapObject(rpc);  
			ht.call(SOAP_ACTION, envelope);  
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = (SoapObject) result.getProperty("getProductByIdResult");  
//			Msg = parseData(detail);
			Map map = parseData_map(detail);
			
			
			int i,j;
			
			List templist=new ArrayList();
			if(map.size()<2)return null;
			for(j=0;j<19;j++){
				String tmpstr = (String) map.get(PrdtKey[j]);
				if(map.get(PrdtKey[j])!=null)
					templist.add(tmpstr);
				else
					templist.add(" ");	
			}
					
//			String[] substr=Msg.split("`");
//			List templist=new ArrayList();
//			if(substr.length<2)return null;
//			for(j=0;j<substr.length;j++){
//				templist.add(substr[j]);	
//			}
		return templist;
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		 return list;
	} 
	
	/* List[]
	 *  0	user_id
	 *  1	emp_id
	 *  2	user_china_id
	 *  3	user_name
	 *  4	user_gender
	 *  5	user_type
	 *  6	user_company
	 *  7	user_job
	 *  8	user_phone
	 *  9	user_mobile
	 *  10	user_email
	 *  11	user_comp_addr
	 *  12	user_home_addr
	 *  13	user_path_come
	 *  14	user_consume_sum
	 *  15	user_rc_id
	 *  16	user_cust_demand
	 *  17	user_cust_impress
	 *  18	user_cust_friends
	 *  19	user_cust_habit
	 *  20	user_other
	 */
	public static List getUserById(String id) {  
		List list=new ArrayList();
		METHOD_NAME ="getUserByUserId";  
		SOAP_ACTION ="http://tempuri.org/getUserByUserId"; 
		try {  
			System.out.println("rpc------");  
			SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
			System.out.println("rpc"+ rpc);  
			rpc.addProperty("userId", id); 
			AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
			ht.debug =true;  
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			envelope.dotNet =true;  
			envelope.setOutputSoapObject(rpc);  
			ht.call(SOAP_ACTION, envelope);  
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = (SoapObject) result.getProperty("getUserByUserIdResult");  
			Msg = parseData(detail);
			
			int i,j;
				String[] substr=Msg.split("`");
				List templist=new ArrayList();
				if(substr.length<2)return null;
				for(j=0;j<substr.length;j++){
					templist.add(substr[j]);	
				}
			return templist;
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		 return list;
	} 
	
	/*
	 * List[][]
	 * 	  list  1	 	prodid,price, num
	 * 	  list  2		prodid,price, num
	 *    ...
	 */
	public static List getUserProduct(String id) {  
		List list=new ArrayList();
		METHOD_NAME ="getUserProduct";  
		SOAP_ACTION ="http://tempuri.org/getUserProduct"; 
		try {  
			System.out.println("rpc------");  
			SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
			System.out.println("rpc"+ rpc);  
			rpc.addProperty("userId", id); 
			AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
			ht.debug =true;  
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			envelope.dotNet =true;  
			envelope.setOutputSoapObject(rpc);  
			ht.call(SOAP_ACTION, envelope);  
			Object result = envelope.getResponse();
			
			Msg =result.toString();
			String str = "";
			String[] strlen=Msg.split(";");
			for(int i=0;i<strlen.length;i++ ){
				String[] substr=strlen[i].split(",");
				List templist=new ArrayList();
				if(substr.length<2)return null;
				for(int j=0;j<substr.length;j++){
					templist.add(substr[j].split(":")[1]);
//					str+=substr[j].split(":")[1]+"#";
				}
				list.add(templist);
			}
//			txt.setText(str);
			return list;
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		 return list;
	} 
	  //use_id;emp_id;order_date;order_price;number;prod_id;price;quantity;prod_id;price;quantity;
	public static boolean updateOrder(String name) {  
		
		List list=new ArrayList();
		METHOD_NAME ="updateOrder";  
		SOAP_ACTION ="http://tempuri.org/updateOrder"; 
		try {  
			System.out.println("rpc------");  
			SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
			System.out.println("rpc"+ rpc);  
			rpc.addProperty("order", name); 
			AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
			ht.debug =true;  
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			envelope.dotNet =true;  
			envelope.setOutputSoapObject(rpc);  
			ht.call(SOAP_ACTION, envelope);  
			//SoapObject result = (SoapObject) envelope.bodyIn;
			Object result = envelope.getResponse();
			
			Msg =result.toString();
		if(Msg.contains("true"))return true;
		return false;
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		 return false;
	}
	public static List getEmpById(String id) {  
		List list=new ArrayList();
		METHOD_NAME ="getEmpById";  
		SOAP_ACTION ="http://tempuri.org/getEmpById"; 
		try {  
			System.out.println("rpc------");  
			SoapObject rpc =new SoapObject(NAMESPACE, METHOD_NAME);  
			System.out.println("rpc"+ rpc);  
			rpc.addProperty("empId", id); 
			AndroidHttpTransport ht =new AndroidHttpTransport(URL);  
			ht.debug =true;  
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			envelope.dotNet =true;  
			envelope.setOutputSoapObject(rpc);  
			ht.call(SOAP_ACTION, envelope);  
			SoapObject result = (SoapObject) envelope.bodyIn;
			detail = (SoapObject) result.getProperty("getEmpByIdResult");  
			Msg = parseData(detail);
			
			int i,j;
			String[] substr=Msg.replace(";","").split("`");
			List templist=new ArrayList();
			if(substr.length<2)return null;
			for(j=0;j<substr.length;j++){
				templist.add(substr[j]);	
			}
			return templist;
			
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		 return list;
	} 
	
	private static String parseData(SoapObject detail) {
				Map map = new HashMap();
				detail=(SoapObject) detail.getProperty(1);
				Pattern pattern=Pattern.compile("ds=anyType\\{[^}]*\\};");
				Matcher matcher=pattern.matcher(detail.toString().replace("anyType{}", "-"));
				String item="";
				while(matcher.find()){
					System.out.println(matcher.group());
					String str=matcher.group().replace("ds=anyType{", "").replace(" ", "").replace("};", "").replace("}", "");
					String[] strlen=str.split(";");
					int i=0;
					for(i=0;i<strlen.length-1;i++ ){
						
						String key = str.split(";")[i].split("=")[0];
						String tempStr=str.split(";")[i].split("=")[1];
						map.put(key, tempStr);
						item+=tempStr+"`";
					}
					String key = str.split(";")[i].split("=")[0];
					String tempStr=str.split(";")[i].split("=")[1];
						item+=tempStr+";";
						map.put(key, tempStr);
					
				}
				return item;
			}
	private static Map parseData_map(SoapObject detail) {
		Map map = new HashMap();
		detail=(SoapObject) detail.getProperty(1);
		Pattern pattern=Pattern.compile("ds=anyType\\{[^}]*\\};");
		Matcher matcher=pattern.matcher(detail.toString().replace("anyType{}", "-"));
		String item="";
		while(matcher.find()){
			System.out.println(matcher.group());
			String str=matcher.group().replace("ds=anyType{", "").replace(" ", "").replace("};", "").replace("}", "");
			String[] strlen=str.split(";");
			int i=0;
			for(i=0;i<strlen.length-1;i++ ){
				
				String key = str.split(";")[i].split("=")[0];
				String tempStr=str.split(";")[i].split("=")[1];
				map.put(key, tempStr);
				item+=tempStr+"`";
			}
			String key = str.split(";")[i].split("=")[0];
			String tempStr=str.split(";")[i].split("=")[1];
				item+=tempStr+";";
				map.put(key, tempStr);
			
		}
		return map;
	}
	

}

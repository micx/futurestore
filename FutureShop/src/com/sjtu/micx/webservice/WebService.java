package com.sjtu.micx.webservice;

import java.util.ArrayList;
import java.util.List;

import com.sjtu.micx.data.DataCenter;

public class WebService {

	public static void DIR_init() {  
		   String str = "";
		  int i,j,k;		  		  
		  List list = new ArrayList(),list_show= new ArrayList(),list_1 = new ArrayList<String>(),list_1_1 = new ArrayList<String>();
		  List list_2= new ArrayList();
		  list_1 = WSMethod.getFirstDir();  
		  list.add(list_1);
 
		  for(i=0;i<list_1.size();i++){
			  List tmplist = (List) list_1.get(i);
			  str+=tmplist.get(0)+"-"+tmplist.get(1)+"\n"; 
			  list_1_1.add(tmplist.get(1)+"");
		 }
		  list_show.add(list_1_1);		 
		  for(i=0;i<list_1.size();i++){
			  List list_2_1= new ArrayList<String>();
			  List tmplist = (List) list_1.get(i);			  
			  str+=tmplist.get(0)+"-"+tmplist.get(1)+"\n"; 			  
			  list_2 = WSMethod.getSecondDir(tmplist.get(0)+"");
			  list.add(list_2);
			  for(j=0;j<list_2.size();j++){		  
				  tmplist = (List) list_2.get(j);
				  str+="\t"+tmplist.get(0)+"-"+tmplist.get(1)+"-"+tmplist.get(2)+";";
				  list_2_1.add(tmplist.get(2)+"");
			  }			 
			  list_show.add(list_2_1); 
			  str+="\n";
		  }
		  DataCenter.Dirlist = list;
		  DataCenter.Dirlist_show = list_show;
		  System.out.println(str);
		}  
	public static List get_cosume_orders(String id) {
		List retlist = new ArrayList();
		
		int i,j,k;
		List list = WSMethod.getUserProduct(id);
		if(list!=null)
		for(i=0;i<list.size();i++){
			List tmplist = (List) list.get(i);
			List list_1 = new ArrayList();
			List list_2 = WSMethod.getProductById((String) tmplist.get(0));
			if(list_2 == null) continue;
			for(j=0;j<list_2.size();j++)
				list_1.add(list_2.get(j));
			
				list_1.add(tmplist.get(1));
				list_1.add(tmplist.get(2));
			retlist.add(list_1);	
		}
		return retlist;  
		
	}
}

/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.XML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DomRWxml {
	
	public List readXMLFile() throws Exception {
		
		List list=new ArrayList();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		File f=new File(android.os.Environment.getExternalStorageDirectory()+"/Futurestore/DIR/student.xml"); 
		String path=f.getAbsolutePath();
	    File myfile=new File(path);
		Document doc = builder.parse(myfile); // 获取到xml文件
		  // 下面开始读取
		Element root = doc.getDocumentElement(); // 获取根元素
		NodeList first_dir = root.getElementsByTagName("First");
		
		for (int i = 0; i < first_dir.getLength(); i++) {
		
		   Element secd_root = (Element) first_dir.item(i);
		   NodeList names = secd_root.getElementsByTagName("item");
		   List tmplist=new ArrayList();
		   for(int j=0;j<names.getLength();j++){
			   Element e = (Element) names.item(j);
			   Node t = e.getFirstChild();
			  // System.out.println(t.getNodeValue());
			   tmplist.add(t.getNodeValue());
			   }
		   list.add(tmplist);
//		   for(int j=0;j<tmplist.size();j++)
//		   {
//			   System.out.println(tmplist.get(j));
//		   }
		}
		NodeList second_dir = root.getElementsByTagName("Second");
		for (int i = 0; i < second_dir.getLength(); i++) {
	
		   Element secd_root = (Element) second_dir.item(i);
		   NodeList names = secd_root.getElementsByTagName("item");
		   List tmplist=new ArrayList();
		   for(int j=0;j<names.getLength();j++){
			   Element e = (Element) names.item(j);
			   Node t = e.getFirstChild();
			  // System.out.println(t.getNodeValue());
			   tmplist.add(t.getNodeValue());
			   }
		   list.add(tmplist);
		}
//		for(int i=0;i<list.size();i++){
//			List str = (List) list.get(i);
//			for(int j=0;j<str.size();j++)
//			{
//				System.out.println(str.get(j));
//			}
//		}
		return list;
	 }
	 public static void callWriteXmlFile(Document doc, Writer w, String encoding) {
		  try {
		   Source source = new DOMSource(doc);
		   Result result = new StreamResult(w);
		   Transformer xformer = TransformerFactory.newInstance()
		     .newTransformer();
		   //xformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		   xformer.transform(source, result);
		  } catch (TransformerConfigurationException e) {
		   e.printStackTrace();
		  } catch (TransformerException e) {
		   e.printStackTrace();
		  }
	 }
	 public void writeXMLFile(List list) {
		  String outfile = "/sdcard/Futurestore/DIR/stucopy.xml";
		 
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder builder = null;
		  try {
			  builder = dbf.newDocumentBuilder();
		  } catch (Exception e) {
		  }
		  Document doc = builder.newDocument();
		  Element root = doc.createElement("DIR");
		  doc.appendChild(root); // 将根元素添加到文档上
		  // 获取学生信息
		  for(int i=0;i<list.size();i++){
			  Element secd;
			  if(0 == i){
				   secd = doc.createElement("First");
			  }
			  else{
				   secd = doc.createElement("Second");
			  }
			root.appendChild(secd);
			  List str =(List) list.get(i);
			  for(int j=0;j<str.size();j++){
				  Element name = doc.createElement("item");
				  secd.appendChild(name);
				   Text tname = doc.createTextNode((String) str.get(j));
				   name.appendChild(tname);
			  }
		  }

		  try {
//			  File f=new File(android.os.Environment.getExternalStorageDirectory()+"/Futureshop/student.xml"); 
//				String path=f.getAbsolutePath();
//			    File myfile=new File(path);
			  File file = new File(outfile);
			   FileOutputStream fos = new FileOutputStream(file);
			   OutputStreamWriter outwriter = new OutputStreamWriter(fos);
			   // ((XmlDocument)doc).write(outwriter); //出错！
			   callWriteXmlFile(doc, outwriter, "gb2312");
			   outwriter.close();
			   fos.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	 }
//	 public static void main(String args[]) {
//		  String str = "xml/student.xml";
//		  DomTest t = new DomTest();
//		   List v = null;
//		   try {
//			   v = t.readXMLFile();
//		  } catch (Exception e) {
//			  e.printStackTrace();
//		  }
//		  String outfile = "xml/stucopy.xml";
//		 
//		t.writeXMLFile(v);
//	 }
}


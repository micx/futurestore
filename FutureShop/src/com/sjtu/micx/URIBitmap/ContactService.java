/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.URIBitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.net.Uri;
import android.util.Xml;

public class ContactService {

	/**
	 * 从服务器上获取数据
	 */
	public List<Contact> getContactAll() throws Exception {
		List<Contact> contacts = null;
		String Parth = "http://192.168.1.103:8080/myweb/list.xml";
		URL url = new URL(Parth);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream is = conn.getInputStream();
			// 这里获取数据直接放在XmlPullParser里面解析
			contacts = xmlParser(is);
			return contacts;
		} else {
			return null;
		}
	}

	// 这里并没有下载图片下来，而是把图片的地址保存下来了
	private List<Contact> xmlParser(InputStream is) throws Exception {
		List<Contact> contacts = null;
		Contact contact = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("contacts")) {
					contacts = new ArrayList<Contact>();
				} else if (parser.getName().equals("contact")) {
					contact = new Contact();
					contact.setId(Integer.valueOf(parser.getAttributeValue(0)));
				} else if (parser.getName().equals("name")) {
					contact.setName(parser.nextText());
				} else if (parser.getName().equals("image")) {
					contact.setImage(parser.getAttributeValue(0));
				}
				break;

			case XmlPullParser.END_TAG:
				if (parser.getName().equals("contact")) {
					contacts.add(contact);
				}
				break;
			}
		}
		return contacts;
	}

	/*
	 * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
	 * 这里的path是图片的地址
	 */
	public Uri getImageURI(String path, File cache) throws Exception {
		String name =path.substring(path.lastIndexOf("/"));
		File file = new File(cache, name);
		// 如果图片存在本地缓存目录，则不去服务器下载 
		if (file.exists()) {
			return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
		} else {
				// 从网络上获取图片
			path = path.substring(0,path.lastIndexOf("."));
			path = path+name.substring(0, name.lastIndexOf("."))+"0.jpg";
			URL url = new URL(path);
			
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[11024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						is.close();
						fos.close();
						// 返回一个URI对象
						return Uri.fromFile(file);
					}
		}
			
			return null;
	}
}

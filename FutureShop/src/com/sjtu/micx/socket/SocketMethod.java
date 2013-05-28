package com.sjtu.micx.socket;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
public class SocketMethod {
	
	public static void sendMsg(String address,String msg) throws UnknownHostException, IOException{
	String SERVER_ADDR = address;
	int SERVER_PORT = 8500;
	Socket server ;
		server = new Socket(SERVER_ADDR, SERVER_PORT);
			OutputStream os = server.getOutputStream();
			String str = msg;
			os.write(str.getBytes());
		}
		
	}


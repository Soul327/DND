package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import Misc.ServerMisc;

public class Client extends Thread {
	Socket socket;
	InputStream in;
	OutputStream out;
	String ip = "127.0.0.1";
	
	public Client() {}
	public void connect() {
		System.out.println("Connecting to "+ip);
		try {
			Socket socket = new Socket(ip, Main.port);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
		} catch (IOException e) {
			System.err.println( "Failed to connect" );
		}
	}
	public void shutdown() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	public void run() {
		connect();
		while(true) {
			if(socket == null)
				connect();
			else
				if(!socket.isConnected())
					connect();
		}
	}
}

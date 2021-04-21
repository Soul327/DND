package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Misc.ServerMisc;

public class Client extends Thread {
	Socket socket;
	InputStream in;
	OutputStream out;
	String ip = "127.0.0.1";
	boolean cycle = false;
	
	public Client() {}
	public void connect() {
		System.out.println("Connecting to "+ip);
		try {
			socket = new Socket(ip, Main.port);
			in = socket.getInputStream();
			out = socket.getOutputStream();
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
		try {
			connect();
			while(socket.isConnected()) {
//				while(!cycle);
//				ServerMisc.sendString(out, "PING");
				System.out.println("CLIENT WAITING");
				String line = ServerMisc.getString(in);
				System.out.println( "CLIENT RECEIVED:"+line );
				if(line.startsWith("LOGIN ACCEPTED")) {
					Main.window.stateManager.state = 1;
					String per = line.substring(15);
					if(per.charAt(1) == '1') {
						
						Main.window.serverMenu.setVisible(true);
						Main.window.frame.pack();
						System.out.println("ADDED BAR");
					}
				}
			}
			/*
			while(socket.isConnected()) {
				System.out.println("CLIENT WAITING FOR LINE");
				byte[] store = in.readAllBytes();
				System.out.println(store);
				if( in.available() <= 0) continue;
				String line = ServerMisc.getString(in);
				if(line!=null)
					if(line.length() > 0) {
						System.out.println( line );
					}
			}
			//*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("CONNECTION LOST");
	}
	public void sendString(String str) {
		ServerMisc.sendString(out, str);
	}
}

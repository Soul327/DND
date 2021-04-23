package Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import FileUtil.FileUtil;
import Misc.ServerMisc;

public class Client extends Thread {
	Socket socket;
	InputStream in;
	OutputStream out;
	String ip = "127.0.0.1";
	boolean cycle = false;
	
	public Client() {
		File file = new File("client");
		if(file.exists())
			FileUtil.delete(file);
	}
	public void connect() {
//		System.out.println("Connecting to "+ip);
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
			do {
				connect();
//				ServerMisc.getString(in);
				while(socket.isConnected()) {
					//System.out.println("CLIENT WAITING");
					String line = ServerMisc.getString(in);
//					System.out.println( "CLIENT RECEIVED:"+line );
					if(line.startsWith("SENDING FILE")) {
						ServerMisc.getFile(in);
						ServerMisc.sendString(out, "FIN");
					}
					if(line.startsWith("LOGIN ACCEPTED")) {
						Main.window.stateManager.state = 1;
						String per = line.substring(15);
						if(per.charAt(1) == '1') {
							Main.window.masterMenu.setVisible(true);
							Main.window.frame.validate();
							Main.window.frame.repaint();
						}
						if(per.charAt(1) == '1') {
							Main.window.serverMenu.setVisible(true);
							Main.window.frame.validate();
							Main.window.frame.repaint();
						}
						sendString("UPDATEFILES");
					}
					
				}
			}while(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("CONNECTION LOST");
	}
	public void sendString(String str) {
		try {
			ServerMisc.sendString(out, str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Misc.ServerMisc;

public class Server extends Thread {
	
	public static ArrayList<User> users = new ArrayList<User>();
	public static ArrayList<Stick> sticks = new ArrayList<Stick>();
	
	public Server() {
		User usr = new User();
		usr.userName = "root";
		usr.password = "password";
		usr.per = "11";
		users.add(usr);
	}
	public void run() {
		ServerSocket ss;
		try {
			ss = new ServerSocket(3434);

			while (true) {
				Socket sock = ss.accept();
				System.out.println("Connected");
				new Handler(sock).start();
			}

//			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Handler extends Thread {
	Socket socket;
	User user = null;
	
	public Handler(Socket socket) { this.socket = socket; }
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			while(socket.isConnected()) {
				String outputString = "";
				
				System.out.println("SERVER WAITING");
				String line = ServerMisc.getString(in);
				System.out.println( "SERVER RECEIVED:"+line );
				if(line.startsWith("AUTHENTICATE")) {
					boolean sus = false;
					String userName = line.substring(13, line.indexOf(","));
					String password = line.substring(line.indexOf(",")+1, line.indexOf(")"));
					
					for(User u:Server.users)
						if(u.userName.equals(userName) && u.password.equals(password)) {
							user = u;
							outputString += "LOGIN ACCEPTED "+u.per;
							sus = true;
							break;
						}
					if(sus) System.out.println("USER LOG");
					else System.out.println("FAILED");
				}
				if(line.startsWith("UPDATEFILES")) {
					updateClientFiles(out);
				}
				if(line.startsWith("SENDING FILE")) {
					ServerMisc.sendString(out, "PONG");
					ServerMisc.getFile(in);
				}
				
				
				if(outputString.length() == 0) outputString = "PONG";
				ServerMisc.sendString(out, outputString);
			}
			
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client Disconnected");
	}
	public void updateClientFiles(OutputStream out) {
		File[] files = new File("server").listFiles();
		try {
			for(File f:files) {
				ServerMisc.sendString(out, "SENDING FILE");
				ServerMisc.sendFile(out, f, "client\\");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
/* Steps for connecting
 * Authenticate client
 */
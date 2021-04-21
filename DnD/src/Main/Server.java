package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Misc.ServerMisc;

public class Server extends Thread {
	public static ArrayList<User> users = new ArrayList<User>();
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
				
				if(outputString.length() == 0) outputString = "PONG";
				ServerMisc.sendString(out, outputString);
			}
			/*
			while(socket.isConnected()) {
				String line = ServerMisc.getString(in);
				if(line!=null)
					if(line.length() > 0) {
						System.out.println( line );
						if(line.startsWith("AUTHENTICATE")) {
							String userName = line.substring(13, line.indexOf(","));
							String password = line.substring(line.indexOf(",")+1, line.indexOf(")"));
							System.out.println("User login. USERNAME:"+userName + " PASSWORD:"+password);
							for(User u:Server.users)
								if(u.userName == userName && u.password == password) {
									user = u;
									System.out.println("User loged in as "+userName);
									ServerMisc.sendString(out, "CLIENTUSER");
									System.out.println("SENT LINE");
									break;
								}
						}
					}
			}
			//*/
			
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
/* Steps for connecting
 * Authenticate client
 */
package Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
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
	public Handler(Socket socket) { this.socket = socket; }
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			while(socket.isConnected()) {
				
			}
			
			in.close();
			out.close();
		} catch (IOException e) {}
	}
}
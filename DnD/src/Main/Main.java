package Main;

public class Main {
	static Server server;
	static Client client;
	static Window window;
	
	public static int port = 3434;

	public static void main(String args[]) {
		new Main();
	}

	public Main() {
		server = new Server();
		client = new Client();
		server.start(); client.start();

		window= new Window();
		window.start();
	}
}

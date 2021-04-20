package Main;

public class Main {
	public static int port = 3434;

	public static void main(String args[]) {
		new Main();
	}

	public Main() {
		Server server = new Server();
		Client client = new Client();
		//server.start(); client.start();

		Window window = new Window();
		window.start();
	}
}

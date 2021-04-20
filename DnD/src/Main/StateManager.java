package Main;

import Misc.Graphics;

public class StateManager {
	LoginState loginState = new LoginState();
	
	public void tick() {
		loginState.tick();
	}
	public void render(Graphics g) {
		loginState.render(g);
	}
}

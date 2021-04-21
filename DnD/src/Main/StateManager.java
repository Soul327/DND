package Main;

import Misc.Graphics;

public class StateManager {
	public int state = 0;
	LoginState loginState = new LoginState();
	
	public void tick() {
		switch(state) {
			case 0:loginState.tick();break;
		}
	}
	public void render(Graphics g) {
		switch(state) {
			case 0:loginState.render(g);break;
		}
	}
}

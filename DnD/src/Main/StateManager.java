package Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Rendering.Graphics;

public class StateManager {
	public int state = 0;
	LoginState loginState = new LoginState();
	BoardState boardState = new BoardState();
	
	public void tick() {
		switch(state) {
			case 0:loginState.tick();break;
			case 1:boardState.tick();break;
		}
	}
	public void render(Graphics g) {
		switch(state) {
			case 0:loginState.render(g);break;
			case 1:boardState.render(g);break;
		}
	}
}

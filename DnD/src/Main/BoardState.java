package Main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Misc.Graphics;

public class BoardState {
	BufferedImage img = null;
	File file = new File("client\\map.jpg");
	ArrayList<Stick> sticks = new ArrayList<Stick>();
	double zoom = 1;
	
	public BoardState() {
		Stick stick = new Stick();
		stick.x = 5;
		stick.y = 3;
		sticks.add(stick);
	}
	
	public void tick() {
		try {
			img = ImageIO.read( file );
		} catch (IOException e) {  }
	}
	public void render(Graphics g) {
		g.drawImage(img);//Background
		g.setColor(Color.red);
		for(int z=0;z<sticks.size();z++) {
			Stick s = sticks.get(z);
			g.fillCircle(s.x*s.size, s.y*s.size, s.size * zoom);
		}
	}
}

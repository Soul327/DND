package RoomViewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Misc.KeyManager;
import Rendering.Graphics;

public class StateManager {
	Tile[][] map = new Tile[3][8];
	int offSetX=0,offSetY=0;
	BufferedImage testImage = null;
	public StateManager() {
		for(int x=0;x<map.length;x++)
			for(int y=0;y<map[x].length;y++)
				map[x][y] = new Tile();
		
		map[0][0].setWalls(true, false, false, true);
		map[1][0].setWalls(true, false, false, false);
		map[2][0].setWalls(true, true, false, true);
		
		map[0][1].setWalls(false, false, true, true);
		map[1][1].setWalls(false, false, true, false);
		map[2][1].setWalls(false, true, true, true);
		
		map[0][2].setWalls(false, false, false, true);
		map[2][2].setWalls(false, true, false, false);
		
		map[0][3].setWalls(true, true, true, true);
		map[2][3].setWalls(true, true, true, true);
		
		map[0][4].setWalls(false, false, false, true);
		map[2][4].setWalls(false, true, false, false);
		
		map[0][5].setWalls(false, false, false, true);
		map[2][5].setWalls(true, true, true, true);
		
		map[0][6].setWalls(false, false, false, true);
		map[2][6].setWalls(false, true, false, false);
		
		Stick stick = new Stick();
		stick.id = 2;
		stick.rotation = 5;
		stick.x = .1;
		stick.y = .2;
		stick.size = .90;
		map[0][7].sticks.add(stick);
		map[0][7].setWalls(true, true, true, true);
//		map[1][7].setWalls(false, false, true, false);
		map[2][7].setWalls(true, true, true, true);
		
		try {
			testImage = ImageIO.read(new File("C:\\Users\\Soul327\\Downloads\\testImage.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void tick() {
		int speed = -1;
		if(KeyManager.getKey(KeyEvent.VK_A)) offSetX-= speed;
		if(KeyManager.getKey(KeyEvent.VK_D)) offSetX+= speed;
		if(KeyManager.getKey(KeyEvent.VK_W)) offSetY-= speed;
		if(KeyManager.getKey(KeyEvent.VK_S)) offSetY+= speed;
	}
	double r = 0;
	public void render(Graphics g) {
//		r+= .1; if(r > 360) r-=2; g.drawRotatedImage(testImage, 0, 0, 500, 500, r);
		
		int tileSize = 50;
		for(int x=0;x<map.length;x++)
			for(int y=0;y<map[x].length;y++) {
//				g.setColor(new Color(55,55,55,100));
//				g.drawRect(x*tileSize+offSetX, y*tileSize+offSetY, tileSize, tileSize);
//				g.setColor(new Color(255,255,255));
//				if(map[x][y].walls[0]) g.drawLine(x*tileSize+offSetX, y*tileSize+offSetY, x*tileSize+tileSize+offSetX, y*tileSize+offSetY);
//				if(map[x][y].walls[1]) g.drawLine(x*tileSize+tileSize+offSetX, y*tileSize+offSetY, x*tileSize+tileSize+offSetX, y*tileSize+tileSize+offSetY);
//				if(map[x][y].walls[2]) g.drawLine(x*tileSize+offSetX, y*tileSize+tileSize+offSetY, x*tileSize+tileSize+offSetX, y*tileSize+tileSize+offSetY);
//				if(map[x][y].walls[3]) g.drawLine(x*tileSize+offSetX, y*tileSize+offSetY, x*tileSize+offSetX, y*tileSize+tileSize+offSetY);
				g.drawImage(map[x][y].image,x*tileSize+offSetX,y*tileSize+offSetY,tileSize,tileSize);
				
			}
	}
}
class Stick {
	int id = 0;
	double size = 1, x = 0, y = 0, rotation = 0;
	public void render(Graphics g, int tileDrawSize) {
		tileDrawSize += 1;
		BufferedImage image = new BufferedImage(tileDrawSize, tileDrawSize, BufferedImage.TYPE_INT_ARGB);
		tileDrawSize -= 1;
		Graphics g2 = new Graphics( image.createGraphics() );
		
		try {
			switch(id) {
				case 1:g2.drawLine(0, 0, tileDrawSize, 0);break;
				case 2:
					g2.drawImage( ImageIO.read(new File("res/crate.jpg")),
							x*tileDrawSize,y*tileDrawSize,tileDrawSize,tileDrawSize );
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		g2.g.dispose();
		g.drawRotatedImage(image, 0, 0, tileDrawSize*size, tileDrawSize*size, rotation);
	}
}
class Tile{
	ArrayList<Stick> sticks = new ArrayList<Stick>();
	BufferedImage image = null;
	Tile() {
		updateImage();
	}
	void setWalls(boolean up, boolean right, boolean down, boolean left) {
		if(up) {
			Stick stick = new Stick();
			stick.id = 1;
			stick.rotation = 0;
			sticks.add( stick );
		}
		if(down) {
			Stick stick = new Stick();
			stick.id = 1;
			stick.rotation = 180;
			sticks.add( stick );
		}
		if(left) {
			Stick stick = new Stick();
			stick.id = 1;
			stick.rotation = 270;
			sticks.add( stick );
		}
		if(right) {
			Stick stick = new Stick();
			stick.id = 1;
			stick.rotation = 90;
			sticks.add( stick );
		}
//		walls[0] = up;
//		walls[1] = right;
//		walls[2] = down;
//		walls[3] = left;
		updateImage();
	}
	void updateImage() {
		int tileDrawSize = 100;
		image = new BufferedImage(tileDrawSize, tileDrawSize, BufferedImage.TYPE_INT_ARGB);
		tileDrawSize -= 1;
		Graphics g = new Graphics( image.createGraphics() );
//		g.drawLine(0, 0, tileDrawSize, 0);//Top
//		g.drawLine(tileDrawSize, 0, tileDrawSize, tileDrawSize);//Right
//		g.drawLine(0, tileDrawSize, tileDrawSize, tileDrawSize);//Down
//		g.drawLine(0, 0, 0, tileDrawSize);//Left
		for(Stick s:sticks) {
			s.render(g, tileDrawSize);
		}
		
		g.g.dispose();
	}
}
class Map{
	
}
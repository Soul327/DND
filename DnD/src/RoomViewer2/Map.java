package RoomViewer2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Rendering.Graphics;

public class Map {
	int maxX=10,maxY=10;
	BufferedImage image = null;
	ArrayList<ArrayList<Stick>> layer = new ArrayList<ArrayList<Stick>>();
	
	public Map() {
		//Layer 1
		layer.add(new ArrayList<Stick>());
		Stick stick;
		
		addWall( 0, 0, true, false, false, true);
		addWall( 1, 0, true, false, false, false);
		addWall( 2, 0, true, true, false, true);
		
		addWall( 0, 1, false, false, true, true);
		addWall( 1, 1, false, false, true, false);
		addWall( 2, 1, false, true, true, true);
		
		addWall( 0, 2, false, false, false, true);
		addWall( 2, 2, false, true, false, false);

		addWall( 0, 3, false, false, false, true);
		addWall( 2, 3, false, true, false, false);
		
		addWall( 0, 4, false, false, false, true);
		addWall( 2, 4, false, true, false, false);

		addWall( 0, 5, false, false, false, true);
		addWall( 2, 5, false, true, false, false);
		
		addWall( 0, 6, false, false, false, true);
		addWall( 2, 6, false, true, false, false);
		
		addWall( 0, 7, false, false, false, true);
		addWall( 2, 7, false, true, false, false);
		
		addWall( 0, 8, false, false, true, true);
		addWall( 2, 8, false, true, true, false);
	}
	
	public void addWall(int x, int y, boolean top, boolean right, boolean down, boolean left) {
		if(top) {
			Stick stick = new Stick();
			stick.id = 1; stick.gx = x; stick.gy = y;
			stick.updateRender();
			layer.get(0).add( stick );
		}
		if(right) {
			Stick stick = new Stick();
			stick.id = 1; stick.gx = x; stick.gy = y; stick.rotation = 90;
			stick.updateRender();
			layer.get(0).add( stick );
		}
		if(down) {
			Stick stick = new Stick();
			stick.id = 1; stick.gx = x; stick.gy = y; stick.rotation = 180;
			stick.updateRender();
			layer.get(0).add( stick );
		}
		if(left) {
			Stick stick = new Stick();
			stick.id = 1; stick.gx = x; stick.gy = y; stick.rotation = 270;
			stick.updateRender();
			layer.get(0).add( stick );
		}
	}
	
	public void updateRender() {
		int width = StateManager.tileSize*maxX;
		int height = StateManager.tileSize*maxY;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		width -= 1; height -= 1;
		Graphics g = new Graphics( image.createGraphics() );
		
		for(int l=0;l<layer.size();l++) {
			for(int b=0;b<layer.get(l).size();b++) {
				Stick s = layer.get(l).get(b);
				s.updateRender();
				g.drawRotatedImage(s.image, s.gx*StateManager.tileSize, s.gy*StateManager.tileSize, s.width*StateManager.tileSize, s.height*StateManager.tileSize, s.rotation);
			}
		}
		
		if(false) {//Draw grid
			g.setColor(Color.white);
			for(int x=1;x<maxX;x++)
				g.drawLine(0, x*StateManager.tileSize, height, x*StateManager.tileSize);
			for(int y=1;y<maxY;y++)
				g.drawLine(y*StateManager.tileSize, 0, y*StateManager.tileSize, width);
			g.drawRect(0, 0, width, height);
		}
		
		g.g.dispose();
		this.image = image;
	}
}
class Stick {
	int id = 0;
	double width = 1, height = 1, x = 0, y = 0, rotation = 0, gx=0,gy=0;
	BufferedImage image;
	
	Stick(){}
	Stick(Stick stick) {
		this.gx = stick.gx;
		this.gy = stick.gy;
		this.rotation = stick.rotation;
		this.id = stick.id;
//		System.out.println(gx+" "+gy+" "+rotation);
		updateRender();
	}
	
	public void updateRender() {
		switch(id) {
			case 1:
				image = new BufferedImage(StateManager.tileSize, StateManager.tileSize, BufferedImage.TYPE_INT_ARGB);
				Graphics g = new Graphics( image.createGraphics() );
				g.setColor(Color.white);
				g.fillRect(0, 0, StateManager.tileSize, StateManager.tileSize*.05);
				g.g.dispose();
				break;
		}
	}
}
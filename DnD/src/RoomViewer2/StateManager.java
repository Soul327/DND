package RoomViewer2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Misc.KeyManager;
import Misc.MouseManager;
import Rendering.Graphics;

public class StateManager {
	static int tileSize = 50;
	int offSetX=0,offSetY=0;
	Map map = new Map();
	public StateManager() {
		selecterTile = new Stick();
		selecterTile.id = 1;
	}
	public void tick() {
		int speed = -1;
		if(KeyManager.getKey(KeyEvent.VK_A)) offSetX-= speed;
		if(KeyManager.getKey(KeyEvent.VK_D)) offSetX+= speed;
		if(KeyManager.getKey(KeyEvent.VK_W)) offSetY-= speed;
		if(KeyManager.getKey(KeyEvent.VK_S)) offSetY+= speed;
		if(KeyManager.keyRelease(KeyEvent.VK_R)) selecterTile.rotation += 90;
		
	}
	boolean p = false;
	public void render(Graphics g) {
		g.drawImage( map.image, offSetX, offSetY );
		map.updateRender();
		renderTileSelecter( g );
	}
	
	Stick selecterTile;
	int selected = 1;
	public void renderTileSelecter(Graphics g) {
		//Draw selected list
		int h = 50, i=5;
		g.setColor( new Color(100,100,100) );
		g.fillRect(g.width/2-(h*i)/2, g.height - h, h*i, h);
		g.setColor( new Color(255,255,255) );
		for(int a=0;a<=i;a++) {
			if(selected == a)
				g.setColor( new Color(255,100,100) );
			else
				g.setColor( new Color(255,255,255) );
				
			g.drawRect(g.width/2-(h*i)/2, g.height - h, h*a, h);
		}
		//Draw selecterTile
		selecterTile.gx = (MouseManager.mouseX+offSetX)/tileSize;
		selecterTile.gy = MouseManager.mouseY/tileSize;
		g.drawRotatedImage( selecterTile.image, selecterTile.gx*tileSize, selecterTile.gy*tileSize, selecterTile.width*tileSize, selecterTile.height*tileSize, selecterTile.rotation );
		if(selecterTile.id != 0)
			if(MouseManager.leftPressed) {
				if(p==false) {
					p = true;
					Stick ts = new Stick(selecterTile);
					ts.updateRender();
					map.layer.get(0).add( ts );
//					System.out.println("CLICK "+map.layer.size()+" "+map.layer.get(0).size());
				}
			} else
				p = false;
		
		selecterTile.updateRender();
	}
}

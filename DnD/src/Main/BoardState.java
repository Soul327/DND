package Main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Misc.KeyManager;
import Misc.Mat;
import Misc.MouseManager;
import Rendering.Graphics;

public class BoardState {
	boolean showGrid = false;
	BufferedImage backgroundImage = null;
	Stick inHand = null;
	ArrayList<Stick> sticks = new ArrayList<Stick>();
	double globalZoom = 1, tileSize = 0, offX = 0, offY = 0;
	
	public BoardState() {
		sticks.add( new Stick( "STICK X:5;Y:5;SIZE:2;ID:0;" ) );
		sticks.add( new Stick( "STICK X:1;Y:1;SIZE:1;ID:1;" ) );
		
		reload();
	}
	
	public void addStick(String str) {
		Stick st = new Stick();
		if(str.startsWith("STICK "))
			str = str.substring( "STICK ".length() );
		String list[] = str.split(";");
		for(String s:list) {
			System.out.println(s);
			if(s.startsWith("X:")) st.x = Double.parseDouble( s.substring( "X:".length() ) );
			if(s.startsWith("Y:")) st.y = Double.parseDouble( s.substring( "Y:".length() ) );
			if(s.startsWith("SIZE:")) st.size = Double.parseDouble( s.substring( "SIZE:".length() ) );
			if(s.startsWith("ID:")) st.id = Integer.parseInt( s.substring( "ID:".length() ) );
		}
	}
	
	public void reload() {
		try {
			backgroundImage = ImageIO.read( new File("client\\map.jpg") );
			//Load mapconfig
			File mapConfig = new File("client\\map.jpg.config");
			if(mapConfig.exists()) {
				Scanner scanner = new Scanner(mapConfig);
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if(line.startsWith("TileSize "))
						tileSize = Double.parseDouble( line.substring("TileSize ".length()) );
				}
				scanner.close();
			}
		} catch (IOException e) {  }
	}
	public void tick() {
//		if(Math.random()*100<1) reload();
		if(KeyManager.keyRelease(KeyEvent.VK_1)) tileSize += 1;
		if(KeyManager.keyRelease(KeyEvent.VK_2)) tileSize -= 1;
		if(KeyManager.keyRelease(KeyEvent.VK_F5)) reload();
		
		if(KeyManager.getKey(KeyEvent.VK_W)) offY += 1;
		if(KeyManager.getKey(KeyEvent.VK_A)) offX += 1;
		if(KeyManager.getKey(KeyEvent.VK_S)) offY -= 1;
		if(KeyManager.getKey(KeyEvent.VK_D)) offX -= 1;
	}
	
	boolean mouseClicks[] = new boolean[5];
	public void checkClicks() {
		boolean[] listOfBools = {MouseManager.leftPressed,MouseManager.rightPressed,MouseManager.middlePressed};
		//Check if mouseClicks is correct size and exists
		if(mouseClicks == null)
			mouseClicks = new boolean[listOfBools.length*2];
		else if(mouseClicks.length < listOfBools.length*2)
			mouseClicks = new boolean[listOfBools.length*2];
		
		for(int z=0;z<listOfBools.length;z++) {
			if(mouseClicks[z*2] == true) mouseClicks[z*2] = false;
			if(listOfBools[z] & mouseClicks[z*2+1] == false) mouseClicks[z*2] =  mouseClicks[z*2+1] = true;
			if(!listOfBools[z]) mouseClicks[z*2+1] = false;
		}
	}
	public void render(Graphics g) {
		checkClicks();
		if(backgroundImage != null)
			g.drawImage(backgroundImage,offX,offY,backgroundImage.getWidth(),backgroundImage.getHeight());//Background
		if(backgroundImage!=null && showGrid) {
			g.setColor(Color.gray);
			for(int x=0;x*tileSize<backgroundImage.getWidth();x++)
				g.drawLine(offX, x*tileSize+offY, backgroundImage.getWidth()+offX, x*tileSize+offY);
			for(int x=0;x*tileSize<backgroundImage.getHeight();x++)
				g.drawLine(x*tileSize+offX, offY, x*tileSize+offX, backgroundImage.getHeight()+offY);
		}
		
		boolean iUsed = false;
		int mx = MouseManager.mouseX;
		int my = MouseManager.mouseY;
		for(int z=0;z<sticks.size();z++) {
			Stick s = sticks.get(z);
			double drawX = s.x*tileSize+(tileSize/2)*(int)s.size+offX;
			double drawY = s.y*tileSize+(tileSize/2)*(int)s.size+offY;
			double size = s.size*tileSize;
			
			if(inHand == null) {
				//Check if user can access this stick
				if(Main.client.usrA.charAt(0)=='1') {
					//Check if mouse is over stick
					g.setColor(Color.white);
					if( Mat.distance(drawX, drawY, mx, my) < size/2 & !iUsed ) {
						if(mouseClicks[0]) {
							g.fillCenterCircle(drawX, drawY, size);
							inHand = s;
							iUsed = true;
						}
						g.drawCenterCircle(drawX, drawY, size);
					}
				}
			//Else is required so that double mouse press is not reconized
			} else if(inHand == s) {
				boolean snaps = true;
				s = inHand;
				if(snaps) {//snaps to tile while following the mouse rather than sticking to the mouse
					inHand.x = (int)(mx/tileSize - offX/tileSize);
					inHand.y = (int)(my/tileSize - offY/tileSize);
				} else {
					inHand.x = (mx/tileSize + offX)-inHand.size/2;
					inHand.y = (my/tileSize + offY)-inHand.size/2;
				}
				if(mouseClicks[0] & !iUsed) {
					inHand = null;
					iUsed = true;
				}
			}
			
			g.setColor(Color.red);
			g.fillCenterCircle(drawX, drawY, size );
			
		}
		g.drawString("TILE SIZE:"+tileSize, 0, 15);
		g.drawString("offX:"+offX+" offY:"+offY, 0, 30);
	}
}

package Main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Misc.Graphics;
import Misc.KeyManager;

public class BoardState {
	BufferedImage imgMap = null;
	ArrayList<Stick> sticks = new ArrayList<Stick>();
	double globalZoom = 1;
	double tileSize = 0;
	
	public BoardState() {
		Stick stick = new Stick();
		stick.x = 1;
		stick.y = 1;
		stick.size = .5;
		sticks.add(stick);
		stick.getString();
		stick.setString( "STICK X:5;Y:5;SIZE:2;" );;
		reload();
	}
	
	public void reload() {
		try {
			imgMap = ImageIO.read( new File("client\\map.jpg") );
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
		if(Math.random()*100<1) reload();
		if(KeyManager.keyRelease(KeyEvent.VK_1)) tileSize += 1;
		if(KeyManager.keyRelease(KeyEvent.VK_2)) tileSize -= 1;
		if(KeyManager.keyRelease(KeyEvent.VK_F5)) reload();
	}
	public void render(Graphics g) {
		g.drawImage(imgMap);//Background
		if(imgMap!=null) {
			g.setColor(Color.gray);
			for(int x=0;x*tileSize<imgMap.getWidth();x++)
				g.drawLine(0, x*tileSize, imgMap.getWidth(), x*tileSize);
			for(int x=0;x*tileSize<imgMap.getHeight();x++)
				g.drawLine(x*tileSize, 0, x*tileSize, imgMap.getHeight());
		}
		
		
		g.setColor(Color.red);
		for(int z=0;z<sticks.size();z++) {
			Stick s = sticks.get(z);
			g.fillCenterCircle(
				s.x*tileSize+(tileSize/2)*(int)s.size,
				s.y*tileSize+(tileSize/2)*(int)s.size,
				s.size*tileSize );
		}
		g.drawString("TILE SIZE:"+tileSize, 0, 15);
	}
}

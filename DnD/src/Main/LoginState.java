package Main;

import java.awt.Color;

import Misc.Graphics;
import Misc.KeyManager;

public class LoginState {
	int selected = 0;
	String userName = "";
	String password = "";
	
	public void tick() {
		String str = KeyManager.checkType();
		if(str == "BACKSPACE") {
			if(userName.length() > 0)
				userName = userName.substring(0,userName.length()-1);
		} else if(str == "ENTER") {
			if(selected == 0) selected = 1;
			if(selected == 1) selected = 0;
		} else
			userName += str;
	}
	public void render(Graphics g) {
		g.setColor(Color.gray);
		if(selected == 0) 
			g.drawOutlinedString("USERNAME: "+userName+"_", 0, 15);
		else
			g.drawOutlinedString("USERNAME: "+userName, 0, 15);
		if(selected == 1)
			g.drawOutlinedString("PASSWORD: "+password+"_", 0, 30);
		else
			g.drawOutlinedString("PASSWORD: "+password, 0, 30);
			
	}
}

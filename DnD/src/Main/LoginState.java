package Main;

import java.awt.Color;

import Misc.Graphics;
import Misc.KeyManager;
import Misc.ServerMisc;

public class LoginState {
	int selected = 0;
	int maxSel = 2;
	String userName = "";
	String password = "";
	
	public void tick() {
		String str = KeyManager.checkType();
		if(str == "BACKSPACE") {
			if(selected == 0)
				if(userName.length() > 0)
					userName = userName.substring(0,userName.length()-1);
			if(selected == 1)
				if(password.length() > 0)
					password = password.substring(0,password.length()-1);
		} else if(str == "ENTER") {
			switch(selected) {
				case 2:
					//Submit
					String store = "AUTHENTICATE("+userName+","+password+");";
					Main.client.sendString(store);
					
//					System.exit(0);
					selected = 0;
					break;
				default: selected++;
			}
		}else if(str == " ") {
		} else
			switch(selected) {
				case 0: userName += str; break;
				case 1: password += str; break;
			}
			
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
		
		
		g.setColor(Color.gray);
		if(selected == 2) g.setColor(Color.LIGHT_GRAY);
		g.fillRect(5, 35, 45, 15);
		g.drawOutlinedString("Submit", 7, 48);
			
	}
}

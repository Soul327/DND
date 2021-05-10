package Main;

public class Stick {
	int id = 0;
	double x = 0, y = 0, size = 1;
	
	public Stick() {}
	
	
	public Stick(String str) {
		setString(str);
	}
	
	String getString() {
		String str = "STICK ";
		String[] list = {
			"X:"+x,
			"Y:"+y,
			"SIZE:"+size
		};
		for(String s:list) str += s + ";";
		System.out.println(str);
		return str;
	}
	
	void setString(String str) {
		if(str.startsWith("STICK "))
			str = str.substring( "STICK ".length() );
		String list[] = str.split(";");
		for(String s:list) {
			System.out.println(s);
			if(s.startsWith("X:")) x = Double.parseDouble( s.substring( "X:".length() ) );
			if(s.startsWith("Y:")) y = Double.parseDouble( s.substring( "Y:".length() ) );
			if(s.startsWith("SIZE:")) size = Double.parseDouble( s.substring( "SIZE:".length() ) );
		}
		System.out.println(str);
	}
}

package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {
            long start = System.currentTimeMillis();
            
            
            
            System.out.println(System.currentTimeMillis() - start);
		
		LwjglApplicationConfiguration cfg =
			new LwjglApplicationConfiguration();
		cfg.title = "Asteroids";
		cfg.width = 1200;
		cfg.height = 800;
		cfg.useGL30 = false;
		cfg.resizable = false;
		
		new LwjglApplication(new Game(), cfg);
		
	}
	
}

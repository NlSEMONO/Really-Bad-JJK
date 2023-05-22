import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Entity {
	private int x, y, life, atk, def, nrg, maxnrg;
	private String name;
	private BufferedImage img;
	
	public int getX(){ return x;	}
	public int getY(){ return y;}
	public int getHP(){ return life;}
	public int getAtk(){ return atk;}
	public int getNrg() {return nrg;}
	public int getDef() {return def;}
	public String getName() { return name; }
	public int getMaxNrg() {return maxnrg; }
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setMaxNrg(int n) {
		this.maxnrg = n;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setHP(int hp) {
		this.life = hp;
	}
	
	public void setA(int a) {
		this.atk = a;
	}
	
	public void setD(int d) {
		this.def = d;
	}
	
	public void setEn(int en) {
		this.nrg = en;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public BufferedImage img(String name) {
		InputStream imageclass = null;
		imageclass = JJKModel.class.getResourceAsStream(name);
		BufferedImage ret = null;
		if (imageclass==null) { 
			
		} else {
			try {
				return ImageIO.read(imageclass);
			} catch (IOException e) {
				
			}
		}
		System.out.println("File not Found" + name);
		try {
			return ImageIO.read(new File(name));
		} catch (IOException e1) {
		}
		
		return ret;
	}
	
	Entity(String n, int x, int y, int atk, int def, int nrg, int max, int hp) {
		this.name = n;
		this.x = x;
		this.y = y;
		this.atk = atk;
		this.def = def;
		this.nrg = nrg;
		this.maxnrg = max;
		this.life = hp;
		this.img = img(n+".png");
	}
}

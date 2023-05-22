import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JJKModel {
	private static int pix = 30;
	private static int mapX = 20;
	private static int mapY = 20;
	private int fingers = 0;
	private int kills = 0;
	private int speed = 1;
	private int opponent;
	
	private String[][] strMappp = new String[mapX][mapY];
	private BufferedImage[] icons = new BufferedImage[8];
	private BufferedImage imghud;
	
	private BufferedImage[] backs = new BufferedImage[2];
	private BufferedImage[] chars = new BufferedImage[3];
	private BufferedImage[] bat = new BufferedImage[1];
	private BufferedImage[] HUlt = new BufferedImage[2];
	private BufferedImage[] MUlt = new BufferedImage[2];
	private BufferedImage[] FUlt = new BufferedImage[1];
	
	private BufferedImage[] NAtk = new BufferedImage[1];
	private BufferedImage[] DF = new BufferedImage[2];
	private BufferedImage[] BF = new BufferedImage[8];
	private BufferedImage dim;
	
	private boolean[] tutProgress = new boolean[5];
	
	// 1 hero 3 mobs 2 fingers
	private Entity[] ens = new Entity[6];
	private Font font;
	
	public Font getFont() {
		return font;
	}
	
	public ArrayList<BufferedImage[]> loadBtlImgs(int mp, String name) {
		ArrayList<BufferedImage[]> ret = new ArrayList<>();
	
		ret.add(backs);
		ret.add(bat);
		ret.add(chars);
		if (name.equals("monsterNoob")) { 
			ret.add(FUlt);
		} else if (mp==0) {
			ret.add(MUlt);
		} else {
			ret.add(HUlt);
		}
		
		ret.add(NAtk);
		ret.add(DF);
		ret.add(BF);
		
		return ret;
	}
	
	public int getOpponent() {
		return opponent;
	}
	
	public void setOpponent(int x) {
		opponent = x;
	}
	
	public void toggleSpeed() {
		if (speed>1) {
			speed = 1;
		} else {
			speed = speed+fingers;
		}
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public String getTile(int x, int y) {
		return strMappp[y][x];
	} 
	
	public String[][] getMap() {
		return strMappp;
	}
	
	public BufferedImage getIcon(int idx) {
		return icons[idx];
	} 
	
	public int getPix() {
		return pix;
	}
	
	public Entity[] getEn() {
		return ens;
	}
	
	public int getFinger() {
		return fingers;
	}
	
	public int getKill() {
		return kills;
	}
	
	public BufferedImage getModel() {
		return imghud;
	}
	
	public void addKill() {
		kills++;
	}
	
	public void addFinger() {
		fingers++;
	}
	
	public void toTile(int x, int y) {
		strMappp[y][x] = "g";
	}
	
	public boolean[] getTutProg() {
		return tutProgress;
	}
	
	public BufferedImage getDim(){
		return dim;
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
	
	JJKModel(String map) {
		int i;
		try {
			System.out.println("XD");
			
			BufferedReader f = new BufferedReader(new InputStreamReader(JJKModel.class.getResourceAsStream(map+".csv")));
			for (i=0;i<mapY;i++) {
				strMappp[i] = f.readLine().split(",");
			}
			f.close();
			
			/* JAR FILE MODE */
			icons[0] = map.equals("school") ? img("tile.PNG") :  img("grass.jpg");
			icons[1] = img("water.jpg");
			icons[3] = img("building.png");
			icons[2] = map.equals("school") ? img("tree.jpg") : img("tree.png");
			icons[4] = img("yuji.png");
			icons[5] = img("monsterNoob.png");
			icons[6] = img("monsterBoss.png");
			icons[7] = img("finger.png");
			
			/* NO JAR 
			icons[0] = map.equals("school") ? ImageIO.read(new File("tile.png")) :  ImageIO.read(new File("grass.jpg"));
			icons[1] = ImageIO.read(new File("water.jpg"));
			icons[3] = ImageIO.read(new File("building.png"));
			icons[2] = map.equals("school") ? ImageIO.read(new File("tree.jpg")) : ImageIO.read(new File("tree.png"));
			icons[4] = ImageIO.read(new File("yuji.png"));
			icons[5] = ImageIO.read(new File("monsterNoob.png"));
			icons[6] = ImageIO.read(new File("monsterBoss.png"));
			icons[7] = ImageIO.read(new File("finger.png"));
			*/
			
			ens[0] = map.equals("school") ? new Entity("yuji", 9, 18, 200, 50, 50, 50, 50) : new Entity("yuji", 18, 18, 200, 50, 50, 50, 50);
			ens[1] = map.equals("school") ? new Entity("finger", 9, 13, -100, 50, 50, 50, 50) : new Entity("finger", 2, 2, -100, 50, 50, 50, 50);
			ens[2] = map.equals("school") ? new Entity("finger", 15, 0, -100, 50, 50, 50, 50) : new Entity("finger", 2, 17, -100, 50, 50, 50, 50);
			ens[3] = new Entity("monsterNoob", 4, 2, 200, 45, 0, 100, 200);
			ens[4] = map.equals("school") ? new Entity("monsterNoob", 15, 2, 200, 45, 0, 100, 200) : new Entity("monsterNoob", 2, 16, 200, 45, 0, 100, 200);
			ens[5] = map.equals("school") ? new Entity("monsterBoss", 10, 7, 750, 60, 0, 100, 425) :  new Entity("monsterBoss", 10, 16, 400, 70, 0, 100, 500);
			
			imghud = img("yujiChar.png");
			
			bat[0] = img("yujiBattle.png");
			chars[0] = img("mahito.png");
			chars[1] = img("hanami.png");
			chars[2] = img("fingerB.png");
			backs[0] = img("school.png");
			backs[1] = img("forest.png");
			FUlt[0] = img("fingerBeam.jpg");
			MUlt[0] = img("junpei.png");
			MUlt[1] = img("mahitoDom.png");
			HUlt[0] = img("bud.png");
			HUlt[1] = img("bud2.png");
			
			NAtk[0] = img("n1.png");
			for (i=0;i<3;i++) BF[i] = img("bf"+(i*2+1)+".png");
			BF[3] = img("bFlash1.png");
			DF[0] = img("dFist1.png");
			DF[1] = img("dFist2.png");
			
			if (map.equals("tutorial")) {
				ens[0] = new Entity("yuji", 9, 19, 250, 50, 50, 50, 350);
				ens[1] = new Entity("finger", 9, 18, -100, 50, 50, 50, 50);
				ens[2] = new Entity("finger", 9, 13, -100, 50, 50, 50, 50);
				ens[3] = new Entity("monsterNoob", 9, 14, 200, 45, 0, 100, 200);
				ens[4] = new Entity("monsterNoob", 9, 2, 200, 45, 0, 100, 200);
				ens[5] = new Entity("monsterBoss", 9, 1, 750, 60, 0, 100, 425);
			} else {
				Arrays.fill(tutProgress, true);
			}
			
			dim = img("blur.png");
			
			
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("Hack-Regular.ttf"));
	            font = font.deriveFont(Font.PLAIN, 20);
			} catch (FontFormatException e) {
				System.out.println("XD");
			} catch (IOException e) { 
				
			}
		} catch (IOException e) {
		}
	}
}

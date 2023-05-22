import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class BatPanel extends JPanel{
	int m;
	String name;
	JJKModel data;
	private BufferedImage enemy;
	private BufferedImage yuji;
	private BufferedImage back;
	private BufferedImage[] BF;
	private BufferedImage[] DF;
	private BufferedImage norm;
	private BufferedImage[] ult;
	private int mX, mY;
	Entity yj;
	Entity en;
	// 0 - normal; 1 - Divergent fist; 2 - Black Flash; 3 - enemy ultimate
	private boolean[] atks = new boolean[4];
	// 0 - divergent fist; 1 - black flash; 2 - enemy ult
	private int[] atkMode = new int[3];
	private int[] frame = new int[3];
	private boolean CC = false;
	private int[] ccTimer = new int[2];
	
	@Override
	public void paintComponent(Graphics g) {
		if (en.getName().equals("monsterBoss")&&atks[3]&&m==0) {
			// mahito
			frame[2]++;
			if (frame[2]==10)frame[2]=0;
			g.drawImage(ult[1], 0, 0, null);
			CC = true;
		} else g.drawImage(back, 0, 0, null);
		
		if (CC&&m==0) {
			g.drawImage(ult[0], 30, 96, null);
		} else if (CC) {
			g.drawImage(yuji, 30, 96, null);
			g.drawImage(ult[1], 200, 250, null);
		} else {
			g.drawImage(yuji, 30, 96, null);
		}
		
		g.drawImage(enemy, mX, mY, null);
		g.setColor(Color.BLACK);
		g.fillRect(0, 500, 1000, 600);
		g.setColor(Color.WHITE);
		g.setFont(data.getFont());
		g.drawString("Life: " + yj.getHP(), 50, 525);
		g.drawString("Cursed Energy: " + yj.getNrg() + "/"  + yj.getMaxNrg(), 50, 555);
		g.drawString("(25) Q - Divergent Fist", 350, 525);
		
		if (atks[0]) {
			g.drawImage(norm, 675, 200, null);
		}
		
		if (atks[1]) {
			if (atkMode[0]==0) {
				g.drawImage(DF[0], 675, 200, null);
			} else  {
				g.drawImage(DF[1], 300+30*frame[0], 25+5*frame[0], null);
				frame[0]++;
				if (frame[0]==10) frame[0] = 0;
			}
		}
		
		if (atks[2]) {
			if (atkMode[1]==0) {
				g.drawImage(BF[3], 300+30*frame[1], 25+5*frame[1], null);
				frame[1]++;
				if (frame[1]==10) frame[1] = 0;
			} 
			if (atkMode[1]>=1) {
				g.drawImage(BF[0], 600, 100, null);
			} 
			if (atkMode[1]>=2) {
				g.drawImage(BF[2], 800, 100, null);
			}
			if (atkMode[1]>=3) {
				g.drawImage(BF[1], 750, 50, null);
			}
		}
		
		if (atks[3]) {
			frame[2]++;
			if (en.getName().equals("monsterNoob")) { 
				for (int i=0;i<frame[2];i++) {
					g.drawImage(ult[0], 600-i*26, 250, null);
				}
				if (frame[2]==15) frame[2]=0; 
			} else {
				// hanami
				if (m==1) {
					g.drawImage(ult[0], 600-frame[2]*23, 250, null);
					if (frame[2]==12)frame[2]=0;
					CC = true;
				}
			}
		}
		
		String strBFStat = "E - ";
		strBFStat = data.getFinger() >= 2 ? "(40) " + strBFStat + "Black Flash" : strBFStat + "Not unlocked";
		g.drawString(strBFStat, 350, 555);
		g.drawString("(0) A - Normal Attack", 350, 585);
		g.drawString("Life: " + en.getHP(), 650, 525);
		g.drawString("Cursed Energy: " + en.getNrg() + "/" + en.getMaxNrg(), 650, 555);
	}
	
	public void setMode(int idx, int val) { 
		atkMode[idx] = val;
	}
	
	public int[] getCCTime() {
		return ccTimer;
	}
	
	public int getMode(int idx) {
		return atkMode[idx];
	}
	
	public void setAtk(int idx, boolean val) {
		atks[idx] = val;
	}
	
	public boolean[] getAtk() {
		return atks;
	}
	
	public boolean getCC() {
		return CC;
	}
	
	public void setCC(boolean in) { 
		CC = in;
	}
	
	public BatPanel(int mp, String name, JJKModel data) {
		super();
		this.setPreferredSize(new Dimension(1000, 600));
		this.m = mp;
		this.name = name;
		this.data = data;
		this.yj = data.getEn()[0];
		this.en = name.equals("monsterNoob") ? data.getEn()[data.getOpponent()] : data.getEn()[5];
		
		mX = name.equals("monsterNoob") ? 575 : 650;
		mY = 85;
		
		System.out.println(data.getOpponent());
		
		ArrayList<BufferedImage[]> imgs = data.loadBtlImgs(mp, name);
		
		back = imgs.get(0)[mp];
		yuji = imgs.get(1)[0];
		enemy = name.equals("monsterNoob") ? imgs.get(2)[2] : imgs.get(2)[mp];
		ult = imgs.get(3);
		norm = imgs.get(4)[0];
		DF = imgs.get(5);
		BF = imgs.get(6);
		
		ccTimer[1] = 2;
	}
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class JJKMain implements KeyListener, ActionListener {
	private int ticks = 0;
	private int lastTick = -1;
	private JJKUX v;
	private JJKModel data;
	private static boolean done = false;
	private static boolean lose = true;
	private String mname = "school";
	private static boolean btl = false;
	private static boolean inGame = false;
	private static boolean tut = false;
	
	private int frames = 50;
	private int mp;
	
	Timer time = new Timer(1000/frames, this);
	// same as batpanel indexes, but 4th index is energy for all parties
	private int[][] atks = new int[5][3];
	private BatPanel bp;
	private Entity[] btlers = new Entity[2];

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (!inGame) {
			tut = false;
			if (e.getKeyChar()=='s'||e.getKeyChar()=='S') {
				v.setData(new JJKModel("school"));
				v.drawMap();
				v.swapPan(0);
				inGame = true;
			} else if (e.getKeyChar()=='F'||e.getKeyChar()=='f') {
				v.setData(new JJKModel("forest"));
				v.drawMap();
				v.swapPan(0);
				inGame = true;
			} else if (e.getKeyChar()=='t'||e.getKeyChar()=='T') {
				v.setData(new JJKModel("tutorial"));
				v.drawMap();
				v.swapPan(0);
				inGame = true;
				tut = true;
			}
		} else {
			if (tut) {
				boolean[] prog = data.getTutProg();
				for (int i=0;i<5;i++) {
					if (!prog[i]) {
						prog[i] = true;
						v.drawMap();
						return;
					}
				}
			}
			if (!btl) {
				data = v.getData();
				if (lastTick<ticks&&!done) {
					Entity yuji = data.getEn()[0];
					Entity[] otherEn = new Entity[5];
					for (int i=0;i<5;i++) {
						otherEn[i] = data.getEn()[i+1];
					}
					
					boolean press = false;
					char curr = e.getKeyChar();
					
					int heroX = yuji.getX();
					int heroY = yuji.getY();
					
					// movement if no tree blocking
					if (curr=='w'&& heroY>-1+data.getSpeed()) {
						if (noTree(curr, heroX, heroY)) {
							yuji.setY(yuji.getY()-data.getSpeed());
							press=true;
						}
					} 
					if (curr=='s'&& heroY<20-data.getSpeed()) {
						if (noTree(curr, heroX, heroY)) {
							yuji.setY(yuji.getY()+data.getSpeed());
							press=true;
						}
					}
					if (curr=='a' && heroX>-1+data.getSpeed()) {
						if (noTree(curr, heroX, heroY)) {
							yuji.setX(yuji.getX()-data.getSpeed());
							press=true;
						}
					}
					if (curr=='d'&& heroX<20-data.getSpeed()) {
						if (noTree(curr, heroX, heroY)) {
							yuji.setX(yuji.getX()+data.getSpeed());
							press=true;
						}
					}
					if (curr=='e'|| curr=='E') {
						data.toggleSpeed();
						press=true;
					}
					
					heroX = yuji.getX();
					heroY = yuji.getY();
					String tile = data.getMap()[heroY][heroX];
					
					// water check
					if (tile.equals("w")) {
						v.swapPan(1);
						lose = true;
						done = true;
						lastTick = Integer.MAX_VALUE;
						ticks = 0;
					} else if (tile.equals("b")) {
						yuji.setHP(yuji.getHP()+10);
						data.toTile(heroX, heroY);
					}
					
					// entity check
					for (int i=0;i<5;i++) {
						if (otherEn[i].getX()==heroX&&otherEn[i].getY()==heroY) {
							if (i==0||i==1) {
								data.addFinger();
								yuji.setD(yuji.getDef()+25);
								yuji.setA(yuji.getAtk()+100);
								yuji.setHP(yuji.getHP()+25);
								yuji.setMaxNrg(yuji.getMaxNrg()+50);
							} else {
								btl = true;
								data.setOpponent(i+1);
								v.setData(data);
								mp = mname.equals("school") ? 0 : 1;
								if (otherEn[i].getName().equals("monsterNoob")) {
									atks[3][1] = 300/time.getDelay();
								} else {
									if (mp==0) {
										atks[3][1] = 200/time.getDelay();
									} else {
										atks[3][1] = 240/time.getDelay();
									}
								}
								bp = v.battle(mp, otherEn[i].getName());
								btlers[0] = yuji;
								btlers[0].setEn(btlers[0].getMaxNrg());
								btlers[1] = otherEn[i];
							}
							otherEn[i].setX(-1);
							otherEn[i].setY(-1);
						}
					}
					
					if (press) {
						ticks = -1;
						v.setData(data);
						v.drawMap();
					}
				}
			} else {
				if (!atking()&&!(bp.getCC()&&mp==0)) {
					if ((e.getKeyChar()=='a' || e.getKeyChar()=='A')&&this.atks[0][2]==0) {
						bp.setAtk(0, true);
						this.atks[0][2] = -1;
						// intMLife = Math.max(0, (int)(intMLife - intHAtk/4/(intMDef/5)));
						btlers[1].setHP(Math.max(0, (int)(btlers[1].getHP()-btlers[0].getAtk()/4/(btlers[1].getDef()/5))));
					} else if ((e.getKeyChar()=='q'||e.getKeyChar()=='Q')&&this.atks[1][2]==0&&btlers[0].getNrg()>=25&&this.atks[1][2]==0) {
						bp.setAtk(1, true);
						btlers[1].setHP(Math.max(0, (int)(btlers[1].getHP()-btlers[0].getAtk()/4/(btlers[1].getDef()/5))));
						btlers[0].setEn(btlers[0].getNrg()-25);
						this.atks[1][2] = -1;
					} else if ((e.getKeyChar()=='e'||e.getKeyChar()=='E')&&btlers[0].getNrg()>=40&&data.getFinger()>=2&&this.atks[2][2]==0) {
						bp.setAtk(2, true);
						btlers[0].setEn(btlers[0].getNrg()-40);
						this.atks[2][2] = -1;
					}
					
				}
			}
		}
	}
	
	public boolean atking() {
		boolean[] atks = bp.getAtk();
		for (int i=0;i<atks.length;i++) if (atks[i]) return true;
		return false;
	}
	
	public void checkTicks() {
		boolean[] atks = bp.getAtk();
		
		// yuji attacks
		for (int i=0;i<atks.length;i++) {
			if (atks[i]) {
				this.atks[i][0]++;
			}
			if (this.atks[i][0]==this.atks[i][1]) {
				bp.setAtk(i, false);
				this.atks[i][0] = 0;
				this.atks[i][2] = 0;
				if (i>0) bp.setMode(i-1, 0);
				//intMLife = Math.max(0, (int)(intMLife - intHAtk/(intMDef*0.8/5)));
				if (i==1) btlers[1].setHP(Math.max(0, (int)(btlers[1].getHP()-btlers[0].getAtk()/(btlers[1].getDef()*0.8/5))));
				//intMLife = Math.max(0, (int)(intMLife - intHAtk*2.5/(intMDef*0.8/5)));
				else if (i==2)  btlers[1].setHP(Math.max(0, (int)(btlers[1].getHP()-btlers[0].getAtk()*2.5/(btlers[1].getDef()*0.8/5)))); 
				// intHLife = Math.max(0, (int)(intHLife - intMAtk/(intHDef*0.8/5)));
				else if (i==3) {
					if (btlers[1].getName().equals("monsterNoob")) {
						btlers[0].setHP(Math.max(0, (int)(btlers[0].getHP()-btlers[1].getAtk()/(btlers[0].getDef()/5))));
					} else if (btlers[1].getName().equals("monsterBoss")) {
						if (mp==0) {
							//intHLife = Math.max(0, (int)(intHLife - intMAtk*3/(intHDef*0.8/5)));
							btlers[0].setHP(Math.max(0, (int)(btlers[0].getHP()-btlers[1].getAtk()*3/(btlers[0].getDef()*0.8/5))));
						}
					}
				}
			}
			// d-fist
			if (i==1) {
				if (this.atks[1][0]==this.atks[0][1]) {
					bp.setMode(0, bp.getMode(1)+1);
				}
			} else if (i==2) {
				// black flash explosion
				if (this.atks[2][0]==10) {
					bp.setMode(1, 1);
				} else if (this.atks[2][0]==14) {
					bp.setMode(1, 2);
				} else if (this.atks[2][0]==18) {
					bp.setMode(1, 3);
				}
			} 
		}
		// energy/hp gain + CC timer
		this.atks[4][0]++;
		if (this.atks[4][0]==this.atks[4][1]) {
			this.atks[4][0] = 0;
			btlers[0].setEn(Math.min(btlers[0].getMaxNrg(), btlers[0].getNrg()+btlers[0].getMaxNrg()/10));
			btlers[1].setEn(Math.min(btlers[1].getMaxNrg(), btlers[1].getNrg()+btlers[1].getMaxNrg()/5));
			if (bp.getCC()) {
				int[] cd = bp.getCCTime();
				System.out.println(cd[0]);
				if (cd[0]==cd[1]) {
					bp.setCC(false);
					cd[0] = 0;
				} else {
					bp.getCCTime()[0]++;
					if (mp==1) btlers[0].setHP(Math.max(0, btlers[0].getHP()-10));
				}
			}
			//hanami will regen 10% max HP when below half health 
			if (btlers[1].getName().equals("monsterBoss")) {
				if (mp==1) {
					btlers[1].setHP(btlers[1].getHP()+50);
				} else {
					btlers[1].setHP(btlers[1].getHP()+(int)((425-btlers[1].getHP())*0.135));
				}
			}
		}
		
		// enemy attack
		if (btlers[1].getMaxNrg()==btlers[1].getNrg()) {
			bp.setAtk(3, true);
			btlers[1].setEn(0);
		}
		
		if (btlers[0].getHP()<=0 || btlers[1].getHP()<=0) {
			btl = false;
			v.toMap();
			if (btlers[0].getHP()<=btlers[1].getHP()) { 
				done = true;
				lose = true;
				v.swapPan(1);
				v.over(lose);
				lastTick = Integer.MAX_VALUE;
				ticks = 0;
			} else {
				data.addKill();
			}
		}
		
		
	}
	
	public boolean noTree(char chrCurr, int intHeroX, int intHeroY) {
		data = v.getData();
		int intCount;
		int intVector;
		String[][] strMap = data.getMap();
		int intSpeed = data.getSpeed();
		
		//flip direction if going in a negative direction
		intVector = 1;
		if (chrCurr == 'a' || chrCurr == 'w') {
			intVector = -1;
		}
		
		//check next intSpeed tiles for trees based on key pressed
		if (chrCurr == 'd' || chrCurr == 'a'){
			for (intCount = 1; intCount < intSpeed+1; intCount++) {
				if (strMap[intHeroY][intHeroX+intCount*intVector].equals("t")) {
					return false;
				}
			}
		} else if (chrCurr == 'w' || chrCurr == 's'){
			for (intCount = 1; intCount < intSpeed+1; intCount++) {
				if (strMap[intHeroY+intCount*intVector][intHeroX].equals("t")) {
					return false;
				}
			}
		} 
		return true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public JJKMain() {
		v = new JJKUX();
		v.getFrame().addKeyListener(this);
		
		v.startGame(mname);
		v.drawMap();
		data = v.getData();
		time.start();
		
		int delay = time.getDelay();
		
		// animation times
		// irl time in ms/delay = ticks
		atks[0][1] = 200/delay; // normal attack
		atks[1][1] = 400/delay; // d-fist
		atks[2][1] = 575/delay; // b-flash
		atks[4][1] = 740/delay; // energy gain
	}
	
	public static void main(String[] args) {
		new JJKMain();
	}

	public void actionPerformed(ActionEvent e) {
		data = v.getData(); 
		if (e.getSource()==time) {
			ticks++;
			if (done&&ticks==40) {
				data = new JJKModel("forest");
				v.setData(data);
				lastTick = -1;
				done=false;
				v.swapPan(2);
				inGame = false;
			} else if (done&&ticks>=0&&ticks<40) v.over(lose);
			else if (data.getKill()==3&&!done) {
				lose = false;
				done = true;
				v.swapPan(1);
				ticks = 0;
				lastTick = Integer.MAX_VALUE;
			}
			
			if (btl) {
				bp.repaint(); 
				checkTicks();
			}
		}
	}
}

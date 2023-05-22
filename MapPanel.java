import java.awt.*;
import java.io.*;

import javax.swing.*;

public class MapPanel extends JPanel {
	JJKModel data;
	int tut = -1;
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		try {
			int p = data.getPix();
			for (int i=0;i<20;i++) {
				for (int j=0;j<20;j++) {
					String curr = data.getTile(i, j);
					if (curr.equals("g")) {
						g.drawImage(data.getIcon(0), i*p, j*p, null);
					} else if (curr.equals("b")) {
						g.drawImage(data.getIcon(3), i*p, j*p, null);
					} else if (curr.equals("w")) {
						g.drawImage(data.getIcon(1), i*p, j*p, null);
					} else if (curr.equals("t")) {
						g.drawImage(data.getIcon(2), i*p, j*p, null);
					}
				}
			}
			
			// HUD
			Entity yuji = data.getEn()[0];
			g.setFont(data.getFont());
			g.setColor(Color.white);
			g.drawLine(600, 0, 600, 600);
			g.drawString("Character: Yuji Itadori", 650, 40);
			g.drawImage(data.getModel(), 675, 70, null);
			g.drawString("Attack: " + yuji.getAtk(), 625, 500);
			g.drawString("Defense: " + yuji.getDef(), 800, 500);
			g.drawString("Life: " + yuji.getHP(), 625, 550);
			g.drawString("Fingers: " + data.getFinger(), 815, 550);
			String strMoveBuff1 = "Cursed Energy Movement";
			String strMoveBuff2 = "Enhancement Buff is";
			String strMoveBuff3 = "";
			int intAlign;
			int intAlign2;
			
			intAlign = 650;
			intAlign2 = 0;
			
			int intFinger = data.getFinger();
			
			//tell user status of movement buff
			if (intFinger > 0 && data.getSpeed() > 1) {
				strMoveBuff2 = strMoveBuff2 + " Active";
				g.drawString("(e) to toggle", 700, 370);
				intAlign = 625;
			} else if (intFinger == 0){
				strMoveBuff3 = "not unlocked";
				intAlign = 675;
				intAlign2 = 712;
			} else {
				strMoveBuff3 = " NOT Active!";
				g.drawString("(e) to toggle", 700, 370);
				intAlign = 675;
				intAlign2 = 712;
			}
			
			//formatting the toggle indicator
			g.drawString(strMoveBuff1, 650, 400);
			g.drawString(strMoveBuff2, intAlign, 425);
			g.drawString(strMoveBuff3, intAlign2, 450);
			
			
			Entity[] ens = data.getEn();
			for (int i=0;i<ens.length;i++) {
				g.drawImage(ens[i].getImg(), ens[i].getX()*p, ens[i].getY()*p, null);
			}
			
			if (tut!=-1&&tut<5) {
				g.drawImage(data.getDim(), 0, 0, null);
				if (tut==0)  g.drawString("WASD to move up, down, left and right respectively", 200, 350);
				else if (tut==1) g.drawString("Walk on fingers to eat them and boost your stats", 200, 350);
				else if (tut==2) {
					g.drawString("Pressing e after eating a finger allows you to jump multiple tiles", 100, 275);
					g.drawString("(press e again to disable)", 300, 300);
				} else if (tut==3) {
					g.drawString("You cannot walk into trees and attempting to walk on water kill you", 100, 325);
					g.drawString("Walking into bulidings will consume the building and heal you", 125, 350);
				} else if (tut==4) {
					g.drawString("You win the game by clearing all the monsters (blue and purple orbs)", 100, 150);
					g.drawString("During battle you have 3 attacks:", 250, 200);
					g.drawString("A - Normal attack; ", 250, 225);
					g.drawString("Q - Divergent Fist; and", 250, 250);
					g.drawString("E - Black Flash (unlocked at 2 fingers)", 250, 275);
					g.drawString("Each enemy also has a unique attack that goes off ", 225, 325);
					g.drawString("when they reach maximum energy", 300, 350);
				}
				g.drawString("(Click any key to continue)", 300, 400);
				System.out.println(tut);
			}
			
		} catch (NullPointerException e) {
		
		}
	}
	
	public void paintMap(JJKModel data) {
		this.data = data;
		this.repaint();
	}
	
	public MapPanel() {
		super();
		
	}
}

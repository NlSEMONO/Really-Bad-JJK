import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class JJKUX {
	private int panels = 4;
	private JFrame f = new JFrame("Really bad JJK");
	// 0 - help, 1 - map, 2 - fight, 3 - game over
	private MapPanel map = new MapPanel();
	private JPanel endPanel = new JPanel();
	private MenuPan menu;
	private BatPanel batpan;
	private JJKModel data;
	private Color[] cols = new Color[2];
	private JLabel lbl = new JLabel();
	
	public void swapPan(int idx) {
		if (idx==0) f.setContentPane(map);
		else if (idx==1) f.setContentPane(endPanel);
		else if (idx==2) f.setContentPane(menu);
		f.pack();
	}
	
	public BatPanel battle(int mp, String name) {
		batpan = new BatPanel(mp, name, data);
		f.setContentPane(batpan);
		f.pack();
		return batpan;
	}
	
	public void toMap() {
		f.setContentPane(map);
		f.pack();
	}
	
	public JJKModel getData() {
		return data;
	}
	
	public void drawMap() {
		map.data = data;
		boolean nb = true;
		for (int i=0;i<5;i++) if (!data.getTutProg()[i]) {
			map.tut = i;
			nb = false;
			break;
		}
		if (nb) map.tut = 5;
		map.repaint();
	}
	
	public void startGame(String map){
		data = new JJKModel(map);
	}
	
	public JFrame getFrame() {
		return f;
	}
	
	public void setData(JJKModel data) {
		this.data = data;
	}
	
	public void over(boolean lose) {
		Graphics g = endPanel.getGraphics();
		if (lose) {
			lbl.setLocation(350, 275);
			lbl.setText("YOU DIED! Game Over!");
		} else {
			lbl.setLocation(250, 275);
			lbl.setText("Good job on clearing all the cursed spirits!");
		}
	}
		
	public JJKUX() {
		int i;
		map.setLayout(null);
		map.setPreferredSize(new Dimension(1000, 600));
		
		endPanel.setLayout(null);
		endPanel.setPreferredSize(new Dimension(1000, 600));
		endPanel.add(lbl);
		lbl.setSize(1000000, 50);
		
		cols[0] = Color.BLACK;
		cols[1] = Color.white;
		
		endPanel.setBackground(cols[0]);
		
		lbl.setFont(new Font("Arial.ttf", Font.PLAIN, 20));
		lbl.setForeground(cols[1]);
		
		
		menu = new MenuPan();
		f.setContentPane(menu);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
	}
	public class MenuPan extends JPanel {
		JJKModel data = new JJKModel("school");
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.black);
			g.fillRect(0, 0, 1280, 720);
			g.setFont(data.getFont());
			g.setColor(Color.white);
			g.drawString("(S)chool", 0, 30);
			g.drawString("(F)orest", 0, 70);
			g.drawString("(T)utorial", 0, 110);
			g.drawString("(Q)uit", 0, 150);
		}
		public MenuPan() {
			setLayout(null);
			setPreferredSize(new Dimension(1000, 600));
		}
	}
}


package Tetris;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class LineFrame extends JFrame implements Runnable {
	final int BLOCK = 100;

	LinePanel lp;
	Container con;
	TetrlXBlock[] tb = new TetrlXBlock[BLOCK];
	Graphics g;
	DrawRectPrint DR;
	Thread t = new Thread();
	int blockNum = 0;
	int speed =70;

	public LineFrame() {
		super("TetrlX");
		this.addKeyListener(new KeyListener());
		con = getContentPane();
		lp = new LinePanel();
		DR = new DrawRectPrint();
		setSize(550, 550);
		con.add(DR);
		con.add(lp);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		g = lp.getGraphics();
		for (int i = 0; i < BLOCK; i++) {
			tb[i] = new TetrlXBlock(g);
		}
		g.drawString("score : " + DR.score, 350, 250);
		GameStart();
	}

	boolean wh = true;

	public void GameStart() {
		while (wh) {
			if (MaxYTable(tb[blockNum].startX, tb[blockNum].y/20)-tb[blockNum].shapeY <= tb[blockNum].minY) {
				wh = false;
			} else {
				run();
			}
		}
		GameOver();
	}

	public void GameOver() {
		lp.game=false;
		lp.background(g,DR.score);
		setVisible(true);
	}

	public void repaint() {
		lp.background(g,DR.score);
		DR.DrawTable(g);
		g.drawString("score : " + DR.score, 350, 270);
		DR.level = (DR.score / 300) + 1;
		speed =10;
		//speed = 80 - DR.level * 10;
		g.drawString("Level : " + DR.level, 350, 250);

		/// Next Block
		if (blockNum + 1 < BLOCK) {
			tb[blockNum].ClearNextShape(330, 90);
			tb[blockNum + 1].DrawNextShape(330, 90);
			con.add(tb[blockNum]);
		}
		setVisible(true);
	}

	public int MaxYTable(int x, int y) {
		for (int j = y; j < DR.table.length; j++) {
			if (DR.table[j][x] == 1) {
				return j * 20 + 30;
			}
		}
		return 450;
	}
	
	public void MaxYTableComp(int[] width, int x) {
		int c = 0;
		tb[blockNum].widCount = 0;
		
		for (int i = x; i < x + width.length; i++) {
			if (tb[blockNum].fix == false) {
				tb[blockNum].widthPos = width[tb[blockNum].widCount];
				int posY = (tb[blockNum].widthPos * 20);
				int maxYtemp =MaxYTable(i,tb[blockNum].y/20);

				if (tb[blockNum].y + posY + 5 > maxYtemp) {
					tb[blockNum].y = maxYtemp - posY;
					tb[blockNum].fix = true;
				} else if (tb[blockNum].y + posY == maxYtemp) {
					tb[blockNum].y = maxYtemp - posY;
					tb[blockNum].fix = true;
				}else {
					c++;
					tb[blockNum].widCount++;
					if (c == width.length) {
						tb[blockNum].y += 5;
					}
				}
			}
		}
		if (tb[blockNum].fix == true) {
			tb[blockNum].fixShape(DR.table, tb[blockNum].widCount);
			DR.Table();
		//	DR.SetMaxYTable();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			if (tb[blockNum].MaxYTable[tb[blockNum].startX] - (tb[blockNum].widthPos * 20) <= tb[blockNum].minY) {
				GameOver();
			}
			
			//tb[blockNum].ClearShape();
			tb[blockNum].DrawShape();
			MaxYTableComp(tb[blockNum].WidthArry, tb[blockNum].startX);
			t.sleep(speed);
			
			if (tb[blockNum].fix == true) {
				blockNum++;
				if (blockNum < BLOCK) {
					DR.score += 10;
				} else {
					wh = false;
				}
			}
			con.add(tb[blockNum]);
			repaint();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	class KeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if (blockNum > BLOCK) {
				repaint();
			}
			if (tb[blockNum].fix == false) {
				g.setColor(Color.white);
				g.fillRect(10, 25, 240, 425);
				repaint();

				switch (keycode) {
				case 32:// space
					tb[blockNum].setSpace();
					break;
				case 39: // right
					if ((tb[blockNum].startX + 1 < 12) && MaxYTable(tb[blockNum].startX + 1,tb[blockNum].y/20) >= tb[blockNum].y) {
						if (tb[blockNum].x < tb[blockNum].maxX) {
							tb[blockNum].x += 20;
							tb[blockNum].startX += 1;
						} else if (tb[blockNum].x >= tb[blockNum].maxX || tb[blockNum].startX > 11) {
							tb[blockNum].x = tb[blockNum].maxX;
							tb[blockNum].startX = 11;
						}
					}
					break;
				case 37:// left
					 
					if ((tb[blockNum].startX - 1 > -1) && MaxYTable(tb[blockNum].startX - 1,tb[blockNum].y/20) >= tb[blockNum].y) {
						if (tb[blockNum].x > tb[blockNum].minX) {
							tb[blockNum].x -= 20;
							tb[blockNum].startX -= 1;
						} else if (tb[blockNum].x <= tb[blockNum].minX || tb[blockNum].startX < 0) {
							tb[blockNum].x = tb[blockNum].minX;
							tb[blockNum].startX = 0;
						}
					}
					break;
				case 40:// down
					if (tb[blockNum].fix == false) {
						tb[blockNum].y += 5;
					}
					break;
				case 27:
					System.exit(0);
				}
			}
		}
	}
}

class LinePanel extends JPanel {
	JLabel next = new JLabel("next");
	Graphics graphics;
	boolean game = true;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		background(g, 0);
		graphics = g;
	}

	public Graphics gh() {
		return graphics;
	}

	
	void background(Graphics g,int score) {
		g.setColor(Color.white);
		g.fillRect(10, 25, 240, 425);

		g.setColor(Color.black);
		g.drawLine(9, 24, 9, 451);// left
		g.drawLine(9, 24, 251, 24);// top
		g.drawLine(251, 24, 251, 451);// right
		g.drawLine(9, 451, 251, 451);// bottom

		// next
		next.setBounds(355, 25, 110, 15);
		this.add(next);
		g.setColor(Color.white);
		g.fillRect(300, 40, 150, 180);

		g.setColor(Color.black);
		g.drawLine(300, 40, 300, 220);// left
		g.drawLine(300, 40, 450, 40);// top
		g.drawLine(450, 40, 450, 220);// right
		g.drawLine(300, 220, 450, 220);// bottom

		g.setFont(new Font("Batang", Font.PLAIN , 20));
		g.drawString("--> : right Move", 320, 330);
		g.drawString("<-- : left Move", 320, 360);
		g.drawString("spacebar : Angle conversion ", 260, 390);
		g.drawString("exc : Game exit ", 320, 420);
		if (game == false) {
			GameOver(g,score);
		} 
	}
	
	public void GameOver(Graphics g,int score) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 550, 550);
		
		g.setColor(Color.RED);
		g.setFont(new Font("Batang", Font.ITALIC + Font.BOLD, 50));
		g.drawString("GameOver....", 130, 250);
		g.drawString("Score : " + score, 130, 310);
		g.setFont(getFont());
	}

}

public class TetrlX {

	public static void main(String[] args) {
		LineFrame myf = new LineFrame();
		//myf.repaint();
	}
}
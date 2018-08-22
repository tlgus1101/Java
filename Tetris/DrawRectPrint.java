package Tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawRectPrint extends JPanel {

	int[][] table;
	int x, y;
	int score;
	int level;
	Color c =  RandomColor();
	
	public DrawRectPrint() {
		x = 0;
		y = 0;
		score = 0;
		level=1;
		this.table = new int[21][12];
	}

	public Color RandomColor() {
		int red = (int) (Math.random() * 256);
		int green = (int) (Math.random() * 256);
		int blue = (int) (Math.random() * 256);
		Color cc = new Color(red, green, blue);
		return cc;
	}

	public void DrawTable(Graphics g) {

		for (int i = 0; i < 21; i++) {

			for (int j = 0; j < 12; j++) {
				if (table[i][j] == 1) {
					x = 10 + (j * 20);
					y = (i * 20) + 30;
					g.setColor(c);
					g.fillRect(x, y, 20, 20);
					g.setColor(Color.black);
					g.drawRect(x, y, 20, 20);
				}
			}
		}
	}

	public void removeTable(int c) {
		table[0] = new int[12];
		int temp[] = new int[12];
		for (int i = c; i > 0; i--) {
			table[i] = table[i - 1];
		}
		this.score += 100;
	}

	public void Table() {
		for (int i = 0; i < 21; i++) {
			int count = 0;
			for (int j = 0; j < 12; j++) {
				if (table[i][j] == 1) {
					count++;
					if (count == 12) {
						removeTable(i);
					}
				}
			}
		}
	}

	public void TablePrint() {
		for (int i = 0; i < 21; i++) {
			int count = 0;
			for (int j = 0; j < 12; j++) {
				System.out.print(table[i][j]);
			}
			System.out.println();
		}
	}

}
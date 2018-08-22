package Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TetrlXBlock  extends JPanel{
	BlockRandom blockShape=new BlockRandom() ;
	public int maxX = 250;
	public int minX = 10;
	public int maxY = 450;
	public int minY = 25;
	
	int startX;
	
	int [] MaxYTable;
	int WidthArry[];
	int x=110,y=25;
	int shapeY;
	Graphics g;
	boolean fix = false;
	int fi, fj,minj = 5,maxj=0;
	int mini=5;
	int maxi = 0;
	int widthPos = 0;
	int widCount;
	Color c = RandomColor();

	int shape[][] = new int [4][4];
	boolean space =true;
	

	public void SetMaxMinTable(int [] MaxYTable)
	{
		this.MaxYTable = MaxYTable;
	}
	
	TetrlXBlock(Graphics g) {
		this.g = g;
		shape = blockShape.RandomShape();
		MaxYTable=new int [12];
		startX=(this.x/20);
		for(int i=0;i<MaxYTable.length;i++)
		{
			MaxYTable[i]=450;
		}
		FindMax(shape);
		while (true) {
			if(posChangeShape() == true) 
				break;
			FindMax(shape);
		}
		WidthArry=new int [maxj-minj+1];
		Width();
		widthPos=WidthArry[0];
		widCount=0;
	}

	public void FindMax(int[][] shape) {
		maxX = 250; minX = 10;
		maxY = 450; minY = 25;
		minj = 5; maxj=0;
		mini=5; maxi=0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (shape[i][j] == 1) {
					if (maxi < i) {
						maxi = i;
					}
					if (mini > i) {
						mini = i;
					}
					if (minj > j) {
						minj = j;
						fi = i;
					}
					if (maxj < j) {
						maxj = j;
					}

				}
			}
		}
		fj = minj;
		minX = minX - (minj * 20);
		maxY = maxY - ((maxi + 1) * 20);
		maxX -= ((maxj + 1) * 20);
		startX = (this.x/20);// + fj;
		shapeY = ((maxi + 1) * 20);
	}
	
	public boolean posChangeShape()
	{
		if(minj!=0)
		{
			int temp[][] = new int[4][4];
			for(int i=0;i<shape.length;i++)
			{
				for(int j=0;j<shape.length-1;j++)
				{
					temp[i][j]=shape[i][j+1];
				}
			}
			this.shape=temp;
			return false;
		}
		
		
		if(mini!=0)
		{
			int temp[][] = new int[4][4];
			for(int i=0;i<shape.length-1;i++)
			{
				for(int j=0;j<shape.length;j++)
				{
					temp[i][j]=shape[i+1][j];
				}
			}
			this.shape=temp;
			return false;
		}
		return true;
	}

	public void Width() {
		int count = fj;
		int xPosition = 1;

		for (int i = 0; i < WidthArry.length; i++) {
			for (int j = 0; j < 4; j++) {
				if (shape[j][count] == 1) {
					WidthArry[i] = xPosition;
				}
				xPosition += 1;
			}
			count++;
			xPosition = 1;
		}
	}

	public void paintComponent(Graphics g) {
	}

	public Color RandomColor() {
		int red = (int) (Math.random() * 256);
		int green = (int) (Math.random() * 256);
		int blue = (int) (Math.random() * 256);
		Color cc = new Color(red, green, blue);
		return cc;
	}
	
	public void DrawShape() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (shape[i][j] == 1) {
					g.setColor(c);
					g.fillRect(x+(j*20), y+(i*20), 20, 20);
					g.setColor(Color.black);
					g.drawRect(x+(j*20), y+(i*20), 20, 20);
				}
			}
		}
	}

	public void ClearShape() {
		if (fix != true) {
			g.setColor(Color.WHITE);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (shape[i][j] == 1) {
						g.fillRect(x + (j * 20), y + (i * 20), 20, 20);
					}
				}
			}
		}
	}

	public void setSpace() {
		if (fix != true) {
			int[][] b = new int[4][4];
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					b[i][j] = shape[j][3 - i];
				}
			}
			FindMax(b);
			
			if (x <= maxX && y <= maxY && x >= minX) {
				shape = b;	
				while (true) {
					if(posChangeShape() == true) 
						break;
					FindMax(shape);
				}
				WidthArry = new int[maxj - minj + 1];
				Width();
				widthPos=WidthArry[0];
			} else {
				FindMax(shape);
				Width();
			}
		}
	}

	public void fixShape(int[][] table, int xPos) {
		int x = (this.x / 20);// y�ġ
		if (this.x < 0) {
			x = -1;
		}
		int y = this.y / 20;// x�ġ
		if (y > 0) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (shape[i][j] == 1) {
						table[y + i - 1][x + j] = 1;
					}
				}
			}
		}
	}

	//// Next/////
	public void DrawNextShape(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (shape[i][j] == 1) {
					g.setColor(c);
					g.fillRect(x + (j * 20), y + (i * 20), 20, 20);
					g.setColor(Color.black);
					g.drawRect(x + (j * 20), y + (i * 20), 20, 20);
				}
			}
		}
	}

	public void ClearNextShape(int x, int y) {
		g.setColor(Color.WHITE);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (shape[i][j] == 1) {
					g.fillRect(x + (j * 20), y + (i * 20), 20, 20);
				}
			}
		}
	}

	public class BlockRandom {
		int shape1[][] = { { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 } };
		int shape2[][] = { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } };
		int shape3[][] = { { 0, 0, 0, 0 }, { 0, 0, 1, 1 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 } };
		int shape4[][] = { { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } };
		int shape5[][] = { { 0, 0, 0, 0 }, { 0, 0, 1, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 } };
		int shape6[][] = { { 0, 0, 0, 0 }, { 0, 1, 1, 1 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 } };
		int shape7[][] = { { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 } };

		public int[][] RandomShape() {
			Random r1 = new Random();
			int index = r1.nextInt(7);
			//int index =1;
			switch (index) {
			case 1:
				return shape1;
			case 2:
				return shape2;
			case 3:
				return shape3;
			case 4:
				return shape4;
			case 5:
				return shape5;
			case 6:
				return shape6;
			case 7:
				return  shape7;
			}
			return shape1;
		}
		
	}
}

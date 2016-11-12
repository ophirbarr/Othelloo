import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardPanel extends JPanel
{
	private final Icon blackIcon=new ImageIcon(getClass().getResource("/black.png"));
	private final Icon whiteIcon=new ImageIcon(getClass().getResource("/white.png"));
	private JButton grid[][]=new JButton[8][8];
	enum Status { EMPTY, BLACK, WHITE};
	private Status gridStatus [][]=new Status[8][8];
	private Status turn = Status.BLACK;
	private int black_score = 2;
	private int white_score = 2;


	public BoardPanel()
	{
		super();
		setLayout( new GridLayout(8,8)) ; // set frame layout, with 64 items.
		BoardHandler handler = new BoardHandler();

		for(int x=0; x<8; x++)
			for(int y=0; y<8; y++)
			{
				grid[x][y] = new JButton();
				grid[x][y].setBackground(Color.ORANGE);
				add(grid[x][y]);
				grid[x][y].addActionListener(handler);
				gridStatus[x][y]=Status.EMPTY;
			}
		
		// place initial pieces
		grid[3][3].setIcon(blackIcon);
		gridStatus[3][3] = Status.BLACK;
		grid[4][4].setIcon(blackIcon);
		gridStatus[4][4] = Status.BLACK;
		grid[3][4].setIcon(whiteIcon);
		gridStatus[3][4] = Status.WHITE;
		grid[4][3].setIcon(whiteIcon);
		gridStatus[4][3] = Status.WHITE;
		
	}
	
	
	private class BoardHandler implements ActionListener
	 {
	 // handle button event
		public void actionPerformed( ActionEvent event )
		{
			int pos[]= getIJ(event);
			if (gridStatus[pos[0]][pos[1]] == Status.EMPTY)
			{
				// calling check methods one by one to avoid compiler short circuiting
				boolean check = false;
				if (checkVertical(pos)) check = true; 
				if (checkHorizonal(pos)) check = true;
				if (checkCrossRight(pos)) check = true;
				if (checkCrossLeft(pos)) check = true;
				
				if (check && turn == Status.BLACK)
				{
					grid[pos[0]][pos[1]].setIcon(blackIcon);
					gridStatus[pos[0]][pos[1]] = Status.BLACK;
					turn = Status.WHITE;
				}
				else if (check && turn == Status.WHITE)
				{
					grid[pos[0]][pos[1]].setIcon(whiteIcon);
					gridStatus[pos[0]][pos[1]] = Status.WHITE;
					turn = Status.BLACK;
				}
				
				Othelloo.updateScore(black_score, white_score);
			}
			
		} // actionPerformed
		
		
		// ---- check methods ahead ----
		
		///// CHECK Y AXIS /////
		private boolean checkVertical(int[] pos)
		{
			int countTrapped = 0, i = 0;
			boolean isLegal = false;
			Status myStatus	= turn, opponentStatus = (turn == Status.BLACK) ? Status.WHITE : Status.BLACK;
			
			gridStatus[pos[0]][pos[1]] = myStatus;
			
			while (i < 6)
			{
				while (i < 6){   // find player's piece
					if (gridStatus[i][pos[1]] == myStatus)
						break;
					i++;
				}
				// if adjacent piece is opponent's, count opponent pieces
				if (gridStatus[i + 1][pos[1]] == opponentStatus)
				{
					countTrapped = 0;
					int j;
					for (j = i + 1; (j < 7) && (gridStatus[j][pos[1]] == opponentStatus); j++)
						countTrapped++;
					i++;
					if (gridStatus[j][pos[1]] == myStatus  // capture trapped pieces
							&& (i - 1 == pos[0] || j == pos[0]))
					{
						int t = countTrapped;
						while (t-- > 0)
							if (myStatus == Status.WHITE){
								grid[i][pos[1]].setIcon(whiteIcon);
								gridStatus[i++][pos[1]] = Status.WHITE;
							}
							else{
								grid[i][pos[1]].setIcon(blackIcon);
								gridStatus[i++][pos[1]] = Status.BLACK;
							}
						isLegal = true; 
						if (myStatus == Status.WHITE)
						{
							white_score += countTrapped + 1;
							black_score -= countTrapped;
						}
						else 
						{
							black_score += countTrapped + 1;
							white_score -= countTrapped;
						}
					}
				}
				i++;
			} // while
			if (!isLegal) gridStatus[pos[0]][pos[1]] = Status.EMPTY;  // restore status
			return isLegal;
		}
		
		///// CHECK X AXIS /////
		private boolean checkHorizonal(int[] pos)
		{
			int countTrapped = 0, i = 0;
			boolean isLegal = false;
			Status myStatus	= turn, opponentStatus = (turn == Status.BLACK) ? Status.WHITE : Status.BLACK;
			
			gridStatus[pos[0]][pos[1]] = myStatus;
			
			while (i < 6)
			{
				while (i < 6){   // find player's piece
					if (gridStatus[pos[0]][i] == myStatus)
						break;
					i++;
				}
				// if adjacent piece is opponent's, count opponent pieces
				if (gridStatus[pos[0]][i + 1] == opponentStatus)
				{
					countTrapped = 0;
					int j;
					for (j = i + 1; (j < 7) && (gridStatus[pos[0]][j] == opponentStatus); j++)
						countTrapped++;
					i++;
					if (gridStatus[pos[0]][j] == myStatus  // capture trapped pieces
							&& (i - 1 == pos[1] || j == pos[1])) 
					{
						int t = countTrapped;
						while (t-- > 0)
							if (myStatus == Status.WHITE){
								grid[pos[0]][i].setIcon(whiteIcon);
								gridStatus[pos[0]][i++] = Status.WHITE;
							}
							else{
								grid[pos[0]][i].setIcon(blackIcon);
								gridStatus[pos[0]][i++] = Status.BLACK;
							}
						isLegal = true; 
						if (myStatus == Status.WHITE)
						{
							white_score += countTrapped + 1;
							black_score -= countTrapped;
						}
						else 
						{
							black_score += countTrapped + 1;
							white_score -= countTrapped;
						}
					}
				}
				i++;
			} // while
			if (!isLegal) gridStatus[pos[0]][pos[1]] = Status.EMPTY;  // restore status
			return isLegal;
		}
		
		
		///// CHECK CROSS RIGHT   /////
		private boolean checkCrossRight(int[] pos)
		{
			int countTrapped = 0, i1, i2;
			boolean isLegal = false;
			Status myStatus	= turn, opponentStatus = (turn == Status.BLACK) ? Status.WHITE : Status.BLACK;
			
			gridStatus[pos[0]][pos[1]] = myStatus;
			
			i1 = (pos[0] - pos[1] >= 0) ? pos[0] - pos[1] : 0;
			i2 = (pos[1] - pos[0] >= 0) ? pos[1] - pos[0] : 0;
			while (i1 < 6 && i2 < 6) 
			{
				while (i1 < 6 && i2 < 6){   // find player's piece
					if (gridStatus[i1][i2] == myStatus)
						break;
					i1++;
					i2++;
				}
				// if adjacent piece is opponent's, count opponent pieces
				if (gridStatus[i1 + 1][i2 + 1] == opponentStatus)
				{
					countTrapped = 0;
					int j1, j2;
					for (j1 = i1 + 1, j2 = i2 + 1; (j1 < 7) && (j2 < 7) && (gridStatus[j1][j2] == opponentStatus); j1++, j2++)
						countTrapped++;
					i1++;
					i2++;
					if (gridStatus[j1][j2] == myStatus  // capture trapped pieces
							&& (i1 - 1 == pos[0] || j1 == pos[0]))
					{
						int t = countTrapped;
						while (t-- > 0)
							if (myStatus == Status.WHITE){
								grid[i1][i2].setIcon(whiteIcon);
								gridStatus[i1++][i2++] = Status.WHITE;
							}
							else{
								grid[i1][i2].setIcon(blackIcon);
								gridStatus[i1++][i2++] = Status.BLACK;
							}
						isLegal = true; 
						if (myStatus == Status.WHITE)
						{
							white_score += countTrapped + 1;
							black_score -= countTrapped;
						}
						else 
						{
							black_score += countTrapped + 1;
							white_score -= countTrapped;
						}
					}
				}
				i1++;
				i2++;
			} // while
			if (!isLegal) gridStatus[pos[0]][pos[1]] = Status.EMPTY;  // restore status
			return isLegal;
		}
		
		
		
		///// CHECK CROSS LEFT  /////
		private boolean checkCrossLeft(int[] pos)
		{
			int countTrapped = 0, i1, i2;
			boolean isLegal = false;
			Status myStatus	= turn, opponentStatus = (turn == Status.BLACK) ? Status.WHITE : Status.BLACK;
			
			gridStatus[pos[0]][pos[1]] = myStatus;
			
			i1 = (pos[0] - ( 7 - pos[1]) >= 0) ? pos[0] - ( 7 - pos[1]) : 0;
			i2 = (pos[1] + pos[0] <= 7) ? pos[1] + pos[0] : 7;
			while (i1 < 6 && i2 > 1) 
			{
				while (i1 < 6 && i2 > 1){   // find player's piece
					if (gridStatus[i1][i2] == myStatus)
						break;
					i1++;
					i2--;
				}
				// if adjacent piece is opponent's, count opponent pieces
				if (gridStatus[i1 + 1][i2 - 1] == opponentStatus)
				{
					countTrapped = 0;
					int j1, j2;
					for (j1 = i1 + 1, j2 = i2 - 1; (j1 < 7) && (j2 > 0) && (gridStatus[j1][j2] == opponentStatus); j1++, j2--)
						countTrapped++;
					i1++;
					i2--;
					if (gridStatus[j1][j2] == myStatus  // capture trapped pieces
							&& (i1 - 1 == pos[0] || j1 == pos[0]))
					{
						int t = countTrapped;
						while (t-- > 0)
							if (myStatus == Status.WHITE){
								grid[i1][i2].setIcon(whiteIcon);
								gridStatus[i1++][i2--] = Status.WHITE;
							}
							else{
								grid[i1][i2].setIcon(blackIcon);
								gridStatus[i1++][i2--] = Status.BLACK;
							}
						isLegal = true; 
						if (myStatus == Status.WHITE)
						{
							white_score += countTrapped + 1;
							black_score -= countTrapped;
						}
						else 
						{
							black_score += countTrapped + 1;
							white_score -= countTrapped;
						}
					}
				}
				i1++;
				i2--;
			} // while
			if (!isLegal) gridStatus[pos[0]][pos[1]] = Status.EMPTY;  // restore status
			return isLegal;
		}
		
		
		private int[] getIJ(ActionEvent e)
		{
			JButton sourceButton = (JButton)e.getSource();
			int [] arr=new int[2];
			for(int x=0; x<8; x++)
				for(int y=0; y<8; y++)
					if(sourceButton==grid[x][y])
					{
						arr[0]=x;
						arr[1]=y;
						break;
					}
			return arr;
		} // end private inner class ButtonHandler
		
	 } // end class TextFieldFrame

	
}

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Othelloo
{
	private static JLabel score;
	
	public static void main( String args[] )
	{
		
		JFrame mainFrame = new JFrame("Othelloo");
		mainFrame.setSize( 500,500); // set frame size
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		score = new JLabel("SCORE:    black 2  -  white 2");
		mainFrame.add(score, BorderLayout.NORTH);  // add score
		mainFrame.add(new BoardPanel(), BorderLayout.CENTER);  // add game board
		mainFrame.setVisible( true ); // display frame

	} // MAIN
	
	public static void updateScore(int x, int y)
	{
		score.setText("SCORE:    black " + x + "  -  white " + y);
	}
} 




// 		board.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
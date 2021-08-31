
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;


public class PacManGame extends JFrame implements KeyListener{

	private enum STATUS {PACMAN,EMPTY,PELLET,BLOCKED};
	private enum DIR {LEFT,RIGHT,UP,DOWN};
	
	private final int NUM_ROWS = 20;
	private final int NUM_COLS = 18;
	
	private  BufferedImage[] PAC_IMAGE;			//left, right, up, down
	private  BufferedImage PELLET_IMAGE=null;;

	private PicPanel[][] allPanels;

	private int pacRow =1;						//location of pacman
	private int pacCol =1;
	
	public PacManGame(){

		PAC_IMAGE = new BufferedImage[4];
		
		try {

			PAC_IMAGE[0] = ImageIO.read(new File("pac_left.jpg"));
			PAC_IMAGE[1] = ImageIO.read(new File("pac_right.jpg"));
			PAC_IMAGE[2] = ImageIO.read(new File("pac_up.jpg"));
			PAC_IMAGE[3] = ImageIO.read(new File("pac_down.jpg"));
			PELLET_IMAGE = ImageIO.read(new File("pellet.png"));

		} catch (IOException ioe) {
			System.out.println("Could not read pacman pics");
			System.exit(0);
		}

		setSize(600,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Pacman");
		setLayout(new GridLayout(NUM_ROWS,NUM_COLS));
		allPanels = new PicPanel[NUM_ROWS][NUM_COLS];
		
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(new File("maze.txt"));
		}catch(FileNotFoundException e) {
			System.exit(-1);
		}

		for(int row=0; row<NUM_ROWS; row++) {
			String readRow = fileIn.nextLine();
			for(int col=0; col<NUM_COLS; col++) {
				PicPanel toAdd = new PicPanel(readRow.substring(col, col+1));
				allPanels[row][col] = toAdd;
				add(toAdd);
			}
		}
		
		allPanels[1][1].addPacman(DIR.RIGHT);
		this.addKeyListener(this);		
		
		setVisible(true);
	}
	
	public void keyPressed(KeyEvent arg0) {
		
		int keyVal = arg0.getKeyCode();
		
		if(keyVal == KeyEvent.VK_LEFT) 
			makeMove(DIR.LEFT);
		else if(keyVal == KeyEvent.VK_RIGHT)
			makeMove(DIR.RIGHT);
		else if(keyVal == KeyEvent.VK_DOWN)
			makeMove(DIR.DOWN);
		else
			makeMove(DIR.UP);		
	}
	
	public void keyReleased(KeyEvent arg0) {
		
	}
	
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	private void makeMove(DIR direction) {
		
		int nextRow = pacRow;
		int nextCol = pacCol;
		
		if(direction == DIR.LEFT)
			nextCol--;
		else if(direction == DIR.RIGHT)
			nextCol++;
		else if(direction == DIR.DOWN)
			nextRow++;
		else
			nextRow--;

		if(allPanels[nextRow][nextCol].panelStat != STATUS.BLOCKED) {
			allPanels[pacRow][pacCol].clearPanel();
			allPanels[nextRow][nextCol].addPacman(direction);
			pacRow = nextRow;
			pacCol = nextCol;			
		}
	}

	class PicPanel extends JPanel{

		private BufferedImage image;		
		private STATUS panelStat;
		
		//takes in a single val from the file (either "x" or "o")
		public PicPanel(String val){
			if(val.equals("o")) {
				panelStat = STATUS.PELLET;
				image = PELLET_IMAGE;
			}
			else
				panelStat = STATUS.BLOCKED;
		}

		public void clearPanel() {
			image = null;
			panelStat = STATUS.EMPTY;
			repaint();
		}
		
		public void addPacman(DIR direction) {
			image = PAC_IMAGE[direction.ordinal()];
			panelStat = STATUS.PACMAN;
			repaint();
		}
		
		//this will draw the image
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			if(panelStat == STATUS.EMPTY)
				setBackground(Color.black);
			else if(panelStat == STATUS.BLOCKED)
				setBackground(Color.blue);
			else
				g.drawImage(image,0,0,this);			
		}
	}

	

	public static void main(String[] args){
		new PacManGame();
	}
}

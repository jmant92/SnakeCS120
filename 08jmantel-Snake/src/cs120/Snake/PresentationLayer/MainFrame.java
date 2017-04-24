package cs120.Snake.PresentationLayer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import cs120.Snake.Controller.GameController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * The main class of the snake game. It is the presentation layer. It has a handle
 * on a game controller object so that it can communicate with the back end methods.
 * It takes those methods and shows them to the end user. It shows the score, lives left,
 * under certain circumstances how many turns will be ignored, and has a quit button.
 * It has a timer that only renders the screen.
 * @author JonathanMantel
 *
 */
public class MainFrame extends JFrame{
	private GameController gc; // the game controller object to handle dealings between this layer and the back end
	
	private final int MAX_WIDTH = 800; // Width of the game window
	private final int MAX_HEIGHT = 800; // Height of the game window
	private JPanel legend; // a panel to show instructions and what all the symbols mean
	private JPanel center; // a panel to hold the canvas and the dash board
	private Canvas canvas; // a panel to show the game play screen
	private JPanel dash; // a dash board to show the points, lives, ignored turns left, etc.
	private JPanel pnlQuit; // a panel to hold the quit button
	private JButton btnQuit; // a quit button
	private JLabel lblPoints; // a label to show the points
	private JLabel lblLives; // a label to show the lives
	private JPanel pnlStats; // a panel to hold the game stats
	private JLabel lblIgnore; // a label to show the number of ignored turns left
	
	private Timer mainT; // the main timer for the game. Repaints the renders the screen
	// for the mushroom legend
	private JLabel lblM1;
	private JLabel lblM4;
	private JLabel lblM3;
	private JLabel lblM2;
	
	/**
	 * The constructor for the main frame deals strictly with the looks of the frame. It has a canvas to play the game, 
	 * a legend for determining the mushrooms, and a dashboard for the score, lives, ignored turns, and quitting. It also
	 * starts the timer for rendering the game. It creates a new instance of a GameController object so that it can communicate
	 * with the back end layer.
	 */
	public MainFrame() {
		this.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		center = new JPanel();
		getContentPane().add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));
		
		canvas = new Canvas();
		canvas.setFocusable(true);
		canvas.setRequestFocusEnabled(true);
		canvas.addKeyListener(new KeyHelper());
		center.add(canvas, BorderLayout.CENTER);
		canvas.setBackground(Color.blue);
		
		int canvasDim = MAX_WIDTH*6/8; // the size of the canvas
		canvas.setMinimumSize(new Dimension(canvasDim, canvasDim));
		
		gc = new GameController(canvasDim, canvasDim); // make a new game controller with the size of the canvas
		// passed through
		
		dash = new JPanel();
		center.add(dash, BorderLayout.SOUTH);
		dash.setPreferredSize(new Dimension(MAX_WIDTH*(6/8), MAX_HEIGHT*2/8));
		dash.setBackground(Color.red);
		dash.setLayout(new BorderLayout(0, 0));
		
		pnlQuit = new JPanel();
		FlowLayout fl_pnlQuit = (FlowLayout) pnlQuit.getLayout();
		fl_pnlQuit.setAlignment(FlowLayout.TRAILING);
		pnlQuit.setBackground(Color.RED);
		dash.add(pnlQuit, BorderLayout.SOUTH);
		
		btnQuit = new JButton("Quit");
		btnQuit.setVerticalAlignment(SwingConstants.BOTTOM);
		btnQuit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // exit when the quit button is pressed
			}
			
		});
		pnlQuit.add(btnQuit);
		btnQuit.setMaximumSize(new Dimension(70, 20));
		btnQuit.setPreferredSize(new Dimension(70, 20));
		
		pnlStats = new JPanel();
		FlowLayout fl_pnlStats = (FlowLayout) pnlStats.getLayout();
		fl_pnlStats.setAlignment(FlowLayout.LEFT);
		pnlStats.setBackground(Color.RED);
		dash.add(pnlStats, BorderLayout.CENTER);
		
		lblPoints = new JLabel("Points: 0");
		lblPoints.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlStats.add(lblPoints);
		
		lblLives = new JLabel("Lives: 2");
		lblLives.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlStats.add(lblLives);
		
		lblIgnore = new JLabel("Ignore turns: 0");
		pnlStats.add(lblIgnore);
		
		legend = new JPanel();
		getContentPane().add(legend, BorderLayout.WEST);
		legend.setMinimumSize(new Dimension(MAX_WIDTH*2/8, MAX_HEIGHT));
		legend.setPreferredSize(new Dimension(MAX_WIDTH*2/8, MAX_HEIGHT));
		legend.setBackground(Color.magenta);
		
		
		// Setting the mushroom images
		lblM1 = new JLabel("Wood Mushroom");
		lblM1.setIcon(new ImageIcon("m1.png"));
		legend.add(lblM1);
		
		lblM2 = new JLabel("Bad Mushroom");
		lblM2.setIcon(new ImageIcon("m2.png"));
		legend.add(lblM2);
		
		lblM3 = new JLabel("Good Mushroom");
		lblM3.setIcon(new ImageIcon("m3.png"));
		legend.add(lblM3);
		
		lblM4 = new JLabel("Wild Mushroom");
		lblM4.setIcon(new ImageIcon("m4.png"));
		legend.add(lblM4);
		
		// make the rendering timer
		mainT = new Timer(1000/20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.repaint();
			}
		});
		mainT.start(); // start it
		this.pack();
	}

	/**
	 * After the game is over
	 */
	public void afterGame() {
		mainT.stop(); // stop the rendering timer
		GameOverDialog game = new GameOverDialog(gc, MainFrame.this); // make a new GameOverDialog
		game.setVisible(true); // set the dialog box to be visible
	}
	
	/**
	 * A private helper class that extends JPanel, it deals with everything that goes on in game. It shows where the 
	 * mushrooms are on the field and the snake as well. The snake is controlled through the arrow keys
	 * @author JonathanMantel
	 *
	 */
	private class Canvas extends JPanel {
		
		/**
		 * Paint the entire screen
		 */
		@Override
		public void paint(Graphics g) {
			if(gc.gameOver()) { // if the game is over
				
				lblLives.setText("Lives: "+gc.getLives()); // show the number of lives
				
				afterGame(); // do the aftergame stuff	
				g.setColor(Color.WHITE); // after the box is closed, paint the canvas white
				g.fillRect(0, 0, this.getWidth(), this.getHeight()); 
				return;
			}
			
			this.setBackground(Color.BLUE); // background is normally blue
			lblPoints.setText("Points: "+gc.getPoints()); // update the points
			lblLives.setText("Lives: "+gc.getLives()); // update the lives
			lblIgnore.setText("Ignore turns: "+gc.getS().getIgnoreT()); // update the ignore turns
			gc.drawSnake(g); // draw the snake
			gc.drawMushroom(g); // draw the mushroom
		}
	}
	
	/**
	 * A private helper class. All this does is tell the snake to move when an arrow key is pressed.
	 * @author JonathanMantel
	 *
	 */
	private class KeyHelper implements KeyListener {
	
		@Override
		public void keyTyped(KeyEvent e) {}
		
		/**
		 * Turn the snake when the arrow key is pressed
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			gc.turnSnake(e.getKeyCode()); // turn the snake
		}

		@Override
		public void keyReleased(KeyEvent e) {}
		
	}
	
	/**
	 * Start everything off
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.setVisible(true); // set the mainframe to be visible
	}
}

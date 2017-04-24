package cs120.Snake.PresentationLayer;

import javax.swing.JDialog;

import cs120.Snake.Controller.GameController;

import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;

/**
 * This is the game over dialog class. When the game is ends, this box pops up and shows the top scores of the game.
 * It reads the file and then writes the score. If the user has a high score, then the score is added to the top scores
 * @author JonathanMantel
 *
 */
public class GameOverDialog extends JDialog {
	private GameController gc;
	private JTextField txtInitials; // a text field to enter initials
	private JTextField txtDate; // a text field to enter the date
	private JTextField txtScore; // a text field to enter the score
	private final int MAX_WIDTH = 600;
	private final int MAX_HEIGHT = 300;
	
	/**
	 * The constructor mainly handles how the dialog box looks. It reads in the file when opened.
	 * When the Save score button is pressed, it will write the score to the file, replacing the one below it.
	 * It passes through a mainframe and the game controller.
	 * @param gc
	 * @param m
	 */
	public GameOverDialog (GameController gc, MainFrame m) {
		super(m, "Game Over"); // call the super constructor
		this.gc = gc; // set this gc to the one passed through
		
		this.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
		
		JPanel pnlShowScores = new JPanel();
		getContentPane().add(pnlShowScores, BorderLayout.SOUTH);
		
		JButton btnShowScores = new JButton("Save Scores");
		btnShowScores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveScores(); // save the scores when pressed
			}
			
		});
		pnlShowScores.add(btnShowScores);
		
		JPanel enterScore = new JPanel();
		getContentPane().add(enterScore, BorderLayout.NORTH);
		
		txtInitials = new JTextField();
		txtInitials.setText("Enter Initials");
		enterScore.add(txtInitials);
		txtInitials.setColumns(10);
		
		txtDate = new JTextField();
		txtDate.setText("Enter Date");
		enterScore.add(txtDate);
		txtDate.setColumns(10);
		
		txtScore = new JTextField();
		txtScore.setEditable(false);
		txtScore.setEnabled(false);
		txtScore.setText(""+gc.getPoints());
		enterScore.add(txtScore);
		txtScore.setColumns(10);

		JPanel pnlScores = new JPanel();
		getContentPane().add(pnlScores, BorderLayout.CENTER);
		
		// a list to show all the scores
		DefaultListModel lm = new DefaultListModel();
		JList listScores = new JList(lm);
		pnlScores.add(listScores);
		
		// read all of the scores
		try {
			gc.setScores(gc.readScoresFromFile(new File("Scores.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO Exception");
		}

		for(String s: gc.getScores()) {
			lm.addElement(s);
		}
		listScores.setFocusable(false);
		
		this.setModal(true);
		this.pack();
	}

	/**
	 * A helper method to compare which score needs to be replaced
	 * @param readScore
	 * @param place
	 */
	public void compare(int readScore, int place) {
		int user = Integer.parseInt(txtScore.getText());
		
		if(user > readScore) {
			gc.getScores().add(place, place+" "+txtInitials.getText()+" "+txtDate.getText()+" :"+user+":");
			gc.getScores().removeLast();
			System.out.println("Score replaced");
		}
		
	}
	
	/**
	 * The save scores method. This method goes through every score and takes them out so that they can be read as integers
	 * and compared to the users score. Then the scores are written to the file
	 */
	public void saveScores() {
		for(int j = 0; j<gc.getScores().size(); j++) { // for every score in the list of scores
			String str = ""; // blank string
			int sepAt = 0; // a separator
			for(int i=0; i<gc.getScores().get(j).length(); i++) { // go through the string on the line
				if(gc.getScores().get(j).substring(i, i+1).equals(":")) { // if it starts with a :, thats where you start to record the
					// string
					str = gc.getScores().get(j).substring(sepAt+1, i+1); // make str the score
					sepAt = i; // mark the separator at i
				}
			}
			int n; // an integer n
			if(!str.isEmpty()&&str.endsWith(":")) { // if the string isn't empty and it ends with a :
				str = str.replaceAll(":", ""); // replace the : with nothing
				n = Integer.parseInt(str); // parse the str which should only be a score
				System.out.println(n);
				compare(n, j); // compare the user's score with the read score. j is the place that will be replaced by the users score
				// if it meets the requirements
			}
		}
		
		// Write to the file
		try {
			gc.writeScoresToFile(new File("Scores.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO Exception");
		}
	}
	
}

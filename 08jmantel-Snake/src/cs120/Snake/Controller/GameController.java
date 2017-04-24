package cs120.Snake.Controller;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import cs120.Snake.BackEnd.Mushrooms.Bad;
import cs120.Snake.BackEnd.Mushrooms.Good;
import cs120.Snake.BackEnd.Mushrooms.Mushroom;
import cs120.Snake.BackEnd.Mushrooms.Wild;
import cs120.Snake.BackEnd.Mushrooms.Wood;
import cs120.Snake.BackEnd.Snake.Segment;
import cs120.Snake.BackEnd.Snake.Snake;


/**
 * This is the game controller. It acts a a middle man between the presentation layer and the back end layer.
 * It has a constructor which makes a new snake for the game, handles the mushroom birth rate, the mushroom
 * image reading, and the speed at which both the snake and mushrooms get animated/placed.
 * This class also reads and writes scores from and to a score file.
 * @author JonathanMantel
 *
 */
public class GameController {
	private Snake s; // The snake for the game
	private int points; // the number of points the user has
	private int width, height; // the width and height of the window
	private int lives; // the number of lives the snake has
	private final int BIRTH_RATE; // the birth rate for mushrooms
	private int mTypes; // number of types of mushrooms
	private LinkedList<Mushroom> fieldM; // a list of mushrooms on the field
	private int frames; // regulates the speed of the snake movement
	private Image[] shroomPics; // an array to hold the mushroom pictures
	private Timer st; // Snake timer
	private Timer mt; // Mushroom birth timer
	private int stdDelay; // starting delay
	private LinkedList<String> scores;
	
	/**
	 * The constructor sets up all of the variables and reads in the mushroom images.
	 * It also starts a timer to animate the snake and a timer to decide when mushrooms should be placed
	 * on the field
	 * @param w
	 * @param h
	 */
	public GameController(int w, int h) {
		width = w; // the width of the field
		height = h; // the height of the field
		s = new Snake(width, height); // make a snake and pass through the height and width
		// of the window so the snake generates in the center of the world.
		frames = 15;
		
		scores = new LinkedList<String>(); // a list to hold the scores read in
		
		shroomPics = new Image[4];
		
		fieldM = new LinkedList<Mushroom>();
		BIRTH_RATE = 20; // 20% chance of mushroom popping up
		
		points = s.getParts(); // set the number of points to the number of segments
		
		lives = s.getParts(); // number of snake lives
		
		// For the mushrooms
		for(int i=1; i<5; i++) { // go through every mushroom
			try {
				shroomPics[i-1]=ImageIO.read(new File("m"+i+".png")); // read the image
			} catch (IOException e) { // if it fails
				System.out.println("Could not load image."); // the the system
			}
		}
		
		// make a new mushroom timer. activate it 2 times every second
		mt = new Timer(1000/2, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addShroom(); // add a mushroom
			}
			
		});
		
		stdDelay = 1000/12; // start the delay, it may change
		
		// make a snake timer for 12 ticks/sec
		st = new Timer(stdDelay, new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
					s.moveSnake();
					snakeMushroom();
			}
			
		});
		
		// start the timers
		mt.start();
		st.start();
	}
	
	public LinkedList<String> getScores() {
		return scores;
	}

	public void setScores(LinkedList<String> scores) {
		this.scores = scores;
	}

	public Timer getSt() {
		return st;
	}

	public void setSt(Timer st) {
		this.st = st;
	}

	public Timer getMt() {
		return mt;
	}

	public void setMt(Timer mt) {
		this.mt = mt;
	}

	public synchronized int getFrames() {
		return frames;
	}

	public synchronized void setFrames(int frames) {
		this.frames = frames;
	}

	public int getmTypes() {
		return mTypes;
	}

	public void setmTypes(int mTypes) {
		this.mTypes = mTypes;
	}

	public LinkedList<Mushroom> getFieldM() {
		return fieldM;
	}

	public void setFieldM(LinkedList<Mushroom> fieldM) {
		this.fieldM = fieldM;
	}

	public int getBIRTH_RATE() {
		return BIRTH_RATE;
	}

	public Snake getS() {
		return s;
	}

	public void setS(Snake s) {
		this.s = s;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * This method adds a random mushroom type to the field
	 */
	public synchronized void addShroom() {
		if(fieldM.size()<5) { // As long as there are less than 5 mushrooms on the field
			Random r = new Random(); // make a new random number generator
			if(r.nextInt(100)<BIRTH_RATE) { // if the number is  less than the birthrate
				r = new Random(); // make a new random number generator
				mTypes = r.nextInt(4); // generate a random number out of 4 possibilities
				switch(mTypes) { // the number corresponds to the mushroom type
				case 0: // add the bad mushroom
					fieldM.add(new Bad(width, height));
					break;
				case 1: // add the good mushroom
					fieldM.add(new Good(width, height));
					break;
				case 2: // add the wild mushroom
					fieldM.add(new Wild(width, height));
					break;
				case 3: // add the wood mushroom
					fieldM.add(new Wood(width, height));
					break;
				}
			}
		}
		// if the field is not empty
		if(!fieldM.isEmpty()) {
			// lower the life of the first mushroom by 1. if it has run out of life, destroy it.
			if(fieldM.getFirst().despawn()) fieldM.removeFirst();
		}
	}
	
	/**
	 * This method deals with checking if the snake collides with a mushroom
	 * and if it does tells the mushroom to be consumed
	 */
	public synchronized void snakeMushroom() {
		for(int i=0; i<fieldM.size(); i++) { // go through every mushroom in the list
			// if the snake overlaps the mushroom, then consume the mushroom
			if(fieldM.get(i).contains(s.getH())) fieldM.get(i).whenConsumed(this, s);
		}
	}
	
	/**
	 * This method deals with turning the snake when a key is pressed
	 * @param key
	 */
	public synchronized void turnSnake(int key) {
		if(!s.ignore()) { // if the snake didn't eat an ignore mushroom
			switch(key) { // which key was pressed
			case 37:
				if(s.getH().getDirX()!=1) { // if the direction isn't complete opposite to it's current direction
					s.getH().setDirY(0); // change the directions so the snake moves horizontally
					s.getH().setDirX(-1); // westward
				}
				break;
			case 38:
				if(s.getH().getDirY()!=1) { // if the direction isn't complete opposite to it's current direction
					s.getH().setDirX(0); // move vertically
					s.getH().setDirY(-1); // north
				}
				break;
			case 39:
				if(s.getH().getDirX()!=-1) { // if the direction isn't complete opposite to it's current direction
					s.getH().setDirY(0); // move horizontally
					s.getH().setDirX(1); // east
				}
				break;
			case 40:
				if(s.getH().getDirY()!=-1) { // if the direction isn't complete opposite to it's current direction
					s.getH().setDirX(0); // move horizontally 
					s.getH().setDirY(1); // south
				}
				break;
			}
		} else s.subIgnore(); // otherwise, subtract 1 from the ignore
	}
	
	/**
	 * This is a method to add points and a life to the snakes amount of lives. This will only be called
	 * whenever the snake runs into a mushroom that adds a segment. If it runs into any other
	 * type of mushroom, this will not be called.
	 */
	public void addPointsLife() {
		s.addSeg(); // add a segment to the snake
		this.points++; // add a point
		if(this.points%4==0) lives++; // for every 4 points, add a new snake life (except the first life will be after 2 points)
	}
	
	/**
	 * If the snake gets damaged, then check to see in what way it got damaged and kill it
	 */
	public void snakeDamage() {
		if((s.getH().x > width) // exited the screen on the right side
				||(s.getH().x < 0) // on the left side
				||(s.getH().y > height) // on the bottom
				||(s.getH().y < 0) // at the top
				||(s.collideSnake(s.getH()))) { // crashed into itself
			s.killSnake(s.getTail()); // regardless it died
			newSnake();
		}
	}
	
	/**
	 * If it's game over return a boolean that says true
	 * @return
	 */
	public boolean gameOver() {
		if(this.getLives()<=0) { // if there are no lives left
			
			this.setLives(0); // set the lives to 0
			
			return true; // yes game over
		}
		return false; // no game on
	}
	
	/**
	 * Make a new snake because the old one died
	 */
	public Snake newSnake() {
		if(!(s==null)) s.killSnake(s.getTail()); // if the snake exists, kill it
		if(lives>0) { // if the player doesn't have a negative amount of lives
			this.lives--; // subtract 1 from the number of lives
			s = new Snake(width, height); // a new snake can be made
			st.setDelay(1000/15); // reset the delay
			return s; // return the new snake
		} else { // if the player has run out of lives
			s.killSnake(s.getTail()); // kill the snake
			st.stop(); // stop the snake timer
			mt.stop(); // stop the mushroom timer
			return null; // return no snake
		}
	}
	
	/**
	 * This is the method for drawing each segment of the snake.
	 * @param g
	 */
	public void drawSnake(Graphics g) {
		Segment temp = s.getTail(); // set a temp segment to the snake's tail
		g.setColor(Color.green); // green snake
		
		while(temp!=null) { // while the temp isn't a null segment
			g.fillOval((int)temp.getX(), (int)temp.getY(), (int)temp.getWidth(), (int)temp.getHeight()); // fill an oval
			temp = temp.getSeg(); // move onto the next segment
		}
		this.snakeDamage(); // check to see if the snake did anything to kill itself
	}
	
	/**
	 * Draw the mushrooms
	 * @param g
	 */
	public void drawMushroom(Graphics g) {
		if(!fieldM.isEmpty()) { // if the list of mushrooms isn't empty
			for(Mushroom m: fieldM) m.drawShroom(g, this); // draw every mushroom
		}
	}
	
	public Image[] getShroomPics() {
		return shroomPics;
	}

	public void setShroomPics(Image[] shroomPics) {
		this.shroomPics = shroomPics;
	}
	
	/**
	 * This method reads the scores from the score file. It returns a Linked List of the scores.
	 * @param f
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public LinkedList<String> readScoresFromFile(File f) throws IOException, FileNotFoundException {
		BufferedReader br = null;
		LinkedList<String> tempScores = new LinkedList<String>();
		try {
			FileReader fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			String str = br.readLine();
			
			for(int i=0; i<11; i++) {
				// process data in str
				tempScores.add(str); // add to the scores linked list what was read
				str = br.readLine(); // next line
				if(str==null) break; // if it's done reading, break
			}
		}
		catch (IOException ex) {
			// handle exception here
			System.err.println("trouble with file :"+ex.getMessage());
		}

		finally {
			/*
			 ** attempt to close the file
			 */
			try {
				if (br != null) {
					br.close();
				}
			}
			catch (Exception ex1) {
				ex1.printStackTrace(); // give up
				System.exit(-1);
			}
		}
		return tempScores; // return the scores
	}
	
	/**
	 * This method writes the scores to the file. it takes in a file as a parameter and
	 * writes whatever is in the scores linked list to the file.
	 * @param f
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeScoresToFile(File f) throws IOException, FileNotFoundException {
		PrintWriter pw = null;
		
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			
			for(String s: scores) { // for every score
				String str = s.toString(); // get the toString
				pw.println(str); // write that toString
			}
			
		} catch(Exception e) {
			System.out.println("Could not write");
		}
		
		finally {
			/*
			 ** attempt to close the file
			 */
			try {
				if (pw != null) {
					pw.close();;
				}
			}
			catch (Exception ex1) {
				ex1.printStackTrace(); // give up
				System.exit(-1);
			}
		}
	}
}

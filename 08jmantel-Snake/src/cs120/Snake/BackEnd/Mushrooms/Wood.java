package cs120.Snake.BackEnd.Mushrooms;

import java.awt.Graphics;

import cs120.Snake.BackEnd.Snake.Snake;
import cs120.Snake.Controller.GameController;

/**
 * This is the wood mushroom class, it inherits the constructor and methods from the mushroom class
 * and it prevents user input from being read for a certain number of turns
 * @author JonathanMantel
 *
 */
public class Wood extends Mushroom {
	private int image; // what is the image number to use?
	
	/**
	 * Use the constructor from the mushroom class
	 * @param w
	 * @param h
	 */
	public Wood(int w, int h) {
		super(w, h);
		image = 0;
	}

	/**
	 * When consumed, this mushroom tells the game to ignore user input 3 times
	 */
	public synchronized void whenConsumed(GameController gc, Snake s) {
		s.setIgnoreT(3); // set the ignore number to be 3
		gc.getFieldM().remove(this); // remove the image from the field
	}
	
	/**
	 * Draw the mushroom
	 */
	public void drawShroom(Graphics g, GameController gc) {
		g.drawImage(gc.getShroomPics()[image], this.x, this.y, null); // draw the image at the x and y designaed
	}
}

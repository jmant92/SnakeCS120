package cs120.Snake.BackEnd.Mushrooms;

import java.awt.Graphics;

import cs120.Snake.BackEnd.Snake.Snake;
import cs120.Snake.Controller.GameController;

/**
 * This is the good mushroom class, it inherits the constructor and methods from the mushroom class
 * and it adds a segment to the snake when consumed
 * @author JonathanMantel
 *
 */
public class Good extends Mushroom {
	private int image; // what is the image number to use?
	
	/**
	 * Get the mushroom constructor from the mushroom class
	 * @param w
	 * @param h
	 */
	public Good(int w, int h) {
		super(w, h);
		image = 2;
	}

	/**
	 * The whenConsumed method decides what to do when the snake eats a mushroom.
	 * The good mushroom adds 1 point, 1 segment, and possibly one life. Then the mushroom
	 * is removed from the field
	 */
	public synchronized void whenConsumed(GameController gc, Snake s) {
		gc.addPointsLife(); // add a new segment and a point
		gc.getFieldM().remove(this); // remove the mushroom from the field
	}
	
	/**
	 * Draw the mushroom
	 */
	public void drawShroom(Graphics g, GameController gc) {
		g.drawImage(gc.getShroomPics()[image], this.x, this.y, null); // draw the image at the x and y designaed
	}
}

package cs120.Snake.BackEnd.Mushrooms;

import java.awt.Graphics;

import cs120.Snake.BackEnd.Snake.Snake;
import cs120.Snake.Controller.GameController;

/**
 * This is the bad mushroom class, it inherits the constructor and methods from the mushroom class
 * and it kills the snake when consumed
 * @author JonathanMantel
 *
 */
public class Bad extends Mushroom {
	private int image; // what is the image number to use?
	
	/**
	 * Use the constructor from the Mushroom class
	 * @param w
	 * @param h
	 */
	public Bad(int w, int h) {
		super(w, h);
		image = 1;
	}
	
	/**
	 * When consumed the mushroom kills the snake
	 */
	@Override
	public synchronized void whenConsumed(GameController gc, Snake s) {
		s.killSnake(s.getTail()); // kill the snake starting from the tail
		gc.newSnake(); // make a new snake at the center of the world
		gc.getFieldM().remove(this); // remove the mushroom image
	}
	
	/**
	 * Draw the mushroom
	 */
	public void drawShroom(Graphics g, GameController gc) {
		g.drawImage(gc.getShroomPics()[image], this.x, this.y, null); // draw the image at the x and y designaed
	}
}

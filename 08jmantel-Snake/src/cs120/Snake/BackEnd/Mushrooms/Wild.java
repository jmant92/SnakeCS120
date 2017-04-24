package cs120.Snake.BackEnd.Mushrooms;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import cs120.Snake.BackEnd.Snake.Snake;
import cs120.Snake.Controller.GameController;

/**
 * This is the wild mushroom class, it inherits the constructor and methods from the mushroom class
 * and it speeds up the snake when consumed
 * @author JonathanMantel
 *
 */
public class Wild extends Mushroom {
	private int image; // what is the image number to use?
	
	/**
	 * Get the mushroom constructor from the mushroom class
	 * @param w
	 * @param h
	 */
	public Wild(int w, int h) {
		super(w, h);
		image = 3;
	}

	/**
	 * The whenConsumed method decides what to do when the snake eats a mushroom.
	 * The wild mushroom increases the speed of the snake.
	 */
	public synchronized void whenConsumed(GameController gc, Snake s) {
		gc.setFrames(gc.getFrames()+5); // increase the number of frames by 2
		gc.getSt().setDelay(1000/gc.getFrames()); // change the delay
		gc.getFieldM().remove(this); // remove the mushroom image from the field
	}
	
	/**
	 * Draw the mushroom
	 */
	public void drawShroom(Graphics g, GameController gc) {
		g.drawImage(gc.getShroomPics()[image], this.x, this.y, null); // draw the image at the x and y designaed
	}
}

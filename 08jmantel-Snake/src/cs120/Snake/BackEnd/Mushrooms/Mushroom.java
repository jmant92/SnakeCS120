package cs120.Snake.BackEnd.Mushrooms;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import cs120.Snake.BackEnd.Snake.Snake;
import cs120.Snake.Controller.GameController;

/**
 * This is the mushroom class. It is an abstract class because no class will be called that 
 * purely be a mushroom. It inherits from the rectangle class.
 * It has a constructor that determines the dimensions of the mushroom hitbox, generates
 * a random number generator to determine the x and y coordinates
 * and gives each mushroom a life of 20.
 * @author JonathanMantel
 *
 */
public abstract class Mushroom extends Rectangle{
	private int life; // so the mushrooms will despawn eventually
	
	/**
	 * Construct the important variables such as position of the mushroom. Height and width
	 * randomly place the mushroom. Set the amount of life the mushroom has
	 * @param w
	 * @param h
	 */
	public Mushroom(int w, int h) {
		this.width = 32; // width of the mushroom hitbox
		this.height = this.width; // height of the mushroom hitbox
		
		Random r = new Random(); // make a new random number generator
		
		int n = r.nextInt(w-32); // 
		this.x = n;
		n = r.nextInt(h-32);
		this.y = n;
		
		life = 20;
	}
	
	/**
	 * Handles what happens when a mushroom is eaten. Changes depending on the mushroom
	 * @param gc
	 * @param s
	 */
	public synchronized void whenConsumed(GameController gc, Snake s) {}
	
	/**
	 * A method to determine when to automatically take out the mushroom from the field.
	 * It's here so that the field doesn't become saturated with deadly mushrooms. If the mushroom
	 * life has reached 0, then return true. Otherwise return false.
	 * @return
	 */
	public boolean despawn() {
		life--; // whenever the method is called, subtract 1 from the life
		
		if(life<=0) return true; // if the life has run out, return true
		else return false; // otherwise return false
		
	}
	
	public void drawShroom(Graphics g, GameController gc) {}
}

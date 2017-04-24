package cs120.Snake.BackEnd.Snake;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * Here is the Segment class. It extends Rectangle so that the segment can know if it's overlapping
 * something. It has a handle on the segment in front of it as well as a handle on the direction
 * it's going.
 * 
 * It has a move method which deals with moving the segment in a particular direction, dir.
 * @author JonathanMantel
 *
 */
public class Segment extends Rectangle {
	private Segment seg; // a handle on the segment in front
	private int dirX, dirY; // X and Y direction of the snake movement
	
	/**
	 * Initialize the variables. Get a handle on the segment directly in front
	 * Start at the center of the screen.
	 * Set the initial direction to be up.
	 * @param s
	 */
	public Segment(Segment s) {
		seg = s; // a handle on the segment in front

		try{
			this.dirX = seg.getDirX();
			this.dirY = seg.getDirY();
		} catch(Exception e) {
			// Have the head start game going North
			dirX = 0;
			dirY = -1;
		}
		
		// Segment Dimensions
		this.width = 16; // set the segment's initial width
		this.height = this.width; // set the segment's initial height to be the same
		// offset
		this.x -= this.width/2;
		this.y -= this.height/2;
	}
	
	// Getters and Setters
	public int getDirX() {
		return dirX;
	}

	public void setDirX(int dirX) {
		this.dirX = dirX;
	}

	public int getDirY() {
		return dirY;
	}

	public void setDirY(int dirY) {
		this.dirY = dirY;
	}

	public Segment getSeg() {
		return seg;
	}

	public void setSeg(Segment seg) {
		this.seg = seg;
	}
	
	/**
	 * This method is here so that the segment doesn't end up on top of the segment ahead of it
	 */
	public void place() {
		
		// For a segments X placement
		this.x = this.getSeg().x - this.getSeg().getDirX()*(this.width); // place the segment half a width+1 behind the segment
		// it has a handle on. That way it isn't overlapping
		
		// for a segments Y placement
		this.y = this.getSeg().y - this.getSeg().getDirY()*(this.height); // do the same here, but for the Y direction
	}
	
	/**
	 * This method is for moving the snake
	 */
	public void move() {
		try { // try catch so the head doesn't crash the game with a null pointer exception
			this.x = (int)this.seg.getX(); // move to the segment ahead's x position
			this.y = (int)this.seg.getY(); // move to the segment ahead's y position
			// change the direction to the segement ahead's direction
			this.dirX = seg.dirX;
			this.dirY = seg.dirY;
		} catch (Exception e) {
			// move the snake its width and in its direction
			this.x += dirX*width;
			this.y += dirY*height;
		}
	}
 }

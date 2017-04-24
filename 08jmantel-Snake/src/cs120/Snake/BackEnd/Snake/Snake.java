package cs120.Snake.BackEnd.Snake;

/**
 * This is the Snake class. It holds the head of the snake as well as the tail or last segment
 * in the snake. It handles adding in a new segment, 
 * @author JonathanMantel
 *
 */
public class Snake {
	private Segment tail; // a handle on the last segment
	private Segment h; // a handle on the head
	private int parts; // number of snake segments
	private int ignoreT; // a counter to ignore turns
	
	/**
	 * Initialize the snake by creating the head and the first body part.
	 * w and l are the width and length of the field. They are passed through so that
	 * the snake knows where to start based on the window size.
	 * @param w
	 * @param l
	 */
	public Snake(int width, int height) {
		h = new Segment(null); // head segment, handle on null
		// Place the head at the center of the world
		h.x=width/2;
		h.y=height/2;
		
		tail = new Segment(h); // second body segment, handle on head
		tail.place(); // place it behind the head
		
		parts=2; // two parts to start with
	}

	public Segment getTail() {
		return tail;
	}

	public void setTail(Segment tail) {
		this.tail = tail;
	}

	public Segment getH() {
		return h;
	}

	public void setH(Segment h) {
		this.h = h;
	}

	public int getParts() {
		return parts;
	}

	public void setParts(int parts) {
		this.parts = parts;
	}
	
	public int getIgnoreT() {
		return ignoreT;
	}

	public synchronized void setIgnoreT(int ignoreT) {
		this.ignoreT = ignoreT;
	}

	/**
	 * This method is for adding a new segment to the snake. This segment becomes
	 * the new tail and has a handle on the previous tail. It places the tail in the
	 * correct place and adds 1 to the number of snake parts.
	 */
	public synchronized void addSeg() {
		tail = new Segment(tail); // make a new segment that has a handle on the old tail
		// make that the new tail
		
		tail.place(); // place the segment so it isn't overlapping the segment it has a handle on
		
		parts++; // add 1 to the parts
	}
	
	/**
	 * This method recursively deletes the snake segments starting from the back.
	 * It takes in the tail segment and returns the new tail segment.
	 * @param end
	 * @return
	 */
	public Segment killSnake(Segment end) {
		if(tail==null) return null; // if the tail is null, exit the recursion
		else { // otherwise
			tail = tail.getSeg(); // set the tail to be the segment that the tail currently has 
			// a handle on.
			parts--; // subtract the number of parts
			return this.killSnake(tail); // call this method again using the new tail
		}
	}
	
	/**
	 * A simple method to tell if the snake is dead or not. More of a helper method to
	 * let the game know if the snake needs to do actions
	 * @return
	 */
	public boolean isDead() {
		if(parts==0) return true; // if no parts left, the snake is dead
		return false; // otherwise it's still alive
	}
	
	/**
	 * This method moves the snake every time the timer to move it ticks. It moves each segment
	 * individually, starting from the head and ending at the tail.
	 * @throws Exception 
	 */
	public synchronized void moveSnake() {
		if(!this.isDead()) { // as long as the snake is alive
			Segment temp = tail; // set a temp segment to have a handle on the head
			
			while(temp!=null) { // while temp is not null
				temp.move(); // move temp
				temp = temp.getSeg(); // make temp the segment temp has a handle on (i.e. the segment in front of it)
			}
			
		}
	}
	
	/**
	 * A simple method for ignoring key strokes. When the snake runs into a certain mushroom type
	 * it will ignore ignoreT turns. Every time the turn button is pressed, this method will be
	 * called. When it is called it will subtract 1 from ignoreT. If the number of ignoreT left
	 * is 0, then return false so the game knows to let the user continue to move. If ignoreT
	 * isn't 0 then return true so the game knows to prevent the user from controlling the snake
	 * @return
	 */
	public boolean ignore() {
		if(ignoreT<=0) return false; // if ignoreT is 0, the user gets control again
		else return true; // otherwise the user doesn't have control
	}
	
	/**
	 * Lessen the number of ignored turns
	 */
	public void subIgnore() {
		ignoreT--; // subtract 1 from ignoreT
	}
	
	/**
	 * A method to see if the snake collided with itself. it takes in a segment (the head)
	 * and checks to see if it collided with something. Return whether or not it did.
	 * @param seg
	 * @return
	 */
	public boolean collideSnake(Segment seg) {
		Segment segCheck = tail; // make some segment have a handle on the supposed last segment
		try{
			while(segCheck.getSeg()!=null) { // while the segment in question is not the head
				if(seg.intersects(segCheck)&&(!segCheck.intersects(segCheck.getSeg()))) return true; // if the segment passed through collides
				// with seg check, and it isn't the segment ahead of it, return Yes! it has collided
				else segCheck=segCheck.getSeg(); // otherwise go to the next segment in the chain
			}
		} catch(Exception e) {} // this try catch is here so the game won't crash when it checks the head.
		// Since the head has a handle on null, it'll give a null pointer exception when checking the handle of the head's segment
		
		return false; // if it hasn't collided, then all is well and return false
	}
	
	// Methods used specifically for testing
	/**
	 * This is a test to see if the new segment was correctly added.
	 * It is a method used for testing. It returns true if the add and delete segment methods
	 * are working.
	 * @return
	 */
	public boolean checkTail() {
		Segment segCheck = tail; // make some segment have a handle on the supposed last segment
		int piece = 0; // 0 segments so far
		
		while(segCheck!=null) { // while the segment in question is not the head
			segCheck = segCheck.getSeg(); // set the test segment to be the one the current segment
			// has a handle on
			piece++; // add one to the number of segments
		}
		
		/*
		 * This works because it will get to the head, but won't check to see what segment
		 * it has a handle on until it's inside the while loop. By the time it goes around to the
		 * null segment, it has already added in the last segment of the snake.
		 */
		
		return (piece==parts); // return true if the pieces counted is the same as the number of parts
	}
}

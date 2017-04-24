package cs120.Snake.BackEnd.Snake;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

/**
 * This is the snake test class. It has a test to check the initialization of the snake.
 * It tests the snake death, and the snake movement
 * @author JonathanMantel
 *
 */
public class SnakeTests {
	
	/**
	 * This set of tests checks to see if the snake initializes properly and the segments
	 * are added correctly.
	 */
	@Test
	public void snakeInitAndAddSegTests() {
		Snake s = new Snake(800, 800);
		
		s.addSeg(); // add a segment
		s.addSeg(); // add a segment
		
		assertTrue(s.checkTail()); // check to see if the number of segments is correct
	}
	
	/**
	 * This test checks to see if the snake is deleted properly
	 */
	@Test
	public void snakeKillTest() {
		Snake s = new Snake(800, 800);
		
		s.addSeg(); // add a segment
		s.addSeg(); // add a segment
		
		s.killSnake(s.getTail()); // kill the snake
		
		assertTrue(s.checkTail()); // check to see if there are no snake parts left
	}

	/**
	 * This test checks to see if the snake moves correctly.
	 */
	@Test
	public void snakeMoveTest() {
		Snake s = new Snake(800, 800); // generate a new snake
		
		assertTrue(s.getH().y == 400);
		int ypos = s.getTail().getSeg().y;
		
		assertTrue(s.getTail().y == ypos+s.getTail().height);
		
		s.moveSnake();

		assertTrue(s.getH().x==400); // the head should not have moved to the side
		assertTrue(s.getTail().y == 400); // the snake moved up

	}
}

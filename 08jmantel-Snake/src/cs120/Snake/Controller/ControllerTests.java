package cs120.Snake.Controller;

import static org.junit.Assert.*;

import org.junit.Test;

import cs120.Snake.BackEnd.Snake.Snake;

/**
 * This is the JUnit test case to test the game controller class. It tests:
 * - Initialization - loading up images
 * - Add points/lives
 * - Make a new snake
 * @author JonathanMantel
 *
 */
public class ControllerTests {

	/**
	 * A simple init test to check if the game controller initializes properly
	 */
	@Test
	public void initTest() {
		GameController gc = new GameController(800, 800);
	}
	
	/**
	 * A set of tests to check if the new snake and add points/lives algorithms work
	 */
	@Test
	public void newSnakePointsLivesTest() {
		GameController gc = new GameController(800, 800);
		Snake s = gc.getS();
		
		assertTrue(gc.getLives()==2); // start with 2 lives
		assertTrue(gc.getPoints()==2); // and 2 points
		gc.addPointsLife(); // add points and maybe a life
		assertTrue(s.getParts()==3); // new part!
		assertTrue(gc.getPoints()==3); // add 1 point
		assertTrue(gc.getLives()==2); // no life added
		gc.addPointsLife(); // add another point and maybe life
		assertTrue(s.getParts()==4); // new part!
		assertTrue(gc.getPoints()==4); // add 1 point
		assertTrue(gc.getLives()==3); // life added
		s = gc.newSnake(); // kill the old snake and make a new one
		assertTrue(gc.getPoints()==4); // points remain the same
		assertTrue(gc.getLives()==2); // lose a life
		s.killSnake(s.getTail()); // kill the snake, then make a new one
		assertTrue(s.getParts()==0);
		s = gc.newSnake(); // make sure it doesn't give a null pointer exception
		assertTrue(gc.getLives()==1); // lose a life
		assertTrue(s.getParts()==2);
	}

}

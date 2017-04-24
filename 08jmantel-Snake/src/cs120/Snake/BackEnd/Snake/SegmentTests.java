package cs120.Snake.BackEnd.Snake;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * A set of tests designed to check if the methods of the Segment class work properly
 * @author JonathanMantel
 *
 */
public class SegmentTests {
	
	/**
	 * A test for the segment initialization
	 */
	@Test
	public void InitializeTests() {
		Segment h = new Segment(null);
		Segment s = new Segment(h);
		
		assertTrue(s.getSeg()==h);
	}

}

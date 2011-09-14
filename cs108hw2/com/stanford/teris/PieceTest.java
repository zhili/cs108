package com.stanford.teris;
import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		

	}
	
	public void testRoration() {
		assertTrue(pyr2.equals(new Piece("1 0	1 1	 0 1  1 2")));
		assertTrue(pyr3.equals(new Piece("2 1	1 1	 1 0  0 1")));
		assertTrue(pyr4.equals(new Piece("0 2	0 1	 1 1  0 0")));
		
		Piece stick_0 = new Piece(Piece.STICK_STR);
		assertEquals(4, stick_0.getHeight());
		Piece stick_1 = stick_0.computeNextRotation();
		assertEquals(1, stick_1.getHeight());
	}
	
	
	public void testFastRotation() {
		Piece[] pieces = Piece.getPieces();
        Piece square = pieces[Piece.SQUARE];
        assertEquals(square, square.fastRotation());
        assertEquals(square, square.fastRotation().fastRotation());
        
        // L1/L2
        Piece pyra = pieces[Piece.PYRAMID];
        assertEquals(pyra, pyra.fastRotation().fastRotation().fastRotation().fastRotation());
		Piece stick = pieces[Piece.STICK];
		assertEquals(4, stick.getHeight());
		Piece stickR1 = stick.fastRotation();
		assertEquals(1, stickR1.getHeight());
		Piece stickR2 = stickR1.fastRotation();
		assertEquals(4, stickR2.getHeight());
	}
	
	// Here are some sample tests to get you started
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		assertEquals(3, s.getWidth());
		assertEquals(2, s.getHeight());
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
//		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}
	
	
}

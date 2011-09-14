package com.stanford.teris;
// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	
	private boolean[][] grid_backup;
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		grid_backup = new boolean[width][height];
		// YOUR CODE HERE
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		int maxHeight = 0;
		for (int i = 0; i < width; i++) {
			if (getColumnHeight(i) > maxHeight)
				maxHeight = getColumnHeight(i);
		}
		return maxHeight; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
			if (grid.length != width || grid[0].length != height)
				throw new RuntimeException("bad grid");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int result = 0;
		int origY = getColumnHeight(x) + skirt[0];
		int maxStep = 0;
		int maxColumnHeightWithinSkirt = getColumnHeight(x);
		for (int i = 1; i < skirt.length; i++) {
			int step = skirt[0] - skirt[i];
			if (Math.abs(maxStep) < Math.abs(step))
				maxStep = step;
			if (maxColumnHeightWithinSkirt < getColumnHeight(x + i))
				maxColumnHeightWithinSkirt = getColumnHeight(x + i);
		}
		if (maxStep > 0)
			result = origY - maxStep;
		else if(maxStep < 0)
			result = getColumnHeight(x);
		else
			result = maxColumnHeightWithinSkirt;
		return result; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		int maxY = 0;
		for (int j =0; j < height; j++) {
			if (grid[x][j])
				 maxY = 1 + j;
		}
		return maxY; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		int count = 0;
		for (int j =0; j < width; j++) {
			if (grid[j][y])
				count += 1;
		}
		return count; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		boolean result = false;
		try {
			result = grid[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			result = true;
		}
		return result; // YOUR CODE HERE
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
			
		int result = PLACE_OK;
		
		committed = false;
		// YOUR CODE HERE
		// backup grid status.
		for (int i = 0; i < grid.length; i++) 
			System.arraycopy(grid[i], 0, grid_backup[i], 0, grid[i].length);
		
		for (TPoint point : piece.getBody()) {
			int placeToPutAtX = point.x + x;
			int placeToPutAtY = point.y + y;
			if (x < 0 || y < 0 ||placeToPutAtX > width-1 || placeToPutAtY > height-1) {
				result = PLACE_OUT_BOUNDS;
				//undo();
				break;
			} else if (grid[placeToPutAtX][placeToPutAtY]) {
				result = PLACE_BAD;
				//undo();
				break;
			} else {
				grid[placeToPutAtX][placeToPutAtY] = true;
				if (width == getRowWidth(placeToPutAtY))
					result = PLACE_ROW_FILLED;
			}
		}
		
		sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		// YOUR CODE HERE
		int maxHeight = getMaxHeight();
		
		// according to assignment doc, normally, the clear row comes after place.
		if (committed)
			// if clearRows not invoke after place, we should do backup here.
			for (int i = 0; i < grid.length; i++) 
				System.arraycopy(grid[i], 0, grid_backup[i], 0, grid[i].length);
		committed = false;
		for (int toIndex = 0; toIndex <= maxHeight; toIndex++) {
			if (width == getRowWidth(toIndex)) {
				// full-filled
				int fromIndex = toIndex + 1;
				while (fromIndex <= maxHeight && width == getRowWidth(fromIndex))
					fromIndex++;
				// now do copy
				for (int i = 0; i < width; i++) {
					grid[i][toIndex] = grid[i][fromIndex];
					//  make the 'from' row empty
					grid[i][fromIndex] = false;
				}
				// shift the 'from' row up.
				int ceil = maxHeight-rowsCleared;
				while (fromIndex < ceil) {
					for (int i=0; i < width; i++) {
						grid[i][fromIndex] = grid[i][fromIndex+1];
						grid[i][fromIndex+1] = false;
					}
					fromIndex++;
				}
				rowsCleared++;
			}
		}

		sanityCheck();
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
		// swap to restore status.
		if (!committed) {
			boolean[][] temp = grid;
			grid = grid_backup;
			grid_backup = temp;
			committed = true;
		}
		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}



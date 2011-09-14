//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	
	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int Y = grid[0].length;
		int X = grid.length;
		//System.out.print(X);
		
		for (int j = 0; j < Y; ++j) {
			boolean shouldClear = true;
			for (int i = 0; i < X; i++) {
				shouldClear = shouldClear & grid[i][j];
				//System.out.print(grid[i][j]);
			}
			//System.out.println();
			if (shouldClear) {
				//System.out.println("shitfing");
				// shift rows down
				int next = j + 1;
				while (next < Y) {
					for (int i = 0; i < X; ++i)
						grid[i][j] = grid[i][next];
					next++;
				}
				// empty top.
				for (int i = 0; i < X; ++i)
					grid[i][next-1] = false;
			}
		}

		
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
/*		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) 
				System.out.print(grid[i][j]);
			System.out.println();
		}*/
		
		return grid; 
	}
}

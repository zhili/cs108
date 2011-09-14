// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		
		int X = grid.length;
		int Y = grid[0].length;
		int maxX = 0;
		int maxY = 0;
		int minX = 1000;
		int minY = 1000;
		boolean found = false;
		for (int i = 0; i < X; i++)
			for (int j = 0; j < Y; j++)
				if (grid[i][j] == ch) {
					found = true;
					if (i > maxX)
						maxX = i;
					if (j > maxY)
						maxY = j;
					if (i < minX)
						minX = i;
					if (j < minY)
						minY = j;
				}
		if (found)
			return (maxX - minX + 1)*(maxY-minY + 1);
		return 0;
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int count = 0;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (plusSurround(i, j, grid[i][j]))
					count +=1;
		return count;  
	}
	
	private boolean plusSurround(int i, int j, char ch) {
		if ((i-1) >= 0 && (i+1) < grid.length && (j-1) >= 0 && (j+1) < grid[0].length)
			if (grid[i-1][j] == ch && grid[i+1][j] == ch && grid[i][j-1] == ch && grid[i][j+1] == ch)
				return true;
		
		return false;
	}
}

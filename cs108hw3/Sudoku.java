import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	
	private class Spot implements Comparable<Spot> {
		private int r, c;
		
		public Spot(int x, int y) {
			r = x;
			c = y;
		}
		
		public void setValue(int v) {
			grid[r][c] = v;
		}
		
//		public int getValue() {
//			return grid[r][c];
//		}
		
		public boolean isLegalVale(int number) {
			int squareIndex = (r / PART) * PART  + c / PART;
			if (squares.get(squareIndex).contains(new Integer(number)))
				return false;
			for (int x = 0; x < SIZE; x++) {
				if (grid[r][x] == number || grid[x][c] == number)
					return false;
			}
			return true;
		}
		
		public List<Integer> validNumbers() {
			ArrayList<Integer> validNumbersList = new ArrayList<Integer>();
			for (int i = 1; i <= SIZE; i++) 
				if (isLegalVale(i))
					validNumbersList.add(new Integer(i));
			return validNumbersList;
		}
		
		public int compareTo(Spot other) {
			// compare by valid number size
			int thisSize = this.validNumbers().size();
			int otherSize = other.validNumbers().size();
			if (thisSize < otherSize)
				return -1;
			if (thisSize > otherSize)
				return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "(r=" + r + ", c=" + c + ", size="+ validNumbers().size() + ")";
		}
	
	}
	
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	private int[][] grid;
	private ArrayList<Spot> spots;
	private ArrayList<HashSet<Integer>> squares;
	private long startTime;
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	private int solutionCount;
	private ArrayList<String> solutionList;
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());

	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		// YOUR CODE HERE
		grid = new int[ints.length][];
		spots = new ArrayList<Spot>();
		squares = new ArrayList<HashSet<Integer>>(SIZE);
		for (int i = 0; i < SIZE; i++)
			squares.add(new HashSet<Integer>());
		solutionList = new ArrayList<String>();
		for (int i = 0; i < ints.length; i++) {
			grid[i] = Arrays.copyOf(ints[i], ints[i].length);
			for (int j = 0; j < ints[i].length; j++) {
				int squareIndex = (i / PART) * PART  + j / PART;
				if (ints[i][j] == 0) {
					// new empty spot.
					spots.add(new Spot(i, j));
				} else {
					squares.get(squareIndex).add(new Integer(ints[i][j]));
				}
			}

		}
		Collections.sort(spots);
		startTime = System.currentTimeMillis();
	}
	
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		doSearch(0);
		return solutionCount; // YOUR CODE HERE
	}
	
	private void doSearch(int index) {
		
		if (index > spots.size()-1) {
			// this is the last spot
			solutionCount++;
			solutionList.add(this.toString());
			return;
		}
		
		List<Integer> validNums = spots.get(index).validNumbers();
		if (validNums.size() == 0 && index != (spots.size()-1))
			// ends with no more valid number, backtrack
			return;

		for (Integer num : validNums) {
			spots.get(index).setValue(num);
			doSearch(index+1);
			spots.get(index).setValue(0);
			if (solutionCount >= MAX_SOLUTIONS)
				break;
		}
	}
	
	public String getSolutionText() {
		if (solutionList.size() > 0) {
			return solutionList.get(0);
		}
		return ""; // YOUR CODE HERE
	}
	
	public long getElapsed() {
		return System.currentTimeMillis() - startTime; // YOUR CODE HERE
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder gridString = new StringBuilder();
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				gridString.append(grid[row][col]);
				gridString.append(" ");
			}
			gridString.append("\n");
		}
		return gridString.toString();
	}
}

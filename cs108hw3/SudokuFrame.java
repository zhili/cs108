import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	JTextArea puzzleTextArea; 
	JTextArea solutionTextArea;
	JCheckBox autoCheck;
	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		setLayout(new BorderLayout(4, 4));
		puzzleTextArea = new JTextArea(15, 30);
		puzzleTextArea.setText("");
		puzzleTextArea.setBorder(new TitledBorder("Puzzle"));
		
		puzzleTextArea.getDocument().addDocumentListener(new PuzzleTextListener());
		
		solutionTextArea = new JTextArea(15, 30);
		solutionTextArea.setText("");
		solutionTextArea.setBorder(new TitledBorder("Solution"));
		add(puzzleTextArea, BorderLayout.CENTER);
		add(solutionTextArea, BorderLayout.EAST);
		
		Box controlBox = Box.createHorizontalBox();
		JButton checkButton = new JButton("Check");
		checkButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSudoku();
			}
		});
		
		controlBox.add(checkButton);
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		controlBox.add(autoCheck);
		add(controlBox, BorderLayout.SOUTH);
		// Could do this:
		// setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		
	}
	
	public void startSudoku() {
		Sudoku sudoku;
		int[][] currentGrid = null;
		try {
			currentGrid = Sudoku.textToGrid(puzzleTextArea.getText());
		} catch (Exception ParseError) {
			solutionTextArea.setText("Parsing problem");
		}
		if (currentGrid != null) {
			sudoku = new Sudoku(currentGrid);
			int count = sudoku.solve();
			solutionTextArea.setText(sudoku.getSolutionText());
			solutionTextArea.append("solutions:" + count + "\n");
			solutionTextArea.append("elapsed:" + sudoku.getElapsed() + "ms\n");
		}
	}
	
	class PuzzleTextListener implements DocumentListener {
	 
	    public void insertUpdate(DocumentEvent e) {
	    	if (autoCheck.isSelected())
	    		startSudoku();
	    }
	    public void removeUpdate(DocumentEvent e) {
	    	if (autoCheck.isSelected())
	    		startSudoku();
	    }
	    public void changedUpdate(DocumentEvent e) {
	        //Plain text components do not fire these events

	    }

	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
		frame.setVisible(true);
	}

}

package com.stanford.teris;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;


public class JBrainTetris extends JTetris {
	
	private JCheckBox brainButton;
	private boolean brainActive;
	private Brain brain;
	private Brain.Move currentMove;
	private int currentCount;
	JBrainTetris(int pixels) {
		super(pixels);
		brain = new DefaultBrain();
		currentCount = -1;
		currentMove = null;
	}
	
	@Override
	public JComponent createControlPanel() {
		
		JComponent controls = super.createControlPanel();
		controls.add(new JLabel("Brain:"));
		brainButton = new JCheckBox("Brain active");
		controls.add(brainButton);
		return controls;
	}
	
	@Override
	public void tick(int verb) {

		brainActive = brainButton.isSelected();
		if (verb == DOWN && brainActive) {
			boolean changed = currentCount != count;
			if(changed) {
				currentCount = count;
				if (currentPiece != null) {
					board.undo();
					currentMove = brain.bestMove(board, currentPiece, HEIGHT, currentMove);
				}
			}
			// take an opportunity to move by the brain when verb is DOWN.
			if (currentMove != null) {
				if (!currentPiece.equals(currentMove.piece)) {
					// rotate
					super.tick(ROTATE);
				}
				// shift left
				if (currentX > currentMove.x) {
					super.tick(LEFT);
				} else if (currentX < currentMove.x) {
					super.tick(RIGHT);
				} else {
				}
			}
		} 
		super.tick(verb);

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JBrainTetris.createFrame(tetris);
		frame.setVisible(true);
	}

}

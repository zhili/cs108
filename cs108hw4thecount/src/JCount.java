import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class JCount extends JPanel {
	private JLabel currentCount;
	private countWorker worker;
	private JTextField countField;
	public JCount() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		countField = new JTextField("100000000");
		add(countField);
		currentCount = new JLabel("0");
		add(currentCount);
		JButton startButton = new JButton("start");
		worker = null;

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int initialCount = 100000000;
				if (worker != null) worker.interrupt();
			
				initialCount = Integer.parseInt(countField.getText());

				worker = new countWorker(initialCount);
				worker.start();
			}
		}); 
		add(startButton);
		JButton stopButton = new JButton("stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (worker != null) worker.interrupt();
			}
		});
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0,40)));
	}
	
	private static void createAndShowGUI() {
		JFrame mainFrame = new JFrame();
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
		mainFrame.add(new JCount());
		mainFrame.add(new JCount());
		mainFrame.add(new JCount());
		mainFrame.add(new JCount());
//	
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	private class countWorker extends Thread { 
		int upperNumber;
		
		public countWorker(int InitNumber) {
			this.upperNumber = InitNumber;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < upperNumber; i++) {
				// Sleep for a second, exit on interrupt
				if ( i % 10000 == 0 ) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						// Control goes here if we are interrupted while sleeping --
						// exit the run loop.  (Way 1 to notice interruption)
						break;
					}
					
					final String finalCount = Integer.toString(i); 
					// Message back to the GUI using invokeLater/Runnable
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							currentCount.setText(finalCount);
						}
					});
				}

				// Notice if interrupted -- exit
				// (Way 2 to notice interruption)
				if (isInterrupted())
					break;

			}
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

}

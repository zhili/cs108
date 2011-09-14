import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class WebFrame extends JFrame {
	private DefaultTableModel model;
	private JTable table;
	private JTextField numThreadField;
	private JLabel runningLabel;
	private JLabel completeLabel;
	private JLabel elapsedLabel;
	private JProgressBar progressBar;
	private Thread launcher;
	private int runningThreads;
	private int completedThreads;
	
	public synchronized void increaseRunning() {
		runningThreads++;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				runningLabel.setText("Running:"+ Integer.toString(runningThreads));
			}
		});
	}
	
	public synchronized void decreaseRunning() {
		runningThreads--;
		//completedThreads++;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				runningLabel.setText("Running:"+ Integer.toString(runningThreads));
//				completeLabel.setText("Completed:" + Integer.toString(completedThreads));
//				progressBar.setValue(completedThreads);
			}
		});
	}
	
	public synchronized void runCompletedWithStatus(final int rowNum, final String status) {

		completedThreads++;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				completeLabel.setText("Completed:" + Integer.toString(completedThreads));
				progressBar.setValue(completedThreads);
				model.setValueAt(status, rowNum, 1);
			}
		});
	}
	
	public WebFrame(String title, File file) {
		super(title);
		
		// running threads
		runningThreads = 0;
		// initialize model and table.
		model = new DefaultTableModel(new String[] { "url", "status"}, 0);
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600, 300));
		add(scrollpane);
		
		if (file!=null) loadFile(file); 
		
		// Create a bunch of controls in a box
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		add(panel, BorderLayout.SOUTH);

		JButton singleButton = new JButton("Single Thread Fetch");
		panel.add(singleButton);
		singleButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					runningThreads = 0;
					completedThreads = 0;
					launcher = new Launcher(1);
					launcher.start();
				}
			}
		);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		JButton conButton = new JButton("Concurrent Fetch");
		panel.add(conButton);
		conButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					runningThreads = 0;
					completedThreads = 0;
					int numThreads = Integer.parseInt(numThreadField.getText());
					launcher = new Launcher(numThreads);
					launcher.start();
				}
			}
		);
		
		numThreadField = new JTextField("2");
		numThreadField.setMaximumSize(new Dimension(40, 120));
		panel.add(numThreadField);
		
		runningLabel = new JLabel("Running:0");
		completeLabel = new JLabel("Completed:0");
		elapsedLabel = new JLabel("Elapsed:");
		panel.add(runningLabel);
		panel.add(completeLabel);
		panel.add(elapsedLabel);
		
		progressBar = new JProgressBar(0, model.getRowCount());
		panel.add(progressBar);
		
		JButton stopButton = new JButton("Stop");
		panel.add(stopButton);
		stopButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					launcher.interrupt();
				}
			}
		);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private class Launcher extends Thread {
		private Semaphore rollSemaphore;
		public Launcher(int numThread) {
			rollSemaphore = new Semaphore(numThread);
		}
		
		@Override
		public void run() {
			ArrayList<Thread> threadsArray = new ArrayList<Thread>();
			increaseRunning();
			for (int i = 0; i < model.getRowCount(); i++) {
	
				WebWorker wt = new WebWorker((String)model.getValueAt(i, 0), i, WebFrame.this, rollSemaphore);
				wt.start();
				threadsArray.add(wt);
			}

			for (Thread t:threadsArray) {
				try {
					t.join();
				} catch (InterruptedException e) {
					//System.out.println("interrupted!");
					for (Thread w:threadsArray) {
						w.interrupt();
					}
					break;
				}
			}
			//System.out.println("launcher exit!");
			decreaseRunning();
		}
	}
	
	private void loadFile(File file) {
		BufferedReader input = null;
		try {
			 input = new BufferedReader(new FileReader(file));
			// read the column names
			 String line;
			 while ((line = input.readLine()) != null) { 
				 Object[] rowData = {line, ""};
				 model.addRow(rowData);
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if (input != null) input.close();
			}
			catch(IOException ignored) {}
		}
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new WebFrame("WebLoader", new File("links.txt")); 
			}
		});
	}
	
	
}

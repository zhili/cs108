import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;


public class MetropolisGui extends JFrame {
	private JTable table;
	private MetropolisTableModel model;
	JButton rowButton;
	JTextField continentText;
	JTextField metroText;
	JTextField populationText;
	JButton searchButton;
	
	public MetropolisGui(String title) {
		super(title);
//		container = (JComponent)getContentPane();
		setLayout(new BorderLayout());
		try {
			model = new MetropolisTableModel();
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		}

		Box Inputpanel = Box.createHorizontalBox();

		Inputpanel.add(new JLabel("Metropolis:"));
		metroText = new JTextField(20);
		metroText.setMaximumSize(new Dimension(120, 24));
		Inputpanel.add(metroText);
		Inputpanel.add( Box.createHorizontalStrut(10));
		Inputpanel.add(new JLabel("Continent:"));
		continentText = new JTextField(20);
		continentText.setMaximumSize(new Dimension(120, 24));
		Inputpanel.add(continentText);
		Inputpanel.add( Box.createHorizontalStrut(10));
		Inputpanel.add(new JLabel("Population:"));
		populationText = new JTextField(20);
		populationText.setMaximumSize(new Dimension(120, 24));
		Inputpanel.add(populationText);
		
	
		add(Inputpanel, BorderLayout.NORTH);
		
		table = new JTable(model);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(300, 500));
		add(scrollpane, BorderLayout.CENTER);
		
		Box panel = Box.createVerticalBox();
		add(panel, BorderLayout.EAST);
		
		rowButton = new JButton("Add Row");
		panel.add(rowButton);
		rowButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String population = populationText.getText();
					int pop;
					if (population == null || population.length() == 0) {
						pop = -1;
					} else {
						pop = Integer.parseInt(population);
					}
					model.addRow(metroText.getText(), continentText.getText(), pop);
				}
			}
		);
		
		searchButton = new JButton("Search");
		panel.add(searchButton);
		searchButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String population = populationText.getText();
						int pop;
						if (population == null || population.length() == 0) {
							pop = -1;
						} else {
							pop = Integer.parseInt(population);
						}
						model.search(metroText.getText(), continentText.getText(), pop);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	static public void main(String[] args) 
	{

		new MetropolisGui("Metropolis");
	}
}

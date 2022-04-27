import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class displayFour implements ActionListener {
	private JFrame mainFrame;
	private JLabel statusLabel;
	private JPanel controlPanel;
	
	public displayFour() {
		mainFrame = new JFrame("Cartel Display");
		mainFrame.setSize(700, 700);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		statusLabel = new JLabel("", JLabel.CENTER);
		statusLabel.setSize(350, 100);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}

	public static void main(String args[]) {
		displayFour database = new displayFour();
		database.runDemo();
	}

	private void runDemo() {
		final DefaultListModel<String> cartelName = new DefaultListModel<String>();
		cartelName.addElement("Cartel 1");
		cartelName.addElement("Cartel 2");
		cartelName.addElement("Cartel 3");
		cartelName.addElement("Cartel 4");
		cartelName.addElement("Cartel 5");

		final JList<String> cartelList = new JList<String>(cartelName);
		cartelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cartelList.setSelectedIndex(1);
		cartelList.setVisibleRowCount(6);
		cartelList.setPreferredSize(new Dimension(100, 200));

		JScrollPane ListScrollPane = new JScrollPane(cartelList);
		controlPanel.add(ListScrollPane);

		JButton showButton = new JButton("Show Selected Cartel");
		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cartelList.getSelectedIndex() != -1) {
					DefaultListModel<String> playerName = new DefaultListModel<String>();
					playerName.addElement(cartelList.getSelectedValue());
					playerName.addElement("Player 1");
					playerName.addElement("Player 2");
					playerName.addElement("Player 3");
					playerName.addElement("Player 4");
					playerName.addElement("Player 5");
					
					JList<String> playerList = new JList<String>(playerName);
					playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					playerList.setSelectedIndex(1);
					playerList.setVisibleRowCount(6);
					playerList.setPreferredSize(new Dimension(100, 200));
					
					JScrollPane ListPlayerScrollPane = new JScrollPane(playerList);
					controlPanel.add(ListPlayerScrollPane);
					
					JButton showPlayerButton = new JButton("Show Selected Player");
					showPlayerButton.addActionListener(new ActionListener() {
						@SuppressWarnings("unused")
						public void actionPerformed(Action e) {
							if (playerList.getSelectedIndex() != -1) {
								
							}
						}

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							
						}
					});
					controlPanel.add(showPlayerButton);
					
					JButton addPlayerButton = new JButton("Add Player");
					addPlayerButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String title = JOptionPane.showInputDialog(null, "Enter player name:");
							System.out.println("Title is:" + title);
							playerName.addElement(title);
						}
					});
					controlPanel.add(addPlayerButton);
				}
			}
		});
		controlPanel.add(showButton);
/*
 * ADD BUTTON
 */
		JButton addCartelButton = new JButton("Add Cartel");
		addCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = JOptionPane.showInputDialog(null, "Enter cartel name:");
				System.out.println("Title is:" + title);
				cartelName.addElement(title);
			}
		});
		controlPanel.add(addCartelButton);
		
/*
 * DELETE BUTTON
 */
		JButton deleteCartelButton = new JButton("Delete Cartel");
		deleteCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = "";
				if (cartelList.getSelectedIndex() != -1) {
					int index = cartelList.getSelectedIndex();
					data = "Cartel deleted: " + cartelList.getSelectedValue();
					cartelName.removeElementAt(index);
					statusLabel.setText(data);
				}
				statusLabel.setText(data);
			}
		});
		controlPanel.add(deleteCartelButton);

		mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

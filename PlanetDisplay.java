import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class PlanetDisplay implements ActionListener {
  private JFrame mainFrame;
  private JLabel headerLabel;
  private JLabel statusLabel;
  private JPanel controlPanel;

  public PlanetDisplay() {
    mainFrame = new JFrame("Planet Display");
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(3, 1));

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    
    headerLabel = new JLabel("", JLabel.CENTER);
    statusLabel = new JLabel("", JLabel.CENTER);
    statusLabel.setSize(350, 100);

    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    mainFrame.add(headerLabel);
    mainFrame.add(controlPanel);
    mainFrame.add(statusLabel);
    mainFrame.setVisible(true);
  }

  public static void main(String args[]) {
	PlanetDisplay database = new PlanetDisplay();
    database.runDemo();
  }

  private void runDemo() {
    headerLabel.setText("Planet List");
    final DefaultListModel<String> planetName = new DefaultListModel<String>();

    planetName.addElement("Planet 1");
    planetName.addElement("Planet 2");
    
    String select = "SELECT * IN PLANET";
    

    final JList<String> planetList = new JList<String>(planetName);
    planetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    planetList.setSelectedIndex(1);
    planetList.setVisibleRowCount(6);
    planetList.setPreferredSize(new Dimension(100, 200));

    JScrollPane ListScrollPane = new JScrollPane(planetList);

/*
 * EDIT BUTTON********************************************************************
 */    
    
    JButton editButton = new JButton("Edit");

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (planetList.getSelectedIndex() != -1) {
          data = "Planet Selected: " + planetList.getSelectedValue();
          statusLabel.setText(data);
          
          JFrame sideFrame = new JFrame(planetList.getSelectedValue());
          sideFrame.setSize(400, 400);
          sideFrame.setLayout(new GridLayout(3, 1));
          
          sideFrame.setVisible(true);
        }
      }
    });
/*
 * ADD BUTTON********************************************************************
 */
    JButton addButton = new JButton("add");

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String title = JOptionPane.showInputDialog(null, "Enter planet name:");
        planetName.addElement(title);
      }
    });
  
/*
 * DELETE BUTTON******************************************************************************
 */
    JButton deleteButton = new JButton("delete");

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (planetList.getSelectedIndex() != -1) {
          int index = planetList.getSelectedIndex();
          data = "Planet Deleted: " + planetList.getSelectedValue();
          planetName.removeElementAt(index);
          statusLabel.setText(data);
        }

        statusLabel.setText(data);
      }
    });
    
    controlPanel.add(ListScrollPane);
    controlPanel.add(editButton);
    controlPanel.add(addButton);
    controlPanel.add(deleteButton);

    mainFrame.setVisible(true);
  
  }
  
 //******************************************************************************

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }
}

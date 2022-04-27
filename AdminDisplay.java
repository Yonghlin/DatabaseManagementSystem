import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class AdminDisplay implements ActionListener {
  private JFrame mainFrame;
  private JLabel headerLabel;
  private JLabel statusLabel;
  private JPanel controlPanel;

  
  public AdminDisplay() {
    mainFrame = new JFrame("Admin Display");
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(3,1));
    
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
    AdminDisplay database = new AdminDisplay();
    database.runDemo();
  }

  private void runDemo() {
    headerLabel.setText("Control in action: JList");
    final DefaultListModel<String> playerName = new DefaultListModel<String>();


    playerName.addElement("John Deer");
    playerName.addElement("Joe Well");

    final JList<String> playerList = new JList<String>(playerName);
    playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playerList.setSelectedIndex(1);
    playerList.setVisibleRowCount(6);
    playerList.setPreferredSize(new Dimension(100, 200));

    JScrollPane ListScrollPane = new JScrollPane(playerList);

    //Show button will show a SQL view of attribues and there current values
    JButton showButton = new JButton("Show");

    showButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (playerList.getSelectedIndex() != -1) {
          data = "Player Selected Attributes: " + "(Will lead to SQL view values)?";
          statusLabel.setText(data);
        }
        statusLabel.setText(data);
      }
    });
/*
 * ADD BUTTON********************************************************************
 */
    JButton addButton = new JButton("add");

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String title = JOptionPane.showInputDialog(null, "Enter planet name:");
        System.out.println("Title is:" + title);
        playerName.addElement(title);
      }
    });
  
/*
 * DELETE BUTTON******************************************************************************
 */
    JButton deleteButton = new JButton("delete");

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (playerList.getSelectedIndex() != -1) {
          int index = playerList.getSelectedIndex();
          data = "Planet Deleted: " + playerList.getSelectedValue();
          playerName.removeElementAt(index);
          statusLabel.setText(data);
        }

        statusLabel.setText(data);
      }
    });
    
    controlPanel.add(ListScrollPane);
    controlPanel.add(showButton);
    controlPanel.add(addButton);
    controlPanel.add(deleteButton);
    
    mainFrame.setVisible(true);
    
/*
 * EDIT BUTTON
 */
    JLabel change = new JLabel("Player attribue to change is: ");
    JLabel changeValue = new JLabel("change value to  ");
    
    //will be SQL to see current players attributes
    String[] attributes = {"Ships", "Planet", "Private Messages", "etc"};
    JComboBox editButton = new JComboBox(attributes);
    
        
    editButton.setSelectedIndex(0);
    editButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(editButton.getSelectedIndex() != -1) {
            String title = JOptionPane.showInputDialog(null, "Enter new attribute value:");
            System.out.println("New attribute value is" + title);
            // SQL statement to change value to new value
            
          }
        }
    });
    
    
    
    
    controlPanel.add(change);
    controlPanel.add(editButton);
    controlPanel.add(changeValue);
    
    
    
   
    
    
    
    
    
     
   
        
        
  
    
       
    
        
        
        
  }
  
 //******************************************************************************

  
  
  
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }


}

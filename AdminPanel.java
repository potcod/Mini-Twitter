import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class AdminPanel {
    private List<User> users = new ArrayList<>();
    private static AdminPanel instance = new AdminPanel();
    private JTree tree; 
    private int userTotal,groupTotal = 0;
    private JLabel userTotalDisplay, groupTotalDisplay, messageTotalDisplay, positiveTotalDisplay;


    private AdminPanel() {
        JFrame jfrm = new JFrame();
        jfrm.setSize(700, 500);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Setup tree and model
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Rooter");
        DefaultTreeModel model = new DefaultTreeModel(top);
        DefaultMutableTreeNode users = null;
        users = new DefaultMutableTreeNode("List of Users");
        top.add(users);
        tree = new JTree(model);

        //Tree GUI
        JScrollPane treePanel = new JScrollPane(tree);
        treePanel.setBorder(BorderFactory.createLineBorder(Color.blue,5));
        treePanel.setOpaque(true);
        
        //Panel to put every other button and stuff
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createLineBorder(Color.pink, 5));

        //Various button classes
        addUserButtons(panel,users, model); //text field and add button for Users
        addGroupButtons(panel,top, model); //text field and add button for Groups

        openUserButton(constraints, panel); //open User Panel Button
        
        bottomButtons(constraints, panel); //User total, Group Total, Message Total, Positive Message Ratio buttons

        newButtons(constraints, panel);

        //Divide tree and button areas
        JSplitPane splitPlane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, panel );
        splitPlane.setDividerLocation(200);

        //Add to frame
        jfrm.add(splitPlane);
        jfrm.setVisible(true);
    }

    public static AdminPanel getInstance() { //Singleton pattern
        if (instance == null)
            instance = new AdminPanel();
        return instance;
    }

    private void addUserButtons(JPanel panel, DefaultMutableTreeNode nodeToAddTo, DefaultTreeModel model) {
        GridBagConstraints constraints = new GridBagConstraints();

            //TextField Add User
            JTextField addUserTextField = new JTextField(20);
            addUserTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            constraints.gridx = 0;
            constraints.gridy = 0;
            panel.add(addUserTextField, constraints);

            //Button Add User
            JButton addUserButton = new JButton("Add User");
            addUserButton.setPreferredSize(new Dimension(200, 30));
            addUserButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) { 
                    String textField = addUserTextField.getText();
                    
                    TreePath path = tree.getSelectionPath();
                    DefaultMutableTreeNode selectedNode = null;
                    if(path != null){
                        selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                        System.out.println("Selected node" + selectedNode.toString());
                    }
                    
                    
                    if(textField != null){
                        addUserTextField.setText("");
                        selectedNode.add(new DefaultMutableTreeNode(textField));
                        userTotal++;
                        users.add(new User(textField));
                        panel.revalidate();
                        panel.repaint();
                    }
                    model.reload();
                    tree.expandPath(path);
                }
            });
            constraints.gridx = 1;
            constraints.gridy = 0;
            panel.add(addUserButton, constraints);
        }
    private void addGroupButtons(JPanel panel, DefaultMutableTreeNode nodeToAddTo, DefaultTreeModel model){
        GridBagConstraints constraints = new GridBagConstraints();
        
        //TextField Add Group
        JTextField addGroupTextField = new JTextField(20);
        addGroupTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(addGroupTextField, constraints);

        //Button Add Group
        JButton addGroupButton = new JButton("Add Group");
        addGroupButton.setPreferredSize(new Dimension(200,30));
        addGroupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String textField = addGroupTextField.getText() + "(group)";

                TreePath path = tree.getSelectionPath();
                DefaultMutableTreeNode selectedNode = null;
                if(path != null){
                    selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    System.out.println("Selected node" + selectedNode.toString());
                }
                
                if(textField!= null){
                    addGroupTextField.setText("");
                    selectedNode.add(new DefaultMutableTreeNode(textField));
                    groupTotal++;
                    panel.revalidate();
                    panel.repaint();
                    

                }
                model.reload();
                tree.expandPath(path);
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(addGroupButton, constraints);

    }

    public List<User> returnListOfUsers(){ //To pass to other class
        return users;
    }
    private User findUserByName(String username){ //Look through user List to see if user exists
        for(int i = 0; i<users.size();i++){
            if(users.get(i).getUserID().equals(username)) 
                return users.get(i);
        } 
        return null;   
    }
    private boolean checkValidityOfIDS(){ //Method used for assignment 3

        for(int i = 0; i < users.size();i++){
            String ID = users.get(i).getUserID();
            if(ID.contains(" "))
                return false;
            for(int j = 0; j<users.size();j++){
                if(i != j && ID.equals(users.get(j).getUserID()))
                    return false;
            }
        }
        return true;
    }

    private User lastUpdatedUser(){
        long base = -99999;
        User focus = null;
        
        for(int i = 0;i<users.size();i++){
            if(users.get(i).getCreationTime() > base)
                base = users.get(i).getCreationTime();
                focus = users.get(i);
        }
        return focus;
    }
    private void checkDisplayEmpty(JPanel panel){ //Check if the button displays are empty to delete 
        if(userTotalDisplay != null){
                    
            panel.remove(userTotalDisplay);
            panel.revalidate();
            panel.repaint();
        }
        if(messageTotalDisplay != null){
            panel.remove(messageTotalDisplay);
            panel.revalidate();
            panel.repaint();
        }
        if(positiveTotalDisplay != null){
            panel.remove(positiveTotalDisplay);
            panel.revalidate();
            panel.repaint();
        }
        if(groupTotalDisplay != null){
            panel.remove(groupTotalDisplay);
            panel.revalidate();
            panel.repaint();
        }
    }
    private void openUserButton(GridBagConstraints constraints, JPanel panel){
        //Button Open User
        JButton openUserButton = new JButton("Open User");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(openUserButton,constraints);
        openUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
            TreePath path = tree.getSelectionPath();
            DefaultMutableTreeNode selectedNode = null;
                if(path != null){
                    selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    System.out.println("Selected node" + selectedNode.toString());
                }
                
                if(findUserByName(selectedNode.toString()) != null){
                    UserPanel panel = new UserPanel(findUserByName(selectedNode.toString()), AdminPanel.this.users);
                    panel.openPanel();
                }
               
            }
        });
    }
    private void bottomButtons(GridBagConstraints constraints, JPanel panel){

        // All other buttons for panel
        JButton showGroupTotalButton = new JButton("Group Total");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(showGroupTotalButton,constraints);
        showGroupTotalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                checkDisplayEmpty(panel);
                groupTotalDisplay = new JLabel("Group Total: " + groupTotal);
                constraints.gridx = 0;
                constraints.gridy = 5;
                panel.add(groupTotalDisplay, constraints);
                panel.revalidate();
                panel.repaint();
            }
        });

       
        JButton showUserTotalButton = new JButton("User Total");
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(showUserTotalButton,constraints);
        showUserTotalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                checkDisplayEmpty(panel);

                userTotalDisplay = new JLabel("User Total: " + userTotal);
                constraints.gridx = 0;
                constraints.gridy = 5;
                panel.add(userTotalDisplay, constraints);
                panel.revalidate();
                panel.repaint();
            }
        });

        
        JButton showMessagesButton = new JButton("Show Total Messages");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(showMessagesButton,constraints);
        showMessagesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                int totalMessages = 0;
                for(int i = 0;i<AdminPanel.this.users.size();i++){
                    totalMessages += AdminPanel.this.users.get(i).getNumberOfPosts();
                    System.out.println("loop");
                }
                checkDisplayEmpty(panel);

                messageTotalDisplay = new JLabel("Total Messages: " + totalMessages);
                constraints.gridx = 0;
                constraints.gridy = 5;
                panel.add(messageTotalDisplay, constraints);
                panel.revalidate();
                panel.repaint();
            }
        });

        JButton showPositiveButton = new JButton("Show Positive Messages");
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(showPositiveButton,constraints);
        showPositiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                int positiveMessages=0;
                for(int i = 0;i<AdminPanel.this.users.size();i++){
                    positiveMessages+=AdminPanel.this.users.get(i).getPositiveMessages();;
                }
                int totalMessages = 0;
                for(int i = 0;i<AdminPanel.this.users.size();i++){
                    totalMessages += AdminPanel.this.users.get(i).getNumberOfPosts();
                    System.out.println("loop");
                }
                checkDisplayEmpty(panel);
                
                double positiveRatio = (double) totalMessages/positiveMessages;
                System.out.println("HEREE" + positiveRatio);
                positiveTotalDisplay = new JLabel("Positive Message Ratio: " + positiveRatio);
                constraints.gridx = 0;
                constraints.gridy = 5;
                panel.add(positiveTotalDisplay, constraints);
                panel.revalidate();
                panel.repaint();
            }
        });
    }
    private void newButtons(GridBagConstraints constraints, JPanel panel){
        JButton checkValidButton = new JButton("Check ID Validity");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(checkValidButton,constraints);
        checkValidButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                JFrame validFrame = new JFrame();
                validFrame.setSize(300, 200);
                validFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                validFrame.setVisible(true);
                validFrame.setLayout(new GridBagLayout());
                
                String validityString;
                if(checkValidityOfIDS())
                    validityString = "All IDS valid";
                else
                    validityString = "Error, invalid IDS";

                JLabel validLabel = new JLabel(validityString);
                validFrame.add(validLabel);
            }
        });

        JButton lastUpdatedButton = new JButton("Last Updated User");
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(lastUpdatedButton,constraints);
        lastUpdatedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                JFrame lastUpdatedFrame = new JFrame();
                lastUpdatedFrame.setSize(300, 200);
                lastUpdatedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                lastUpdatedFrame.setVisible(true);
                lastUpdatedFrame.setLayout(new GridBagLayout());
                
            
                JLabel validLabel = new JLabel(lastUpdatedUser().getUserID());
                lastUpdatedFrame.add(validLabel);
            }
        });
    }
}
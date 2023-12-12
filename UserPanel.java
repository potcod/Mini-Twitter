import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class UserPanel {
    private User userFocus;
    private String panelName;
    private List<User> listOfUsers;
    private JList<String> newsFeedList;
    private JList followingList;

    
    UserPanel(User person, List<User> userList){
        userFocus = person;
        panelName = person.getUserID();
        listOfUsers = userList;

       
    }

    public void openPanel(){
        JFrame userPanel = new JFrame(panelName);
        userPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userPanel.setSize(500,600);
        userPanel.setVisible(true);
        userPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Follow User Button and Field
        followUserStuff(c, userPanel);

        //Following Label and List
        following(c, userPanel);
    
        //Post Tweet Button and Field
        postTweetStuff(c, userPanel);

        //News Feed Label and List
        newsFeedStuff(c, userPanel);
        
    }
    
    private User findUserByName(String username){
        for(int i = 0; i<listOfUsers.size();i++){
            if(listOfUsers.get(i).getUserID().equals(username)) 
                return listOfUsers.get(i);
        } 
        return null;   
    }
    private String[] convertUserListToArray(List<User> list){
        String followingArray[] = new String[list.size()];
        for(int i = 0;i<list.size();i++)
            followingArray[i] = list.get(i).getUserID();
        
        return followingArray;
    }
    private void updateNewsFeed(String newPost, GridBagConstraints c, JPanel panel){ //Observer Pattern
        DefaultListModel<String> model = (DefaultListModel<String>) newsFeedList.getModel();
        model.addElement(newPost);
    }
    private void followUserStuff(GridBagConstraints c, JFrame userPanel){
        
        JTextField followUserTextField = new JTextField(20);
        followUserTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        c.gridx=0;
        c.gridy=0;
        c.anchor = GridBagConstraints.WEST;
        userPanel.add(followUserTextField,c);

        JButton followUserButton = new JButton("Follow User");
        c.gridx=1; 
        c.gridy=0;
        c.insets = new Insets(0, 10, 0, 0);
        userPanel.add(followUserButton,c);
        followUserButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                String userToFollow = followUserTextField.getText();
                if(findUserByName(userToFollow) != null){
                    followUserTextField.setText("");
                    userFocus.addFollowing(findUserByName(userToFollow)); // Make userfocus user follow the desired user
                    findUserByName(userToFollow).addFollower(userFocus);  // Make the desired user followed by userfocus
                    
                }
                List<User> updatedFollowing = new ArrayList<>(userFocus.getUserFollowing());
                String[] updatedFollowingArray = convertUserListToArray(updatedFollowing);
                DefaultListModel<String> followingListModel = new DefaultListModel<>();
                for(String user : updatedFollowingArray)
                    followingListModel.addElement(user);
                followingList.setModel(followingListModel);

                userPanel.repaint();
                userPanel.revalidate();
            }
        });
    }
    private void following(GridBagConstraints c, JFrame userPanel){
        JLabel following = new JLabel("Following");
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=2;
        c.insets = new Insets(10, 0, 0, 0);
        userPanel.add(following,c);
        
        List<User> temp = new ArrayList<>(userFocus.getUserFollowing());
        followingList = new JList(convertUserListToArray(temp));
        followingList.setBorder(BorderFactory.createLineBorder(Color.black));
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=2;
        c.insets = new Insets(10, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        userPanel.add(new JScrollPane(followingList),c);
    }
    
    private void postTweetStuff(GridBagConstraints c, JFrame userPanel){JTextField postTweetTextField = new JTextField(20);
        postTweetTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        c.gridx=0;
        c.gridy=3;
        c.gridwidth=1;
        c.insets = new Insets(10, 0, 0, 0);
        userPanel.add(postTweetTextField,c);

        JButton postTweetButton = new JButton("Post Tweet");
        c.gridx=1;
        c.gridy=3;
        c.insets = new Insets(10, 0, 0, 0);
        userPanel.add(postTweetButton,c);

        JLabel lastUpdateLabel = new JLabel("Last Update: NA");
        c.gridx=1;
        c.gridy=7;
        userPanel.add(lastUpdateLabel,c);

        postTweetButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                String postString = postTweetTextField.getText();
                if(postString != null){
                    postTweetTextField.setText("");
                    userFocus.addPost(postString);

                    //Assignment 3 addition
                    long lastUpdate = System.currentTimeMillis();
                    lastUpdateLabel.setText("Last Update: " + Long.toString(lastUpdate));
                    lastUpdateLabel.setBorder(BorderFactory.createLineBorder(Color.pink, 5));
                    
                    System.out.println("TEST");
                    
                }
                //I think this works
            }
        });}
    private void newsFeedStuff(GridBagConstraints c, JFrame userPanel){
        JLabel newsFeedLabel = new JLabel("News Feed");
        c.gridx=0;
        c.gridy=4;
        c.insets = new Insets(10, 0, 0, 0);
        userPanel.add(newsFeedLabel,c);

        int sizeOfFollowing = userFocus.getUserFollowing().size();
        List<String> tempListFollowingPosts = new ArrayList<>();
        for(int i = 0;i<sizeOfFollowing;i++){
            for(int j = 0;j<userFocus.getUserFollowing().get(i).getNumberOfPosts();j++){
                tempListFollowingPosts.add(userFocus.getUserFollowing().get(i).getPosts().get(j));
            }
        }
        
        newsFeedList = new JList(tempListFollowingPosts.toArray());
        newsFeedList.setBorder(BorderFactory.createLineBorder(Color.black));
        c.gridx=0;
        c.gridy=5;
        c.gridwidth=2;
        c.insets = new Insets(10, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        userPanel.add(new JScrollPane(newsFeedList),c);



        //Assignment 3 
        JLabel creationTimeLabel = new JLabel("Creation Time: " + Long.toString(userFocus.getCreationTime()));
        
        c.gridx=0;
        c.gridy=6;
        userPanel.add(creationTimeLabel,c);


        

    }

}
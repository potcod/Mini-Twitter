import java.util.ArrayList;
import java.util.List;
public class User {
    private String userID;
    private List<User> userFollowers; //List of users or String userIDS? , same for following
    private List<User> userFollowing;
    private List<String> userFeed;
    private List<String> posts;

    private long creationTime;
    
    public User(String name){
        this.userID= name;
        this.userFollowers = new ArrayList<>();
        this.userFollowing = new ArrayList<>();
        this.userFeed = new ArrayList<>();
        this.posts = new ArrayList<>();

        creationTime = System.currentTimeMillis();
    }

    public long getCreationTime(){
        return creationTime;
    }
    
    public String getUserID(){
        return userID;
    }
    public void addFollower(User newFollower){ 
        userFollowers.add(newFollower);
    }
    public void addFollowing(User newFollow){
        userFollowing.add(newFollow);
    }

    public List<User> getUserFollowing(){
        return userFollowing;
    }
   
    public String[] getNewsFeed(){
        String[] array = userFeed.toArray(new String[0]);
        return array;
    }

    public String toString(){
        return userID;
    }


    public void addPost(String newPost) {
        posts.add("[" + userID + "]" + newPost);
        notifyFollowers(newPost);
    }

    public void notifyFollowers(String newPost){ //Observer Pattern
        for(int i = 0;i<userFollowers.size();i++){
            userFollowers.get(i).updateFeed(this, newPost);
        }
    }

    public void updateFeed(User user, String newPost){ //Observer Pattern
        userFeed.add("[" + userID + "]" + newPost);
    }

    public List<String> getPosts() {
        return posts;
    }

    public int getNumberOfPosts(){
        return posts.size();
    }
    public int getPositiveMessages(){ //To make finding positive messages easier
        int messages=0;
        for(int i = 0;i<posts.size();i++){
            String post = posts.get(i).toLowerCase();
            if(post.contains("good") || post.contains("great") || posts.contains("nice"))
                messages++;
        }
        return messages;

    }

    
    



}

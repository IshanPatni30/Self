package util;

import android.app.Application;

public class JournalAPI extends Application {
    private String username;
private String userId;
private static JournalAPI instance;
    public static JournalAPI getInstance(){
        if(instance==null){
            instance=new JournalAPI();
        }
        return instance;
    }

    public JournalAPI() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package entity;

import lombok.Data;

@Data
public class Friend {
    private String friend_L;
    private String friend_R;
    private String friend_F;
    public Friend(){};
    public Friend(String friend_L, String friend_R, String friend_F){
        this.friend_L = friend_L;
        this.friend_R = friend_R;
        this.friend_F = friend_F;
    }

    public String getFriend_L() {
        return friend_L;
    }

    public void setFriend_L(String friend_L) {
        this.friend_L = friend_L;
    }

    public String getFriend_R() {
        return friend_R;
    }

    public void setFriend_R(String friend_R) {
        this.friend_R = friend_R;
    }

    public String getFriend_F() {
        return friend_F;
    }

    public void setFriend_F(String friend_F) {
        this.friend_F = friend_F;
    }
}

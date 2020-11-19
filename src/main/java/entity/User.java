package entity;

import lombok.Data;

public class User {
    private Long id;
    private String username;
    private String password;
    private String avater;
    public User(){}
    public User(String username ,String password,String avatar){
        this.username = username;
        this.password = password;
        this.avater = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avater;
    }

    public void setAvatar(String avatar) {
        this.avater = avatar;
    }
}

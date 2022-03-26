package tech.devcrazelu.url_shortener.models;

import java.util.UUID;

public class AppUser {
    private  UUID id;
    String email;
    String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AppUser() {}

    public AppUser(String email, String password){
        this.email = email;
        this.password = password;
    }

    public AppUser(UUID id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public AppUser(UUID id, String email){
        this.id = id;
        this.email = email;
    }
}

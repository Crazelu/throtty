package tech.devcrazelu.url_shortener.models;

public class AppUser {
    private  int id;
    String email;

    public String getPassword() {
        return password;
    }

    String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public AppUser(String email, String password){
        this.email = email;
        this.password = password;
    }

    public AppUser(int id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public AppUser(int id, String email){
        this.id = id;
        this.email = email;
    }
}

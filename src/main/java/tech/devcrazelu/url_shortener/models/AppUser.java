package tech.devcrazelu.url_shortener.models;

public class AppUser {
    private  int id;
    private  String email;
    private String password;
    private boolean verified = false;
    private boolean registeredFromOAuth = false;

    public String getPassword() {
        return password;
    }

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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isRegisteredFromOAuth() {
        return registeredFromOAuth;
    }

    public void setRegisteredFromOAuth(boolean registeredFromOAuth) {
        this.registeredFromOAuth = registeredFromOAuth;
    }

    public AppUser(int id, String email, boolean verified, boolean registeredFromOAuth){
        this.id = id;
        this.email = email;
        this.verified = verified;
        this.registeredFromOAuth = registeredFromOAuth;
    }

    public AppUser(int id, String email){
        this.id = id;
        this.email = email;
    }
}

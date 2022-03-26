package tech.devcrazelu.url_shortener.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthRequest {
    public String email;
    public String password;

    public AuthRequest(
           @JsonProperty("email") String email,
           @JsonProperty("password") String password
            ){
        this.email = email;
        this.password = password;
    }
}

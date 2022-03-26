package tech.devcrazelu.url_shortener.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordRequest {
    public String email;
    public int otp;
    public String password;

    public ResetPasswordRequest(
            @JsonProperty("email") String email,
            @JsonProperty("otp") int otp,
            @JsonProperty("newPassword") String password
    ){
        this.email = email;
        this.otp = otp;
        this.password = password;
    }
}

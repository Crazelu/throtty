package tech.devcrazelu.url_shortener.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.exceptions.ApiException;
import tech.devcrazelu.url_shortener.models.requests.AuthRequest;

@Service
public class RequestValidator {

    public void validateAuthRequest(AuthRequest request){
        if(request.email == null || request.email.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, "email is not provided");
        if(request.password == null || request.password.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, "password is not provided");
        validateEmail(request.email);
    }

    public void validateEmail(String email){
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if(!email.matches(emailRegex)) throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid email");
    }
}

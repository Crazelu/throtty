package tech.devcrazelu.url_shortener.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.exceptions.ApiException;
import tech.devcrazelu.url_shortener.models.requests.AuthRequest;
import tech.devcrazelu.url_shortener.models.requests.CreateShortUrlRequest;
import tech.devcrazelu.url_shortener.models.requests.ResetPasswordRequest;

@Service
public class RequestValidator {

    private void validateNotEmpty(String param, String paramName){
        if(param == null || param.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, paramName+" is not provided");
    }

    public void validateAuthRequest(AuthRequest request){
        validateEmail(request.email);
        validatePassword(request.password);
    }

    public void validateEmail(String email){
        validateNotEmpty(email, "Email");
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if(!email.matches(emailRegex)) throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid email");
    }

    private void validatePassword(String password){
        validateNotEmpty(password, "Password");
        if(password.length() <8)  throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Password must contain at least 8 characters");

    }

    private void validateOTP(String otp){
        if(otp == null || otp.isEmpty() || otp.equals("0")) throw new ApiException(HttpStatus.BAD_REQUEST, "OTP is not provided");
        if(otp.length()!=4) throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid OTP");
        try{
            Integer.decode(otp);
        }catch(NumberFormatException e){
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid OTP");
        }
    }

    public void validateResetPasswordRequest(ResetPasswordRequest request){
        validateEmail(request.email);
        validateOTP(String.valueOf(request.otp));
        validatePassword(request.password);
    }

    public void validateCreateShortUrlRequest(CreateShortUrlRequest request){
       validateNotEmpty(request.longUrl, "Link");
       if(request.shortUrl!= null) validateNotEmpty(request.shortUrl, "Short URL");
    }
}

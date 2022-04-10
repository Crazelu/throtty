package tech.devcrazelu.url_shortener.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
   private HttpStatus status;
   private String message;

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ApiException(HttpStatus status, String message){
        super(message);
        this.message = message;
        this.status = status;
    }
}

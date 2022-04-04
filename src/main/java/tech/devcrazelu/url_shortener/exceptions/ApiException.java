package tech.devcrazelu.url_shortener.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
   private HttpStatus status;
   private String message;

    public ApiException(String message){
        super(message);
        this.message = message;
        this.status = HttpStatus.UNAUTHORIZED;
    }

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

    public ApiException(HttpStatus status, Throwable exception){
        super(exception.getMessage());
        this.message = exception.getMessage();
        this.status = status;
    }
}

package tech.devcrazelu.url_shortener.exceptions;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(){
        super("Unauthorized access");
    }

    public AuthorizationException(String message){
        super(message);
    }
}

package tech.devcrazelu.url_shortener.exceptions;

public class ResourceCreationException extends RuntimeException{
    public ResourceCreationException(String message){
        super(message);
    }
}

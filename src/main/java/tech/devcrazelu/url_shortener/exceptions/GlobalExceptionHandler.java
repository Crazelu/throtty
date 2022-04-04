package tech.devcrazelu.url_shortener.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        ApiResponse response = new ApiResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException exception){
        ApiResponse response = new ApiResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiResponse> handleAuthorizationException(AuthorizationException exception){
        ApiResponse response = new ApiResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<ApiResponse> handleResourceCreationException(ResourceCreationException exception){
        ApiResponse response = new ApiResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFoundException(){
        ApiResponse response = new ApiResponse("Account not found");
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception){
        ApiResponse response = new ApiResponse(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

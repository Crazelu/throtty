package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tech.devcrazelu.url_shortener.exceptions.ResourceNotFoundException;
import tech.devcrazelu.url_shortener.models.AppUser;
import tech.devcrazelu.url_shortener.models.requests.AuthRequest;
import tech.devcrazelu.url_shortener.models.requests.ResetPasswordRequest;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import tech.devcrazelu.url_shortener.services.UserService;
import tech.devcrazelu.url_shortener.utils.AuthUtil;
import tech.devcrazelu.url_shortener.utils.JwtUtil;
import tech.devcrazelu.url_shortener.validators.RequestValidator;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RequestValidator validator;


    @PostMapping("/createAccount")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody AuthRequest request){
        validator.validateAuthRequest(request);
        try{
            userService.findUserByEmail(request.email);
        }catch(ResourceNotFoundException exception){
            AppUser user = userService.createUser(request.email, request.password);
            String token = jwtUtil.generateToken(String.valueOf(user.getId()));
            if(user != null ){
                return new ResponseEntity(new ApiResponse(true, token,"Login to continue"), HttpStatus.CREATED);
            }
            return new ResponseEntity(new ApiResponse("Account creation failed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new ApiResponse("Email is already taken"), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest request){
        validator.validateAuthRequest(request);

        try{
            userService.findUserByEmail(request.email);
        }catch(Exception e){
            return new ResponseEntity(new ApiResponse("Account not registered"),HttpStatus.FORBIDDEN);
        }

        int id =  userService.verifyCredentials(request.email, request.password);

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(id), request.password));
        }catch(Exception e){
            return new ResponseEntity(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }

        String token = jwtUtil.generateToken(String.valueOf(id));
        if(id != -1 && token != null){
            return new ResponseEntity(new ApiResponse(true, token), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse("Invalid credentials"),HttpStatus.FORBIDDEN);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@PathVariable String email){
        validator.validateEmail(email);
            userService.forgotPassword(email);
            return new ResponseEntity(new ApiResponse(true,  "Check your email for an OTP"), HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request){
        validator.validateResetPasswordRequest(request);
        boolean isPasswordReset = userService.setNewPassword(request.email, request.password, request.otp);
        if(isPasswordReset) return new ResponseEntity(new ApiResponse(true, null, "Password reset successful"), HttpStatus.OK);
        return new ResponseEntity(new ApiResponse("Password reset failed"), HttpStatus.FORBIDDEN);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getUser(){
        int id = authUtil.getAuthenticatedUserId();
        AppUser user = userService.getUser(id);
        if (user != null) return new ResponseEntity(new ApiResponse(true, new AppUser(user.getId(), user.getEmail())), HttpStatus.OK);
        return new ResponseEntity(new ApiResponse("User not found"), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ApiResponse> deleteUser(){
        int id = authUtil.getAuthenticatedUserId();
        boolean deleted = userService.deleteUser(id);
        if (deleted) return new ResponseEntity(new ApiResponse(true, "User deleted"), HttpStatus.OK);
        return new ResponseEntity(new ApiResponse("User not found"), HttpStatus.NOT_FOUND);
    }
}

package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ApiResponse createAccount(@RequestBody AuthRequest request){
        validator.validateAuthRequest(request);
        try{
            userService.verifyCredentials(request.email, request.password);
        }catch(ResourceNotFoundException exception){
            AppUser user = userService.createUser(request.email, request.password);
            String token = jwtUtil.generateToken(String.valueOf(user.getId()));
            if(user != null ){
                return new ApiResponse(true, token,"Login to continue");
            }
            return new ApiResponse("Account creation failed");
        }
        return new ApiResponse("Email is already taken");
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthRequest request){
        validator.validateAuthRequest(request);
        int id =  userService.verifyCredentials(request.email, request.password);

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(id), request.password));
        }catch(Exception e){
            return new ApiResponse(e.getMessage());
        }

        String token = jwtUtil.generateToken(String.valueOf(id));
        if(id != -1 && token != null){
            return new ApiResponse(true, token);
        }
        return new ApiResponse("Invalid credentials");
    }

    @PostMapping("/forgotPassword")
    public ApiResponse forgotPassword(@PathVariable String email){
        validator.validateEmail(email);
            userService.forgotPassword(email);
            return new ApiResponse(true,  "Check your email for an OTP");
    }

    @PostMapping("/resetPassword")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request){
        boolean isPasswordReset = userService.setNewPassword(request.email, request.password, request.otp);
        return new ApiResponse(true, isPasswordReset,
                isPasswordReset ? "Password reset successful" : "Password reset failed"
        );
    }

    @GetMapping("/user")
    public ApiResponse getUser(){
        int id = authUtil.getAuthenticatedUserId();
        AppUser user = userService.getUser(id);
        if (user != null) return new ApiResponse(true, new AppUser(user.getId(), user.getEmail()));
        return new ApiResponse("User not found");
    }

    @DeleteMapping("/deleteUser")
    public ApiResponse deleteUser(){
        int id = authUtil.getAuthenticatedUserId();
        boolean deleted = userService.deleteUser(id);
        if (deleted) return new ApiResponse(true, "User deleted");
        return new ApiResponse("User not found");
    }
}

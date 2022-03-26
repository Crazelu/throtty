package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.devcrazelu.url_shortener.models.AppUser;
import tech.devcrazelu.url_shortener.models.requests.AuthRequest;
import tech.devcrazelu.url_shortener.models.requests.ResetPasswordRequest;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import tech.devcrazelu.url_shortener.services.UserService;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createAccount")
    public ApiResponse createAccount(@RequestBody AuthRequest request){
       AppUser user = userService.createUser(request.email, request.password);
        if(user != null){
            return new ApiResponse(true, user.getId());
        }
        return new ApiResponse("Account creation failed");
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthRequest request){
        UUID id =  userService.verifyCredentials(request.email, request.password);
        if(id != null){
            return new ApiResponse(true, id);
        }
        return new ApiResponse("Invalid credentials");
    }

    @PostMapping("/forgotPassword")
    public ApiResponse forgotPassword(@PathVariable String email){
        userService.forgotPassword(email);
        return new ApiResponse(true, "", "Check your email for an OTP");
    }

    @PostMapping("/resetPassword")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request){
        boolean isPasswordReset = userService.setNewPassword(request.email, request.password, request.otp);
        return new ApiResponse(true, isPasswordReset,
                isPasswordReset? "Password reset successful": "Password reset failed"
        );
    }

    @GetMapping("/user/{id}")
    public ApiResponse getUser(@PathVariable UUID id){
        AppUser user = userService.getUser(id);
        if(user != null) return new ApiResponse(true, user);
        return new ApiResponse("User not found");
    }

    @PostMapping("/deleteUser/{id}")
    public ApiResponse deleteUser(@PathVariable UUID id){
        boolean deleted = userService.deleteUser(id);
        if(deleted) return new ApiResponse(true, "User deleted");
        return new ApiResponse("User not found");
    }
}

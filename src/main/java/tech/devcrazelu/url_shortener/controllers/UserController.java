package tech.devcrazelu.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tech.devcrazelu.url_shortener.models.AppUser;
import tech.devcrazelu.url_shortener.models.requests.AuthRequest;
import tech.devcrazelu.url_shortener.models.requests.ResetPasswordRequest;
import tech.devcrazelu.url_shortener.models.responses.ApiResponse;
import tech.devcrazelu.url_shortener.services.UserService;
import tech.devcrazelu.url_shortener.utils.AuthUtil;
import tech.devcrazelu.url_shortener.utils.JwtUtil;

import java.util.UUID;

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


    @PostMapping("/createAccount")
    public ApiResponse createAccount(@RequestBody AuthRequest request){
       AppUser user = userService.createUser(request.email, request.password);
        String token = jwtUtil.generateToken(user.getId().toString());
        if(user != null ){
            try{
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getId().toString(), request.password));
            }catch(Exception e){
                return new ApiResponse(true, token,"Login to continue");
            }
            return new ApiResponse(true, token);
        }
        return new ApiResponse("Account creation failed");
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthRequest request){
        UUID id =  userService.verifyCredentials(request.email, request.password);

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id.toString(), request.password));
        }catch(Exception e){
            return new ApiResponse(e.getMessage());
        }

        String token = jwtUtil.generateToken(id.toString());
        if(id != null && token != null){
            return new ApiResponse(true, token);
        }
        return new ApiResponse("Invalid credentials");
    }

    @PostMapping("/forgotPassword")
    public ApiResponse forgotPassword(@PathVariable String email){
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
        UUID id = authUtil.getAuthenticatedUserId();
        AppUser user = userService.getUser(id);
        if (user != null) return new ApiResponse(true, user);
        return new ApiResponse("User not found");
    }

    @PostMapping("/deleteUser")
    public ApiResponse deleteUser(){
        UUID id = authUtil.getAuthenticatedUserId();
        boolean deleted = userService.deleteUser(id);
        if (deleted) return new ApiResponse(true, "User deleted");
        return new ApiResponse("User not found");
    }
}

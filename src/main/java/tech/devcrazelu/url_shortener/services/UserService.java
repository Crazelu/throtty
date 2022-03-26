package tech.devcrazelu.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.models.AppUser;
import tech.devcrazelu.url_shortener.repositories.UserRepository;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    private String encodePassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public AppUser createUser(String email, String password){
        String hashedPassword =encodePassword(password);
        AppUser user = new AppUser(UUID.randomUUID(), email, hashedPassword);
        boolean created =  userRepository.createUser(user);
        return created? user: null;
    }

    public AppUser getUser(UUID id){
        return userRepository.getUserById(id.toString());
    }

  public boolean updateUser(UUID id, String password){
        String hashedPassword = encodePassword(password);
        AppUser user = getUser(id);

        user.setPassword(hashedPassword);
        return userRepository.updateUser(user);
    }

  public  boolean deleteUser(UUID id){
       return userRepository.deleteUser(id.toString());
    }

   public UUID verifyCredentials(String email, String password){
      UUID id = userRepository.findUserByEmailAndPassword(email, password);
        return id;
    }

    public void forgotPassword(String email){
       //verify this email is in db

        //then generate otp and store in otps table

        //send otp with email service
        emailService.sendEmail(email, 1);
    }

    private boolean isOtpValid(int otp){
       //todo: check otp in table, grab it's expiration and check if it hasn't gone past current time
       return true;
    }

    public boolean setNewPassword(String email, String password, int otp){
       if (isOtpValid(otp)){
           //todo: update user record in db
           return true;
        }
       return false;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AppUser user = getUser(UUID.fromString(id));
        return new User(user.getId().toString(), user.getPassword(), new ArrayList<>());
    }
}

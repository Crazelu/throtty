package tech.devcrazelu.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.exceptions.ResourceCreationException;
import tech.devcrazelu.url_shortener.exceptions.ResourceNotFoundException;
import tech.devcrazelu.url_shortener.models.AppUser;
import tech.devcrazelu.url_shortener.repositories.UserRepository;
import java.util.ArrayList;

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
        AppUser user = new AppUser(email, hashedPassword);
        boolean created =  userRepository.createUser(user);
        if(!created) throw new ResourceCreationException("Account creation failed");
        return user;
    }

    private AppUser findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    public AppUser getUser(int id){
        return userRepository.getUserById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

  public boolean updateUser(int id, String password){
        String hashedPassword = encodePassword(password);
        AppUser user = getUser(id);

        user.setPassword(hashedPassword);
        return userRepository.updateUser(user);
    }

  public  boolean deleteUser(int id){
       return userRepository.deleteUser(id);
    }

   public int verifyCredentials(String email, String password){
      int id = userRepository.findUserByEmailAndPassword(email, password);
      if(id == -1) throw new ResourceNotFoundException("User not found");
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
          AppUser user = findUserByEmail(email);
          if(user!= null)
           return updateUser(user.getId(), password);
        }
       return false;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AppUser user = getUser(Integer.decode(id));
        if(user==null) throw new UsernameNotFoundException("Account not found");
        return new User(String.valueOf(user.getId()), user.getPassword(), new ArrayList<>());
    }
}

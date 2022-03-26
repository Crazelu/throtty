package tech.devcrazelu.url_shortener.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.models.AppUser;
import tech.devcrazelu.url_shortener.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

   public AppUser createUser(String email, String password){
        String hashedPassword = DigestUtils.sha256Hex(password);
        AppUser user = new AppUser(UUID.randomUUID(), email, hashedPassword);
        boolean created =  userRepository.createUser(user);
        return created? user: null;
    }

    public AppUser getUser(UUID id){
        return userRepository.getUserById(id.toString());
    }

  public boolean updateUser(UUID id, String password){
        String hashedPassword = DigestUtils.sha256Hex(password);
        AppUser user = getUser(id);

        user.setPassword(hashedPassword);
        return userRepository.updateUser(user);
    }

  public  boolean deleteUser(UUID id){
       return userRepository.deleteUser(id.toString());
    }

   public UUID verifyCredentials(String email, String password){
        String hashedPassword = DigestUtils.sha256Hex(password);
      UUID id = userRepository.findUserByEmailAndPassword(email, hashedPassword);
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
}

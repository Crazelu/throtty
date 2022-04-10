package tech.devcrazelu.url_shortener.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import tech.devcrazelu.url_shortener.models.AppUser;
import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserRepository {
    @Value("${datasource.url}")
    private String url;

    public boolean createUser(AppUser user){
        boolean created = false;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
           final String query = "insert into users (email, password, verified, fromOAuth) values (?, ?, ?, ?)";

           ps = connection.prepareStatement(query);

           ps.setString(1,user.getEmail());
           ps.setString(2,user.getPassword());
           ps.setBoolean(3, false);
           ps.setBoolean(4, user.isRegisteredFromOAuth());

          ps.execute();
         created = ps.getUpdateCount() == 1;

        }catch(Exception e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ignored) {
                }
            }
            try{
                connection.close();
            }catch(Exception e){
                Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            }
        }

        return created;

    }

    public int findUserByEmailAndPassword(String email, String password){
      int userId = -1;
      Connection connection = null;
      PreparedStatement ps = null;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

      try{
          connection = DriverManager.getConnection(url);

         final String query = "SELECT password, id FROM `users`"
                 + "WHERE email = ?";

              ps = connection.prepareStatement(query);
              ps.setString(1, email);

        ResultSet result = ps.executeQuery();

        while(result.next()){
            String retrievedPassword = result.getString("password");
            if(encoder.matches(password, retrievedPassword)){
                userId = result.getInt("id");
            }
        }

      }catch(Exception e){
          Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
      }finally {
          if (ps != null) {
              try {
                  ps.close();
              } catch (Exception ignored) {
              }
          }
          try{
              connection.close();
          }catch(Exception e){
              Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
          }
      }
      return userId;
  }

    public Optional<AppUser> getUserById(int id){
        AppUser user = null;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "select email, password from `users` where id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

            while(result.next()){
                String email = result.getString("email");
                String password = result.getString("password");
                user = new AppUser(id, email, password);
            }

        } catch(Exception e){
        Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
    }finally {
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception ignored) {
            }
        }
        try{
            connection.close();
        }catch(Exception e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }
        return Optional.ofNullable(user);
    }

    public boolean deleteUser(int id){
        Connection connection = null;
        PreparedStatement ps = null;
        boolean deleted = false;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "delete from `users` where id = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            ps.execute();
            deleted = ps.getUpdateCount() == 1;

        }catch(Exception e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ignored) {
                }
            }
            try{
                connection.close();
            }catch(Exception e){
                Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            }
        }

        return deleted;
    }

    public boolean updateUser(AppUser user){
        boolean updated = false;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "update `users` set password = ?"+
                    "where id = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getPassword());
            ps.setInt(2, user.getId());

            ps.execute();
            updated = ps.getUpdateCount()==1;

        }catch(Exception e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ignored) {
                }
            }
            try{
                connection.close();
            }catch(Exception e){
                Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            }
        }
        return updated;
    }

    public Optional<AppUser> findUserByEmail(String email){
        AppUser user = null;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "select id, fromOAuth, verified from `users` where email = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet result = ps.executeQuery();

            while(result.next()){
                int id = result.getInt("id");
                boolean fromOAuth = result.getBoolean("fromOAuth");
                boolean verified = result.getBoolean("verified");
                user = new AppUser(id, email, verified, fromOAuth);
            }

        } catch(Exception e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ignored) {
                }
            }
            try{
                connection.close();
            }catch(Exception e){
                Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            }
        }
        return Optional.ofNullable(user);
    }
}

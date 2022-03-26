package tech.devcrazelu.url_shortener.repositories;

import org.springframework.stereotype.Repository;
import tech.devcrazelu.url_shortener.models.AppUser;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserRepository {
    static final String url = "jdbc:mysql://localhost:3306/urlshortenerdb?user=root&password=897$312Game&autoReconnect=true&useSSL=false";

    public boolean createUser(AppUser user){
        boolean created = false;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
           final String query = "insert into users values (?, ?, ?)";

           ps = connection.prepareStatement(query);

           ps.setString(1,user.getId().toString());
           ps.setString(2,user.getPassword());
           ps.setString(3,user.getEmail());

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

    public UUID findUserByEmailAndPassword(String email, String password){
      UUID userId = null;
      Connection connection = null;
      PreparedStatement ps = null;

      try{
          connection = DriverManager.getConnection(url);

         final String query = "SELECT password, id FROM `users`"
                 + "WHERE email = ?";

              ps = connection.prepareStatement(query);
              ps.setString(1, email);

        ResultSet result = ps.executeQuery();

        while(result.next()){
            String retrievedPassword = result.getString("password");
            if(retrievedPassword.equals(password)){
                userId = UUID.fromString(result.getString("id"));
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

    public AppUser getUserById(String id){
        AppUser user = null;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "select email from `users` where id = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ResultSet result = ps.executeQuery();

            while(result.next()){
                String email = result.getString("email");
                user = new AppUser(UUID.fromString(id), email);
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
        return user;
    }

    public boolean deleteUser(String id){
        Connection connection = null;
        PreparedStatement ps = null;
        boolean deleted = false;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "delete from `users` where id = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, id);

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
            ps.setString(2, user.getId().toString());

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
}

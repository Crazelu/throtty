package tech.devcrazelu.url_shortener.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tech.devcrazelu.url_shortener.exceptions.ResourceCreationException;
import tech.devcrazelu.url_shortener.models.ShortenedUrl;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UrlRepository {
    @Value("${datasource.url}")
    private String url;

    public boolean createShortUrl(String shortUrl, String longUrl, int userId){
        boolean created = false;
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            connection = DriverManager.getConnection(url);
            final String query = "insert into urls values (?, ?, ?, ?)";

            ps = connection.prepareStatement(query);

            ps.setString(1, shortUrl);
            ps.setString(2, longUrl);
            ps.setInt(3, 0);
            ps.setInt(4, userId);

            ps.execute();
            created = ps.getUpdateCount() == 1;

        }catch(Exception e){
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            Logger.getAnonymousLogger().log(Level.WARNING, e.getClass().getTypeName());
            if(e.getClass().getTypeName().equals("com.mysql.cj.jdbc.exceptions.MysqlDataTruncation")) throw new ResourceCreationException("Short URL cannot be more than 20 characters long");
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

    public String getLongUrl(String shortUrl){
        Connection connection = null;
        PreparedStatement ps = null;
        String longUrl = null;
        try{
            final String query = "select longUrl from `urls` where shortUrl = ?";
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);

            ps.setString(1, shortUrl);
            ResultSet result = ps.executeQuery();

            while(result.next()){
                longUrl = result.getString("longUrl");
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

        return longUrl;
    }

    public boolean deleteShortUrl(String shortUrl){
        boolean deleted = false;
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            final String query = "delete from `urls` where shortUrl = ?";
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);

            ps.setString(1, shortUrl);
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

    private int getClickCount(String shortUrl){
        Connection connection = null;
        PreparedStatement ps = null;
        int clickCount = 0;
        try{
            final String query = "select clickCount from urls where shortUrl = ?";
            connection  = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);
            ps.setString(1, shortUrl);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                clickCount = result.getInt("clickCount");
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
        return clickCount;
    }

    public void updateClickCount(String shortUrl){
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            final String query = "update `urls` set clickCount = ? where shortUrl = ?";
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);

            int currentClickCount = getClickCount(shortUrl);

            ps.setInt(1, currentClickCount+1);
            ps.setString(2,shortUrl );

            ps.execute();

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
    }

    public ArrayList<ShortenedUrl> getShortUrlsForUser(int userId){
        Connection connection = null;
        PreparedStatement ps = null;
        ArrayList<ShortenedUrl> shortenedUrls = new ArrayList<>();
        try{
            final String query = "select * from `urls` where userId = ?";
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                String shortUrl = result.getString("shortUrl");
                String longUrl = result.getString("longUrl");
                int clickCount = result.getInt("clickCount");
                ShortenedUrl shortenedUrl = new ShortenedUrl(longUrl, shortUrl, clickCount);
                shortenedUrls.add(shortenedUrl);
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
        return shortenedUrls;
    }
}

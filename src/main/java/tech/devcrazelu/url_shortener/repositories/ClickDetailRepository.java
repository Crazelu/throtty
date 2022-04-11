package tech.devcrazelu.url_shortener.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tech.devcrazelu.url_shortener.models.ClickDetail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class ClickDetailRepository {
    @Value("${datasource.url}")
    private String url;

    public boolean createClickDetail(ClickDetail clickDetail){
        boolean created = false;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "insert into click_details (flag, country, clicks, shortUrl) values (?, ?, ?, ?)";

            ps = connection.prepareStatement(query);

            ps.setString(1, clickDetail.getFlag());
            ps.setString(2, clickDetail.getCountry());
            ps.setInt(3, 1);
            ps.setString(4, clickDetail.getShortUrl());

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

    public boolean updateClickDetail(int id){
        boolean updated = false;
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = DriverManager.getConnection(url);
            final String query = "update `click_details` set clicks = ? where id = ?";

            ps = connection.prepareStatement(query);

            ps.setInt(1, getClickCount(id)+1);
            ps.setInt(2, id);

            ps.execute();
            updated = ps.getUpdateCount() == 1;

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

    private int getClickCount(int id){
        Connection connection = null;
        PreparedStatement ps = null;
        int clickCount = 0;
        try{
            final String query = "select clicks from click_details where id = ?";
            connection  = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                clickCount = result.getInt("clicks");
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

    public ClickDetail getClickDetail(String shortUrl, String country){
        Connection connection = null;
        PreparedStatement ps = null;
        ClickDetail clickDetail = null;
        try{
            final String query = "select * from click_details where shortUrl = ? and country = ?";
            connection  = DriverManager.getConnection(url);
            ps = connection.prepareStatement(query);
            ps.setString(1, shortUrl);
            ps.setString(2, country);

            ResultSet result = ps.executeQuery();

            while(result.next()){
                int clicks = result.getInt("clicks");
                int id = result.getInt("id");
                String flag = result.getString("flag");
                clickDetail = new ClickDetail(id, flag, country, clicks, shortUrl);
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
        return clickDetail;
    }
}

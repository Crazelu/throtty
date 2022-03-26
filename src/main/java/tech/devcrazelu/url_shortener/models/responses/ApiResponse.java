package tech.devcrazelu.url_shortener.models.responses;



public class ApiResponse{
    public final boolean success;
    public  Object data;
    public final String message;

    public ApiResponse(boolean success, Object data){
        this.success = success;
        this.data = data;
        this.message = "";
    }

    public ApiResponse(boolean success, Object data, String message){
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public ApiResponse(String message){
        this.success = false;
        this.message = message;
    }
}
package tech.devcrazelu.url_shortener.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.devcrazelu.url_shortener.services.IPInterceptorService;

import javax.servlet.http.HttpServletRequest;

@Service
public class IPUtil {
    private String ip;

    public String getIp() {
        return ip;
    }

    @Autowired
    IPInterceptorService IPInterceptorService;

    public void setIpAddress(HttpServletRequest request){
        ip = IPInterceptorService.getClientIp(request);
    }

}

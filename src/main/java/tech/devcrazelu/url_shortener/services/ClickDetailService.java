package tech.devcrazelu.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.devcrazelu.url_shortener.models.ClickDetail;
import tech.devcrazelu.url_shortener.repositories.ClickDetailRepository;
import tech.devcrazelu.url_shortener.utils.IPUtil;

import java.time.Duration;

@Service
public class ClickDetailService {
    @Value("${location.service.client.url}")
    private String locationServiceClientUrl;

    @Value("${flag.service.url}")
    private String flagServiceUrl;

    @Autowired
    IPUtil ipUtil;
    @Autowired
    ClickDetailRepository clickDetailRepository;

    public void updateClickDetail(String shortUrl){
        String location = getLocationForIP();
        ClickDetail clickDetail = clickDetailRepository.getClickDetail(shortUrl, location);

        if(clickDetail!= null){
            clickDetailRepository.updateClickDetail(clickDetail.getId());
        }else{
            clickDetailRepository.createClickDetail(
                    new ClickDetail(
                            location.equalsIgnoreCase("unknown")? "": flagServiceUrl+location,
                            location,0, shortUrl
                    )
            );
        }
    }

    private WebClient locationServiceClient() {
        return WebClient.create(locationServiceClientUrl);
    }

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    private String getLocationForIP(){
        return locationServiceClient()
                .get()
                .uri(ipUtil.getIp())
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional(REQUEST_TIMEOUT).orElse("Unknown");
    }
}

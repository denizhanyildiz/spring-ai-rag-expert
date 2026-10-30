package ai.project.spring_ai_rag.client;

import ai.project.spring_ai_rag.model.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apiNinjasWeather", url = "${api-ninjas.url}", configuration = ApiNinjasFeignConfig.class)
public interface WeatherClient {

    @GetMapping(value = "/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    WeatherResponse getWeather(@RequestParam("lat") Double lat,
                               @RequestParam("lon") Double lon);
}

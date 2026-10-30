package ai.project.spring_ai_rag.service;

import ai.project.spring_ai_rag.client.WeatherClient;
import ai.project.spring_ai_rag.model.WeatherRequest;
import ai.project.spring_ai_rag.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherServiceFunction implements Function<WeatherRequest, WeatherResponse> {

    private final WeatherClient weatherClient;

    @Override
    public WeatherResponse apply(WeatherRequest weatherRequest) {
        log.info("Requesting weather for: {}", weatherRequest);
        return weatherClient.getWeather(weatherRequest.lat(), weatherRequest.lon());
    }

}

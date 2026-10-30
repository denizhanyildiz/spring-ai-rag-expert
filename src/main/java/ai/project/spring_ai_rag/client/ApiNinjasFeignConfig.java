package ai.project.spring_ai_rag.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Applied only to {@link WeatherClient}; deliberately not a @Configuration so it doesn't affect other Feign clients.
 */
public class ApiNinjasFeignConfig {

    @Bean
    public RequestInterceptor apiNinjasKeyInterceptor(@Value("${api-ninjas.key}") String apiNinjasKey) {
        return requestTemplate -> requestTemplate.header("X-Api-Key", apiNinjasKey);
    }
}

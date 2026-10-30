package ai.project.spring_ai_rag.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.math.BigDecimal;

public record WeatherResponse(@JsonProperty("wind_speed") @JsonPropertyDescription("WindSpeed in KMH") BigDecimal windSpeed,
                              @JsonProperty("wind_degrees") @JsonPropertyDescription("Direction of wind") Integer windDegrees,
                              @JsonPropertyDescription("Current Temperature in Celsius") Integer temp,
                              @JsonPropertyDescription("Current Humidity") Integer humidity,
                              @JsonPropertyDescription("Epoch time of sunset GMT ") Integer sunset,
                              @JsonPropertyDescription("Epoch time of Sunrise GMT ") Integer sunrise,
                              @JsonProperty("min_temp") @JsonPropertyDescription("Low Temperature in Celsius") Integer minTemp,
                              @JsonProperty("cloud_pct") @JsonPropertyDescription("Cloud Coverage Percentage") Integer cloudPct,
                              @JsonProperty("feels_like") @JsonPropertyDescription("Temperature in Celsius") Integer feelsLike,
                              @JsonProperty("max_temp") @JsonPropertyDescription("MaximumTemperature in Celsius") Integer maxTemp) {
}

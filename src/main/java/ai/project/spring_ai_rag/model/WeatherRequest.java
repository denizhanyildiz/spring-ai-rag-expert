package ai.project.spring_ai_rag.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Weather API request. Convert the place the user asks about into its latitude and longitude.")
public record WeatherRequest(@JsonProperty(required = true, value = "lat")
                             @JsonPropertyDescription("Latitude of the location in decimal degrees, e.g. 51.5072 for London") Double lat,
                             @JsonProperty(required = true, value = "lon")
                             @JsonPropertyDescription("Longitude of the location in decimal degrees, e.g. -0.1276 for London") Double lon) {
}

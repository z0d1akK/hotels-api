package com.zodiakk.hotelsapi.hotel.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Hotel search filter. All provided fields are combined with a logic of AND operator.")
public class HotelSearchFilterDto {

    @Schema(description = "Case-insensitive name contains", example = "hotel")
    private String name;

    @Schema(description = "Case-insensitive brand contains", example = "brand")
    private String brand;

    @Schema(description = "Case-insensitive city contains", example = "minsk")
    private String city;

    @Schema(description = "Case-insensitive country contains", example = "belarus")
    private String country;

    @Schema(description = "Hotel must contain all listed amenities", example = "[\"Free WiFi\"]")
    private List<String> amenities;
}

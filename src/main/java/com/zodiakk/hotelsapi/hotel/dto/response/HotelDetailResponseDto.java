package com.zodiakk.hotelsapi.hotel.dto.response;

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
@Schema(description = "Detailed hotel information")
public class HotelDetailResponseDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Hotel")
    private String name;

    @Schema(example = "bla bla bla")
    private String description;

    @Schema(example = "Brand")
    private String brand;

    private AddressResponseDto address;

    private ContactsResponseDto contacts;

    private ArrivalTimeResponseDto arrivalTime;

    @Schema(description = "Hotel amenities", example = "[\"Free WiFi\", \"Free parking\"]")
    private List<String> amenities;
}

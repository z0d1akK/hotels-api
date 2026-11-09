package com.zodiakk.hotelsapi.hotel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Hotel address details")
public class AddressResponseDto {

    @Schema(example = "67")
    private Integer houseNumber;

    @Schema(example = "Pyshkina Street")
    private String street;

    @Schema(example = "Minsk")
    private String city;

    @Schema(example = "Belarus")
    private String country;

    @Schema(example = "220025")
    private String postCode;
}

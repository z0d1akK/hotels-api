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
@Schema(description = "Short hotel information")
public class HotelShortResponseDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Hotel")
    private String name;

    @Schema(example = "bla bla bla")
    private String description;

    @Schema(description = "Formatted address", example = "9 Pyshkina Street, Minsk, 220025, Belarus")
    private String address;

    @Schema(example = "+375 33 333-33-33")
    private String phone;
}

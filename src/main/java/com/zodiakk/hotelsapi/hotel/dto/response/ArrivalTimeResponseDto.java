package com.zodiakk.hotelsapi.hotel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Hotel arrival policy")
public class ArrivalTimeResponseDto {

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "14:00", type = "string")
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    @Schema(example = "12:00", type = "string")
    private LocalTime checkOut;

    @Schema(example = "Europe/Minsk")
    private String timeZone;
}

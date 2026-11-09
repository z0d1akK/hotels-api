package com.zodiakk.hotelsapi.hotel.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zodiakk.hotelsapi.common.message.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Hotel check-in / check-out policy in hotel local time")
public class ArrivalTimeRequestDto {

    @NotNull(message = ValidationMessages.CHECK_IN_REQUIRED)
    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "Check-in time", example = "14:00", type = "string", pattern = "HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "Check-out time", example = "12:00", type = "string", pattern = "HH:mm")
    private LocalTime checkOut;

    @NotBlank(message = ValidationMessages.TIME_ZONE_REQUIRED)
    @Schema(description = "IANA time zone of the hotel", example = "Europe/Minsk", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timeZone;
}

package com.zodiakk.hotelsapi.hotel.dto.request;

import com.zodiakk.hotelsapi.common.message.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Payload for creating a hotel")
public class HotelCreateDto {

    @NotBlank(message = ValidationMessages.HOTEL_NAME_REQUIRED)
    @Schema(description = "Hotel name", example = "Hotel Minsk", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 4000, message = ValidationMessages.HOTEL_DESCRIPTION_SIZE)
    @Schema(description = "Optional hotel description", example = "Awful description for nonexistent hotel lmao")
    private String description;

    @NotBlank(message = ValidationMessages.HOTEL_BRAND_REQUIRED)
    @Schema(description = "Hotel brand", example = "HotelW", requiredMode = Schema.RequiredMode.REQUIRED)
    private String brand;

    @NotNull(message = ValidationMessages.ADDRESS_REQUIRED)
    @Valid
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressRequestDto address;

    @NotNull(message = ValidationMessages.CONTACTS_REQUIRED)
    @Valid
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private ContactsRequestDto contacts;

    @NotNull(message = ValidationMessages.ARRIVAL_TIME_REQUIRED)
    @Valid
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private ArrivalTimeRequestDto arrivalTime;
}

package com.zodiakk.hotelsapi.hotel.dto.request;

import com.zodiakk.hotelsapi.common.message.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Schema(description = "Hotel address")
public class AddressRequestDto {

    @NotNull(message = ValidationMessages.HOUSE_NUMBER_REQUIRED)
    @Positive(message = ValidationMessages.HOUSE_NUMBER_POSITIVE)
    @Schema(description = "House number", example = "67", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer houseNumber;

    @NotBlank(message = ValidationMessages.STREET_REQUIRED)
    @Schema(description = "Street name", example = "Pyshkina Street", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @NotBlank(message = ValidationMessages.CITY_REQUIRED)
    @Schema(description = "City", example = "Minsk", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @NotBlank(message = ValidationMessages.COUNTRY_REQUIRED)
    @Schema(description = "Country", example = "Belarus", requiredMode = Schema.RequiredMode.REQUIRED)
    private String country;

    @NotBlank(message = ValidationMessages.POST_CODE_REQUIRED)
    @Schema(description = "Postal code", example = "220025", requiredMode = Schema.RequiredMode.REQUIRED)
    private String postCode;
}

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
@Schema(description = "Hotel contacts")
public class ContactsResponseDto {

    @Schema(example = "+375 29 333-33-33")
    private String phone;

    @Schema(example = "hotelemail.info@hotel.com")
    private String email;
}

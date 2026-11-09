package com.zodiakk.hotelsapi.hotel.dto.request;

import com.zodiakk.hotelsapi.common.message.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class ContactsRequestDto {

    @NotBlank(message = ValidationMessages.PHONE_REQUIRED)
    @Schema(description = "Phone number", example = "+375 29 111-22-33", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
    @Email(message = ValidationMessages.EMAIL_INVALID)
    @Schema(description = "Email address", example = "hotelemail.info@hotel.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}

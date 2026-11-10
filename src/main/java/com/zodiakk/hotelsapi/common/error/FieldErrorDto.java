package com.zodiakk.hotelsapi.common.error;

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
@Schema(description = "Field-level validation error")
public class FieldErrorDto {

    @Schema(example = "name")
    private String field;

    @Schema(example = "Hotel name is required")
    private String message;
}

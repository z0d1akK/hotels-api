package com.zodiakk.hotelsapi.hotel.controller;

import com.zodiakk.hotelsapi.common.constant.ApiPaths;
import com.zodiakk.hotelsapi.common.error.ErrorResponseDto;
import com.zodiakk.hotelsapi.common.message.ValidationMessages;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelDetailResponseDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;
import com.zodiakk.hotelsapi.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotel CRUD and amenities management")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping(ApiPaths.HOTELS)
    @Operation(summary = "List all hotels with short information")
    @ApiResponse(responseCode = "200", description = "Hotels list",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HotelShortResponseDto.class))))
    public List<HotelShortResponseDto> getAllHotels() {
        return hotelService.findAllShort();
    }

    @GetMapping(ApiPaths.HOTEL_BY_ID)
    @Operation(summary = "Get detailed hotel information by id")
    @ApiResponse(responseCode = "200", description = "Hotel found",
            content = @Content(schema = @Schema(implementation = HotelDetailResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Hotel not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public HotelDetailResponseDto getHotelById(@PathVariable @Positive(message = ValidationMessages.HOTEL_ID_POSITIVE) Long id) {
        return hotelService.findByIdDetail(id);
    }

    @PostMapping(ApiPaths.HOTELS)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new hotel")
    @ApiResponse(responseCode = "201", description = "Hotel created",
            content = @Content(schema = @Schema(implementation = HotelShortResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public HotelShortResponseDto createHotel(@Valid @RequestBody HotelCreateDto dto) {
        return hotelService.create(dto);
    }

    @PutMapping(ApiPaths.HOTEL_BY_ID)
    @Operation(summary = "Fully update hotel fields (amenities unchanged)")
    @ApiResponse(responseCode = "200", description = "Hotel updated",
            content = @Content(schema = @Schema(implementation = HotelDetailResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Hotel not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    @ApiResponse(responseCode = "409", description = "Optimistic lock conflict",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public HotelDetailResponseDto updateHotel(@PathVariable @Positive(message = ValidationMessages.HOTEL_ID_POSITIVE) Long id,
                                              @Valid @RequestBody HotelUpdateDto dto) {
        return hotelService.update(id, dto);
    }

    @DeleteMapping(ApiPaths.HOTEL_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete hotel by id")
    @ApiResponse(responseCode = "204", description = "Hotel deleted")
    @ApiResponse(responseCode = "404", description = "Hotel not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public void deleteHotel(@PathVariable @Positive(message = ValidationMessages.HOTEL_ID_POSITIVE) Long id) {
        hotelService.delete(id);
    }

    @PostMapping(value = ApiPaths.HOTEL_AMENITIES, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Replace hotel amenities list")
    @ApiResponse(responseCode = "200", description = "Amenities replaced",
            content = @Content(schema = @Schema(implementation = HotelDetailResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Hotel not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    @ApiResponse(responseCode = "409", description = "Conflict",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public HotelDetailResponseDto replaceAmenities(@PathVariable @Positive(message = ValidationMessages.HOTEL_ID_POSITIVE) Long id,
                                                   @RequestBody @NotNull(message = ValidationMessages.AMENITIES_LIST_REQUIRED)
                                                   List<@NotBlank(message = ValidationMessages.AMENITY_NAME_REQUIRED) String> amenities
    ) {
        return hotelService.replaceAmenities(id, amenities);
    }
}

package com.zodiakk.hotelsapi.hotel.controller;

import com.zodiakk.hotelsapi.common.constant.ApiPaths;
import com.zodiakk.hotelsapi.hotel.dto.filter.HotelSearchFilterDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;
import com.zodiakk.hotelsapi.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Search", description = "Hotel search by filters")
public class HotelSearchController {

    private final HotelService hotelService;

    @GetMapping(ApiPaths.SEARCH)
    @Operation(summary = "Search hotels by name, brand, city, country and amenities")
    @ApiResponse(responseCode = "200", description = "Filtered hotels",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HotelShortResponseDto.class))))
    public ResponseEntity<List<HotelShortResponseDto>> search(@ModelAttribute HotelSearchFilterDto filter) {
        return ResponseEntity.ok(hotelService.search(filter));
    }
}

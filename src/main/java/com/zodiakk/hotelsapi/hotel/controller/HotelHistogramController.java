package com.zodiakk.hotelsapi.hotel.controller;

import com.zodiakk.hotelsapi.common.constant.ApiPaths;
import com.zodiakk.hotelsapi.common.enums.HistogramParam;
import com.zodiakk.hotelsapi.common.error.ErrorResponseDto;
import com.zodiakk.hotelsapi.hotel.service.HotelHistogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Histogram", description = "Hotel aggregations by parameter")
public class HotelHistogramController {

    private final HotelHistogramService hotelHistogramService;

    @GetMapping(ApiPaths.HISTOGRAM)
    @Operation(summary = "Get hotel counts grouped by brand, city, country or amenities")
    @ApiResponse(responseCode = "200", description = "Histogram map")
    @ApiResponse(responseCode = "400", description = "Unknown parameter",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    public Map<String, Long> histogram(@PathVariable("param") String param) {
        return hotelHistogramService.histogram(HistogramParam.from(param));
    }
}

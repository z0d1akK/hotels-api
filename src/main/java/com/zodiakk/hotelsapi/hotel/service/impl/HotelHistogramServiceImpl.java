package com.zodiakk.hotelsapi.hotel.service.impl;

import com.zodiakk.hotelsapi.common.enums.HistogramParam;
import com.zodiakk.hotelsapi.common.message.ErrorMessages;
import com.zodiakk.hotelsapi.hotel.repository.AmenityRepository;
import com.zodiakk.hotelsapi.hotel.repository.HotelRepository;
import com.zodiakk.hotelsapi.hotel.service.HotelHistogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelHistogramServiceImpl implements HotelHistogramService {

    private final HotelRepository hotelRepository;

    private final AmenityRepository amenityRepository;

    @Override
    public Map<String, Long> histogram(HistogramParam param) {
        return switch (param) {
            case BRAND -> toHistogram(hotelRepository.countGroupByBrand());
            case CITY -> toHistogram(hotelRepository.countGroupByCity());
            case COUNTRY -> toHistogram(hotelRepository.countGroupByCountry());
            case AMENITIES -> toAmenityHistogram(amenityRepository.countHotelsGroupedByAmenity());
        };
    }

    private Map<String, Long> toHistogram(List<Object[]> rows) {
        Map<String, Long> result = new LinkedHashMap<>();

        for (Object[] row : rows) {
            String key = normalizeKey(row[0]);
            Long count = ((Number) row[1]).longValue();
            result.merge(key, count, Long::sum);
        }
        return result;
    }

    private Map<String, Long> toAmenityHistogram(List<Object[]> rows) {
        Map<String, Long> result = new LinkedHashMap<>();

        for (Object[] row : rows) {
            String key = row[0] == null ? ErrorMessages.UNSPECIFIED : String.valueOf(row[0]);
            Long count = ((Number) row[1]).longValue();
            result.put(key, count);
        }
        return result;
    }

    private String normalizeKey(Object value) {
        if (value == null) {
            return ErrorMessages.UNSPECIFIED;
        }
        String text = String.valueOf(value);
        return StringUtils.hasText(text) ? text : ErrorMessages.UNSPECIFIED;
    }
}

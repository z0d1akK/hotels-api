package com.zodiakk.hotelsapi.hotel.service.impl;

import com.zodiakk.hotelsapi.common.enums.HistogramParam;
import com.zodiakk.hotelsapi.common.message.ErrorMessages;
import com.zodiakk.hotelsapi.hotel.repository.AmenityRepository;
import com.zodiakk.hotelsapi.hotel.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelHistogramServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private HotelHistogramServiceImpl histogramService;

    @Test
    void histogramByBrand_mergesBlankAsUnspecified() {
        when(hotelRepository.countGroupByBrand()).thenReturn(List.<Object[]>of(
                new Object[]{"HotelW", 2L},
                new Object[]{"", 1L},
                new Object[]{null, 1L}
        ));

        Map<String, Long> result = histogramService.histogram(HistogramParam.BRAND);

        assertThat(result)
                .containsEntry("HotelW", 2L)
                .containsEntry(ErrorMessages.UNSPECIFIED, 2L);
    }

    @Test
    void histogramByCity() {
        when(hotelRepository.countGroupByCity()).thenReturn(List.<Object[]>of(new Object[]{"Minsk", 3L}));

        assertThat(histogramService.histogram(HistogramParam.CITY)).containsEntry("Minsk", 3L);
    }

    @Test
    void histogramByCountry() {
        when(hotelRepository.countGroupByCountry()).thenReturn(List.<Object[]>of(new Object[]{"Belarus", 4L}));

        assertThat(histogramService.histogram(HistogramParam.COUNTRY)).containsEntry("Belarus", 4L);
    }

    @Test
    void histogramByAmenities_includesZeroCounts() {
        when(amenityRepository.countHotelsGroupedByAmenity()).thenReturn(List.<Object[]>of(
                new Object[]{"Free WiFi", 5L},
                new Object[]{"Pool", 0L},
                new Object[]{null, 1L}
        ));

        Map<String, Long> result = histogramService.histogram(HistogramParam.AMENITIES);

        assertThat(result)
                .containsEntry("Free WiFi", 5L)
                .containsEntry("Pool", 0L)
                .containsEntry(ErrorMessages.UNSPECIFIED, 1L);
    }
}

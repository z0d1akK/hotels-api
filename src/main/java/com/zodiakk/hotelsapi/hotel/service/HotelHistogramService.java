package com.zodiakk.hotelsapi.hotel.service;

import com.zodiakk.hotelsapi.common.enums.HistogramParam;

import java.util.Map;

/**
 * Application service for hotel histogram aggregations.
 */
public interface HotelHistogramService {

    /**
     * Builds a histogram of hotels grouped by the given parameter.
     *
     * @param param grouping parameter
     * @return map of group key to hotel count
     */
    Map<String, Long> histogram(HistogramParam param);
}

package com.zodiakk.hotelsapi.common.enums;

import com.zodiakk.hotelsapi.common.exception.InvalidHistogramParamException;
import com.zodiakk.hotelsapi.common.message.ErrorMessages;

import java.util.Locale;

public enum HistogramParam {

    BRAND,
    CITY,
    COUNTRY,
    AMENITIES;

    public static HistogramParam from(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidHistogramParamException(ErrorMessages.HISTOGRAM_UNKNOWN_PARAM.formatted(raw));
        }
        try {
            return HistogramParam.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidHistogramParamException(ErrorMessages.HISTOGRAM_UNKNOWN_PARAM.formatted(raw));
        }
    }
}

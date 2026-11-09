package com.zodiakk.hotelsapi.common.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {

    public static final String HOTEL_NOT_FOUND = "Hotel with id %d not found";

    public static final String HOTEL_OPTIMISTIC_LOCK = "Hotel was modified concurrently, please retry";

    public static final String AMENITY_CONFLICT = "Amenity could not be created due to a concurrent conflict";

    public static final String INVALID_TIME_ZONE = "Invalid time zone: %s";

    public static final String HISTOGRAM_UNKNOWN_PARAM = "Unknown histogram parameter: %s";

    public static final String VALIDATION_FAILED = "Validation failed";

    public static final String UNSPECIFIED = "не указано";
}
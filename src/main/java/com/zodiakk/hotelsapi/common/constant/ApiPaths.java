package com.zodiakk.hotelsapi.common.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPaths {

    public static final String API_PREFIX = "/property-view";

    public static final String HOTELS = API_PREFIX + "/hotels";

    public static final String HOTEL_BY_ID = HOTELS + "/{id}";

    public static final String HOTEL_AMENITIES = HOTEL_BY_ID + "/amenities";

    public static final String SEARCH = API_PREFIX + "/search";

    public static final String HISTOGRAM = API_PREFIX + "/histogram/{param}";
}

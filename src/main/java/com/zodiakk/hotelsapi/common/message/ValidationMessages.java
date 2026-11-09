package com.zodiakk.hotelsapi.common.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {

    public static final String HOTEL_NAME_REQUIRED = "Hotel name is required";
    public static final String HOTEL_BRAND_REQUIRED = "Hotel brand is required";
    public static final String HOTEL_DESCRIPTION_SIZE = "Hotel description must be at most 4000 characters";

    public static final String ADDRESS_REQUIRED = "Address is required";
    public static final String HOUSE_NUMBER_REQUIRED = "House number is required";
    public static final String HOUSE_NUMBER_POSITIVE = "House number must be positive";
    public static final String STREET_REQUIRED = "Street is required";
    public static final String CITY_REQUIRED = "City is required";
    public static final String COUNTRY_REQUIRED = "Country is required";
    public static final String POST_CODE_REQUIRED = "Post code is required";

    public static final String CONTACTS_REQUIRED = "Contacts are required";
    public static final String PHONE_REQUIRED = "Phone is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email must be a valid email address";

    public static final String ARRIVAL_TIME_REQUIRED = "Arrival time is required";
    public static final String CHECK_IN_REQUIRED = "Check-in time is required";
    public static final String TIME_ZONE_REQUIRED = "Time zone is required";

    public static final String AMENITY_NAME_REQUIRED = "Amenity name is required";
    public static final String AMENITIES_LIST_REQUIRED = "Amenities list is required";
    public static final String HOTEL_ID_POSITIVE = "Hotel id must be positive";
}

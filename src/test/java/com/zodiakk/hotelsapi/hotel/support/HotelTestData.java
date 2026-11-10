package com.zodiakk.hotelsapi.hotel.support;

import com.zodiakk.hotelsapi.hotel.dto.request.AddressRequestDto;
import com.zodiakk.hotelsapi.hotel.dto.request.ArrivalTimeRequestDto;
import com.zodiakk.hotelsapi.hotel.dto.request.ContactsRequestDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.entity.Address;
import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import com.zodiakk.hotelsapi.hotel.entity.ArrivalTime;
import com.zodiakk.hotelsapi.hotel.entity.Contacts;
import com.zodiakk.hotelsapi.hotel.entity.Hotel;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.List;

public final class HotelTestData {

    public static final String NAME = "Hotel Minsk";
    public static final String DESCRIPTION = "Awful description for nonexistent hotel lmao";
    public static final String BRAND = "HotelW";
    public static final String UPDATED_NAME = "Updated Hotel";
    public static final String UPDATED_BRAND = "Brandi";
    public static final int HOUSE_NUMBER = 67;
    public static final String STREET = "Pyshkina Street";
    public static final String CITY = "Minsk";
    public static final String COUNTRY = "Belarus";
    public static final String POST_CODE = "220025";
    public static final String PHONE = "+375 29 111-22-33";
    public static final String EMAIL = "hotelemail.info@hotel.com";
    public static final String TIME_ZONE = "Europe/Minsk";
    public static final String FORMATTED_ADDRESS = "67 Pyshkina Street, Minsk, 220025, Belarus";
    public static final List<String> AMENITIES = List.of("Free WiFi", "Free parking");

    private HotelTestData() {}

    public static HotelCreateDto createDto() {
        return HotelCreateDto.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .brand(BRAND)
                .address(addressRequest())
                .contacts(contactsRequest())
                .arrivalTime(arrivalTimeRequest(TIME_ZONE))
                .build();
    }

    public static HotelUpdateDto updateDto() {
        return HotelUpdateDto.builder()
                .name(UPDATED_NAME)
                .description(DESCRIPTION)
                .brand(UPDATED_BRAND)
                .address(addressRequest())
                .contacts(contactsRequest())
                .arrivalTime(arrivalTimeRequest(TIME_ZONE))
                .build();
    }

    public static AddressRequestDto addressRequest() {
        return AddressRequestDto.builder()
                .houseNumber(HOUSE_NUMBER)
                .street(STREET)
                .city(CITY)
                .country(COUNTRY)
                .postCode(POST_CODE)
                .build();
    }

    public static ContactsRequestDto contactsRequest() {
        return ContactsRequestDto.builder()
                .phone(PHONE)
                .email(EMAIL)
                .build();
    }

    public static ArrivalTimeRequestDto arrivalTimeRequest(String timeZone) {
        return ArrivalTimeRequestDto.builder()
                .checkIn(LocalTime.of(14, 0))
                .checkOut(LocalTime.of(12, 0))
                .timeZone(timeZone)
                .build();
    }

    public static Hotel hotel(Long id) {
        return Hotel.builder()
                .id(id)
                .name(NAME)
                .description(DESCRIPTION)
                .brand(BRAND)
                .address(Address.builder()
                        .houseNumber(HOUSE_NUMBER)
                        .street(STREET)
                        .city(CITY)
                        .country(COUNTRY)
                        .postCode(POST_CODE)
                        .build())
                .contacts(Contacts.builder()
                        .phone(PHONE)
                        .email(EMAIL)
                        .build())
                .arrivalTime(ArrivalTime.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .timeZone(ZoneId.of(TIME_ZONE))
                        .build())
                .amenities(new LinkedHashSet<>())
                .version(0L)
                .build();
    }

    public static Amenity amenity(Long id, String name) {
        return Amenity.builder()
                .id(id)
                .name(name)
                .nameNormalized(name.toLowerCase())
                .build();
    }
}
